package caseyellow.client.infrastructre;

import caseyellow.client.domain.services.interfaces.SystemService;
import caseyellow.client.infrastructre.AppBootHelper;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static caseyellow.client.common.Utils.generateUniqueID;
import static org.junit.Assert.*;

/**
 * Created by dango on 6/3/17.
 */
public class AppBootHelperTest {

    @Autowired
    private SystemService systemService;

    @Test
    public void bootAppWithArgs() throws Exception {
        Map<String, String> actualArgsMap;
        Map<String, String> expectedArgsMap = new HashMap<>();
        String[] args = {"-DlogFilePath=/home/dango/sandbox/my_personal_projects/case-yellow-client/logs/logging.log",
                          "-DanotherKey=anotherValue",
                           "notToBeIncludeInArgaMap=Basa"};

        expectedArgsMap.put("logFilePath", "/home/dango/sandbox/my_personal_projects/case-yellow-client/logs/logging.log");
        expectedArgsMap.put("anotherKey", "anotherValue");

        Method buildArgsKeyValueParisMethod = AppBootHelper.class.getDeclaredMethod("buildArgsKeyValueParis", String[].class);
        buildArgsKeyValueParisMethod.setAccessible(true);

        actualArgsMap = (Map<String, String>)buildArgsKeyValueParisMethod.invoke(null, new Object[] {args});

        assertEquals(expectedArgsMap, actualArgsMap);
    }

    @Test
    public void createLogFileLocally() throws Exception {
        File tmpLogFile = null;
        File logDir = new File(System.getProperty("user.home"), "case-yellow-logs");

        try {

            if (logDir.exists()) {
                tmpLogFile = new File(System.getProperty("java.io.tmpdir"), generateUniqueID());
                FileUtils.copyDirectory(logDir, tmpLogFile);
                FileUtils.deleteDirectory(logDir);
            }

            createDefaultLoggingDir();
            assertTrue(logDir.exists());

        } finally {

            if (tmpLogFile != null) { // indicates tmpLogFile created
                FileUtils.copyDirectory(tmpLogFile, logDir);
                FileUtils.deleteDirectory(tmpLogFile);
            }
        }
    }

    private void createDefaultLoggingDir() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method createDefaultLoggingFileMethod = AppBootHelper.class.getDeclaredMethod("createDefaultLoggingFile");
        createDefaultLoggingFileMethod.setAccessible(true);
        createDefaultLoggingFileMethod.invoke(null);
    }
}