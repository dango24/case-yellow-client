package caseyellow.client.domain.system;

import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.test.model.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dan on 6/20/2017.
 */
public interface SystemService extends URLToFileService {

    String LAN_CONNECTION = "LAN";
    String WIFI_CONNECTION = "Wifi";
    String UNKNOWN_CONNECTION = "Unknown";

    String getISP();
    SystemInfo getSystemInfo();
    String convertToMD5(File file);
    double getJvmUsedMemory();
    double getJvmMaxMemory();
    double getJvmCpuLoad() throws IOException;
    void saveSnapshotHashToDisk(Test test);
}
