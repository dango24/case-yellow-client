package speed.test.entities;

import speed.test.web.site.SpeedTestWebSite;

import java.util.ArrayList;
import java.util.List;

import static utils.Utils.generateUniqueID;
import static utils.Utils.getConnection;

/**
 * Created by Dan on 24/10/2016.
 */
public class Test {

    // Fields
    private int                  numOfUrlForTest;
    private String               testID;
    private SystemInfo           systemInfo;
    private SpeedTestWebSite     speedTestWebsite;
    private List<String>         completedUrls;
    private List<String>         failedUrls;
    private List<ComparisonInfo> comparisonInfoTests;

    // Constructor
    public Test(int numOfUrlForTest, SpeedTestWebSite SpeedTestWebsite) {
        this.numOfUrlForTest = numOfUrlForTest;
        this.testID = generateUniqueID();
        this.speedTestWebsite = SpeedTestWebsite;
        this.comparisonInfoTests = new ArrayList<>(numOfUrlForTest);
        this.completedUrls = new ArrayList<>(numOfUrlForTest);
        this.failedUrls = new ArrayList<>();
        this.systemInfo = new SystemInfo(getConnection());
    }

    // Methods

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public SpeedTestWebSite getSpeedTestWebsite() {
        return speedTestWebsite;
    }

    public void setSpeedTestWebsite(SpeedTestWebSite speedTestWebsite) {
        this.speedTestWebsite = speedTestWebsite;
    }

    public List<String> getCompletedUrls() {
        return completedUrls;
    }

    public void setCompletedUrls(List<String> completedUrls) {
        this.completedUrls = completedUrls;
    }

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    public List<ComparisonInfo> getComparisonInfoTests() {
        return comparisonInfoTests;
    }

    public void setComparisonInfoTests(List<ComparisonInfo> speedTests) {
        this.comparisonInfoTests = speedTests;
    }

    public void addComparisonInfo(ComparisonInfo comparisonInfo) {
        comparisonInfoTests.add(comparisonInfo);
    }

    public void addCompletedUrl(String url) {
        completedUrls.add(url);
    }

    public void addFailedUrl(String url) {
        failedUrls.add(url);
    }

    public boolean doneTest() {
        return completedUrls.size() == numOfUrlForTest;
    }

    public List<String> getFailedUrls() {
        return failedUrls;
    }
}
