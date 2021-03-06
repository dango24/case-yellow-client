package caseyellow.client.domain.system;

import caseyellow.client.domain.file.model.DownloadedFileDetails;
import caseyellow.client.domain.logger.services.CYLogger;
import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.test.model.SnapshotMetadata;
import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.MBeanServerConnection;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.sun.management.OperatingSystemMXBean;

import static caseyellow.client.common.FileUtils.getSnapshotMetadataFile;
import static caseyellow.client.domain.system.CommandExecutorServiceImpl.TIMEOUT_ERROR;
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

    private static CYLogger log = new CYLogger(SystemServiceImpl.class);
    private static final String TRACE_ROUTE_COMMAND = "tracert %s";

    private static final List<String> ETHERNET_IDENTIFIERS = Arrays.asList("eth", "eno", "enp", "ens", "enx");

    private MessagesService messagesService;
    private CommandExecutorService commandExecutorService;

    @Autowired
    public SystemServiceImpl(MessagesService messagesService, CommandExecutorService commandExecutorService) {
        this.messagesService = messagesService;
        this.commandExecutorService = commandExecutorService;
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
    public DownloadedFileDetails copyURLToFile(String fileName, URL source, File destination,
                                               long fileSize, boolean runTraceRoute, int timeoutInMin) throws FileDownloadInfoException, UserInterruptException {

        String traceRouteOutputPreviousDownloadFile = null;
        String traceRouteOutputAfterDownloadFile = null;
        String traceRouteAddress = source.getHost();
        String command = String.format(TRACE_ROUTE_COMMAND, traceRouteAddress);

        int readTimeOut = toIntExact(TimeUnit.SECONDS.toMillis(10));
        Pair<Long, Map<String, List<String>>> fileDownloadDurationAndHeaders;

        try {
            URLConnection connection = source.openConnection();
            connection.setReadTimeout(readTimeOut);
            connection.connect();

            if (runTraceRoute) {
                traceRouteOutputPreviousDownloadFile = runTraceRouteCommand(command, traceRouteAddress, timeoutInMin, String.format("Execute trace route: %s previously to downloading file", traceRouteAddress));
            }

            messagesService.startDownloadingFile(fileName);
            fileDownloadDurationAndHeaders = downloadFile(destination, connection, fileSize);
            log.info("finish downloading file from: " + source.toString());

            if (runTraceRoute) {
                traceRouteOutputAfterDownloadFile = runTraceRouteCommand(command, traceRouteAddress, timeoutInMin, String.format("Execute trace route: %s after downloading file", traceRouteAddress));
            }

            return new DownloadedFileDetails(fileDownloadDurationAndHeaders.getLeft(), traceRouteOutputPreviousDownloadFile, traceRouteOutputAfterDownloadFile, fileDownloadDurationAndHeaders.getRight());

        } catch (IOException e) {
            messagesService.finishDownloadingFile();
            throw new FileDownloadInfoException("Failed to download file, " + e.getMessage(), e);
        }
    }

    private String runTraceRouteCommand(String command, String traceRouteAddress, int timeoutInMin, String stateMessage) {
        messagesService.showMessage(stateMessage);
        log.info(String.format("Execute trace route: %s previously to downloading file", traceRouteAddress));
        String traceRouteOutput = commandExecutorService.executeCommand(command, timeoutInMin);

        if (TIMEOUT_ERROR.equals(traceRouteOutput)) {
            throw new FileDownloadInfoException("Reach file download timeout");
        }

        return traceRouteOutput;
    }

    private Pair<Long, Map<String, List<String>>> downloadFile(File destination, URLConnection connection, long fileSize) {
        int count;
        long fileDownloadedDurationTimeInMs;
        final byte[] data = new byte[1024];
        Map<String, List<String>> headers;

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

            fileDownloadedDurationTimeInMs = System.currentTimeMillis() - startDownloadingTime;

            headers = connection.getHeaderFields()
                                .entrySet()
                                .stream()
                                .filter(entry -> nonNull(entry.getKey()))
                                .filter(entry -> !entry.getKey().equalsIgnoreCase("null"))
                                .filter(entry -> !entry.getValue().isEmpty())
                                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));


            return new ImmutablePair<>(fileDownloadedDurationTimeInMs, headers);

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
        List<NetworkInterface> networkInterfaces;

        try {
            networkInterfaces = getInternetConnections().stream().filter(inter -> {
                try {
                    return inter.isUp() && !inter.isLoopback();
                } catch (SocketException e) {
                    e.printStackTrace();
                    return false;
                }
            }).collect(toList());

            if (networkInterfaces.stream().anyMatch(this::isEthernetConnection)) {
                return LAN_CONNECTION;
            } else {
                return WIFI_CONNECTION;
            }

        } catch (Exception e) {
            log.error("Failed to find client connection type" + e.getMessage(), e);
            return UNKNOWN_CONNECTION;
        }
    }

    private List<NetworkInterface> getInternetConnections() throws SocketException {

        return Collections.list(NetworkInterface.getNetworkInterfaces())
                          .stream()
                          .filter(this::isUp)
                          .filter(this::isHardwareAddress)
                          .collect(Collectors.toList());
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
            return nonNull(networkInterface.getHardwareAddress()) &&
                   !networkInterface.toString().toLowerCase().contains("virtual");

        } catch (SocketException e) {
            throw new ConnectionTypeException(e.getMessage());
        }
    }

    private boolean isEthernetConnection(NetworkInterface networkInterface) {

        return Stream.of(networkInterface)
                     .filter(Objects::nonNull)
                     .map(NetworkInterface::getName)
                     .filter(StringUtils::isNotEmpty)
                     .anyMatch(this::isConnectionExist);
    }

    private boolean isConnectionExist(String connectionName) {
        return ETHERNET_IDENTIFIERS.stream().anyMatch(prefix -> connectionName.contains(prefix));
    }

    private String getBrowser() {
        return "CHROME";
    }

    private SnapshotMetadata createSnapshotMetadata(String webSiteDownloadInfoSnapshot, String s3Path) {
        String md5 = convertToMD5(new File(webSiteDownloadInfoSnapshot));
        return new SnapshotMetadata(md5, s3Path);
    }

    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    static double calcBytesToMegabytes(double bytes) {
        return bytes / Math.pow(2, 20); // Transform to Mbps
    }

    public double getJvmUsedMemory() {
        return calcBytesToMegabytes(Runtime.getRuntime().totalMemory());
    }

    public double getJvmMaxMemory() {
        return calcBytesToMegabytes(Runtime.getRuntime().maxMemory());
    }

    public double getJvmCpuLoad() throws IOException {
        MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();

        OperatingSystemMXBean osMBean = ManagementFactory.newPlatformMXBeanProxy(
                mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
        return  osMBean.getProcessCpuLoad() < 0 ? 0 : osMBean.getProcessCpuLoad() * 100.0; // cpu usage in percents
    }

}
