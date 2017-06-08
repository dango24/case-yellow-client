package utils;

import app.utils.AppBootUtils;
import app.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dango on 6/3/17.
 */
public class AppBootUtilsTest {

    @Test
    public void bootAppWithArgs() throws Exception {
        Map<String, String> actualArgsMap;
        Map<String, String> expectedArgsMap = new HashMap<>();
        String[] args = {"-DlogFilePath=/home/dango/sandbox/my_personal_projects/case-yellow-client/logs/logging.log",
                          "-DanotherKey=anotherValue",
                           "notToBeIncludeInArgaMap=Basa"};

        expectedArgsMap.put("logFilePath", "/home/dango/sandbox/my_personal_projects/case-yellow-client/logs/logging.log");
        expectedArgsMap.put("anotherKey", "anotherValue");

        Method buildArgsKeyValueParisMethod = AppBootUtils.class.getDeclaredMethod("buildArgsKeyValueParis", String[].class);
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
                tmpLogFile = new File(System.getProperty("java.io.tmpdir"), Utils.generateUniqueID());
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
        Method createDefaultLoggingFileMethod = AppBootUtils.class.getDeclaredMethod("createDefaultLoggingFile");
        createDefaultLoggingFileMethod.setAccessible(true);
        createDefaultLoggingFileMethod.invoke(null);
    }
}