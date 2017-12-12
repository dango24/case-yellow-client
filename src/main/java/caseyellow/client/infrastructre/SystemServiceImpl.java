package caseyellow.client.infrastructre;

import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.interfaces.SystemService;
import caseyellow.client.exceptions.ConnectionTypeException;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.InternalFailureException;
import caseyellow.client.exceptions.UserInterruptException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class SystemServiceImpl implements SystemService {

    private static final String ETHERNET_IDENTIFIER = "eth";

    private Logger log = Logger.getLogger(SystemServiceImpl.class);

    private Future<Long> copyURLToFileTask;
    private ExecutorService copyURLToFileService;

    @PostConstruct
    private void init() {
        copyURLToFileService = Executors.newSingleThreadExecutor();
    }

    @Override
    public SystemInfo getSystemInfo() {
        String connection = getConnection();
        String ipAddress = getPublicIPAddress();
        String os = getOperationSystem();
        String browser = getBrowser();

        return new SystemInfo(os, browser, ipAddress, connection);
    }

    @Override
    public void deleteDirectory(File directory) throws IOException {
        FileUtils.deleteDirectory(directory);
    }

    @Override
    public long copyURLToFile(URL source, File destination) throws IOException, InternalFailureException {
        try {
            copyURLToFileTask = copyURLToFileService.submit(() -> executeCopyURLToFile(source, destination));
            return copyURLToFileTask.get(120, TimeUnit.MINUTES);

        } catch (InterruptedException | CancellationException e) {
            throw new UserInterruptException("User cancel download file request, " + e.getMessage(), e);

        } catch (ExecutionException e) {
            throw new InternalFailureException("Failed to download file, " + e.getMessage(), e);

        } catch (TimeoutException e) {
            log.error("Reach timeout of 30 minutes for url: " + source.toString());
            throw new InternalFailureException("Failed to download file, reach timeout of 30 minutes for url: " + source.toString() +
                                               " cause: " + e.getMessage(), e);
        }
    }

    private long executeCopyURLToFile(URL source, File destination) {
        String threadOriginalName = Thread.currentThread().getName();
        Thread.currentThread().setName("copy url to file thread");

        try (FileOutputStream fos = new FileOutputStream(destination);
            ReadableByteChannel rbc = Channels.newChannel(source.openStream())){

            long startDownloadingTime = System.currentTimeMillis();
//            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE); // TODO dangooooooooooooooooooo removeeeee
            long fileDownloadedDurationTimeInMs = System.currentTimeMillis() - startDownloadingTime;
            log.info("finish downloading file from: " + source.toString());

            return fileDownloadedDurationTimeInMs;

        } catch (IOException e) {
            log.error("Failed to download file from url: " + source.toString() + ", cause: " + e.getMessage(), e);
            throw new FileDownloadInfoException("Failed to download file from url: " + source.toString() + ", cause: " + e.getMessage());
        } finally {
            Thread.currentThread().setName(threadOriginalName);
        }
    }

    private long executeCopyURLToFile2(URL source, File destination) {
        String threadOriginalName = Thread.currentThread().getName();
        Thread.currentThread().setName("copy url to file thread");

        try {
            long startDownloadingTime = System.currentTimeMillis();
            FileUtils.copyURLToFile(source, destination);
            long fileDownloadedDurationTimeInMs = System.currentTimeMillis() - startDownloadingTime;
            log.info("finish downloading file from: " + source.toString());

            return fileDownloadedDurationTimeInMs;

        } catch (IOException e) {
            log.error("Failed to download file from url: " + source.toString() + ", cause: " + e.getMessage(), e);
            throw new FileDownloadInfoException("Failed to download file from url: " + source.toString() + ", cause: " + e.getMessage());
        } finally {
            Thread.currentThread().setName(threadOriginalName);
        }
    }

    @Override
    public void close() throws IOException {
        if (nonNull(copyURLToFileTask) && !copyURLToFileTask.isCancelled()) {
            copyURLToFileTask.cancel(true);
        }
    }

    @Override
    public String getImgMD5HashValue(String imgPath) {
        try {
            byte[] bytes = convertImgToByteArray(imgPath);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashResult = messageDigest.digest(bytes);

            return Hex.encodeHexString(hashResult);

        } catch (Exception e) {
           throw new InternalFailureException(e.getMessage(), e);
        }
    }

    private byte[] convertImgToByteArray(String imgPath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bufferedImage, "png", baos );
        baos.flush();

        return baos.toByteArray();
    }

    private String getOperationSystem() {
        return System.getProperty("os.name").toUpperCase();
    }

    private String getPublicIPAddress() {

        return IntStream.range(0, 10)
                        .mapToObj(i -> getPublicIPAddressFromAWS())
                        .filter(ipAddress -> !ipAddress.equals(UNKNOWN_CONNECTION))
                        .findFirst()
                        .orElse(UNKNOWN_CONNECTION);
    }

    private String getPublicIPAddressFromAWS() {
        String ipAddress;

        try {
            URL amazonAwsCheckIP = new URL("http://checkip.amazonaws.com");
            ipAddress = IOUtils.toString(amazonAwsCheckIP, "UTF-8").replace("\n", "");

        } catch (IOException e) {
            log.error("Failed to retrieve public IP address " + e.getMessage());
            ipAddress = UNKNOWN_CONNECTION;
        }

        if (ipAddress.equals(UNKNOWN_CONNECTION)) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new UserInterruptException("Sleep interrupt while retrieving ip address");
            }
        }

        return ipAddress;
    }

    private String getConnection() {
        NetworkInterface networkInterface;

        try {
            networkInterface = getInternetConnection();
            return isEthernetConnection(networkInterface) ? LAN_CONNECTION : WIFI_CONNECTION;

        } catch (Exception e) {
            log.error("Failed to find client connection type" + e.getMessage(), e);
            return UNKNOWN_CONNECTION;
        }
    }

    private NetworkInterface getInternetConnection() throws SocketException {

        return Collections.list(NetworkInterface.getNetworkInterfaces())
                          .stream()
                          .filter(this::isUp)
                          .filter(this::isHardwareAddress)
                          .findFirst()
                          .orElseThrow(() -> new ConnectionTypeException("Unknown connection"));
    }

    private boolean isUp(NetworkInterface networkInterface) {
        try {
            return networkInterface.isUp();
        } catch (SocketException e) {
            throw new ConnectionTypeException(e.getMessage());
        }
    }

    private boolean isHardwareAddress(NetworkInterface networkInterface) {
        try {
            return networkInterface.getHardwareAddress() != null;
        } catch (SocketException e) {
            throw new ConnectionTypeException(e.getMessage());
        }
    }

    private boolean isEthernetConnection(NetworkInterface networkInterface) {

        return Stream.of(networkInterface)
                     .filter(Objects::nonNull)
                     .map(NetworkInterface::getName)
                     .filter(Objects::nonNull)
                     .anyMatch(connectionName -> connectionName.contains(ETHERNET_IDENTIFIER));
    }

    private String getBrowser() {
        return "CHROME";
    }

}