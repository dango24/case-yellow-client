package caseyellow.client.domain.system;

import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.exceptions.ConnectionTypeException;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.UserInterruptException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class SystemServiceImpl implements SystemService {

    private Logger log = Logger.getLogger(SystemServiceImpl.class);

    private static final String ETHERNET_IDENTIFIER = "eth";

    private MessagesService messagesService;

    @Autowired
    public SystemServiceImpl(MessagesService messagesService) {
        this.messagesService = messagesService;
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
    public void deleteDirectory(File directory) {
        try {
            if (nonNull(directory)) {
                FileUtils.deleteDirectory(directory);
            }
        } catch (IOException e) {
            log.error("Failed to delete file, cause: " + e.getMessage(), e);
        }
    }

    @Override
    public long copyURLToFile(String fileName, URL source, File destination, long fileSize) throws FileDownloadInfoException, UserInterruptException {
        long fileDownloadedDurationTimeInMs;

        try {
            URLConnection connection = source.openConnection();
            connection.setReadTimeout(5000);
            connection.connect();

            messagesService.startDownloadingFile(fileName);
            fileDownloadedDurationTimeInMs = downloadFile(destination, connection, fileSize);

            log.info("finish downloading file from: " + source.toString());

            return fileDownloadedDurationTimeInMs;

        } catch (IOException e) {
            messagesService.finishDownloadingFile();
            throw new FileDownloadInfoException("Failed to download file, " + e.getMessage(), e);
        }
    }

    private long downloadFile(File destination, URLConnection connection, long fileSize) {
        int count;
        final byte[] data = new byte[1024];

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(destination)){

             long startDownloadingTime = System.currentTimeMillis();

             while ((count = in.read(data, 0, 1024)) != -1) {
                 out.write(data, 0, count);

                 if (Thread.currentThread().isInterrupted()) {
                     throw new UserInterruptException("User cancel download file request");
                 }

                 messagesService.showDownloadFileProgress(out.getChannel().size(), fileSize);
             }

            return System.currentTimeMillis() - startDownloadingTime;

        } catch (IOException e) {
            throw new FileDownloadInfoException("Failed to download file, " + e.getMessage(), e);

        } finally {
            messagesService.finishDownloadingFile();
        }
    }

    @Override
    public String convertToMD5(File file)  {

        try (InputStream in = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(IOUtils.toByteArray(in));

            return DatatypeConverter.printHexBinary(md.digest());

        } catch (IOException | NoSuchAlgorithmException e) {
            log.error(String.format("Failed to convert to MD5, error: %s", e.getMessage(), e));
            return "UNKNOWN";
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
        return getUrl("http://checkip.amazonaws.com");
    }

    @Override
    public String getISP() {
        return getUrl("https://ipinfo.io/org");
    }

    private String getUrl(String url) {
        return IntStream.range(0, 10)
                        .mapToObj(i -> getUrlPayload(url))
                        .filter(ipAddress -> !ipAddress.equals(UNKNOWN_CONNECTION))
                        .findFirst()
                        .orElse(UNKNOWN_CONNECTION);
    }

    private String getUrlPayload(String url) {
        String ipAddress;

        try {
            ipAddress = IOUtils.toString(new URL(url), "UTF-8").replace("\n", "");

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