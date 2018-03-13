package caseyellow.client.domain.system;

import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.test.model.SnapshotMetadata;
import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static caseyellow.client.common.Utils.getSnapshotMetadataFile;
import static java.lang.StrictMath.toIntExact;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

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
        int readTimeOut = toIntExact(TimeUnit.SECONDS.toMillis(10));

        try {
            URLConnection connection = source.openConnection();
            connection.setReadTimeout(readTimeOut);
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

    @Override
    public void saveSnapshotHashToDisk(Test test) {
        List<String> snapshotMetadata =
                test.getComparisonInfoTests()
                    .stream()
                    .map(ComparisonInfo::getSpeedTestWebSite)
                    .filter(SpeedTestWebSite::isSucceed)
                    .map(info -> createSnapshotMetadata(info.getWebSiteDownloadInfoSnapshot(), info.getPath()).toString())
                    .collect(toList());

        try {
            if (!snapshotMetadata.isEmpty()) {
                log.info(String.format("save snapshot hash to disk: %s" , snapshotMetadata));
                Files.write(getSnapshotMetadataFile().toPath(), snapshotMetadata, UTF_8, APPEND, CREATE);
            }

        } catch (IOException e) {
            throw new TestException("Failed to write test snapshot metadata file, cause: " + e.getMessage(), e);
        }
    }

    @Override
    public String runCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);

        try (InputStream inputStream = process.getInputStream()) {

            return IOUtils.toString(inputStream, UTF_8);
        }
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

    private SnapshotMetadata createSnapshotMetadata(String webSiteDownloadInfoSnapshot, String s3Path) {
        String md5 = convertToMD5(new File(webSiteDownloadInfoSnapshot));
        return new SnapshotMetadata(md5, s3Path);
    }

}