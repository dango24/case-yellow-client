package caseyellow.client.domain.test.model;

import java.util.List;

/**
 * Created by Dan on 24/10/2016.
 */
public class Test {

    private String testID;
    private String clientVersion;
    private long startTime;
    private long endTime;
    private SystemInfo systemInfo;
    private String speedTestWebsiteIdentifier;
    private List<ComparisonInfo> comparisonInfoTests;

    public Test() {
    }

    private Test(TestBuilder testBuilder) {
        this.testID = testBuilder.testID;
        this.systemInfo = testBuilder.systemInfo;
        this.speedTestWebsiteIdentifier = testBuilder.speedTestWebsite;
        this.comparisonInfoTests = testBuilder.comparisonInfoTests;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getSpeedTestWebsiteIdentifier() {
        return speedTestWebsiteIdentifier;
    }

    public void setSpeedTestWebsiteIdentifier(String speedTestWebsiteIdentifier) {
        this.speedTestWebsiteIdentifier = speedTestWebsiteIdentifier;
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

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    // TestBuilder Helper
    public static class TestBuilder {

        // Fields
        private String testID;
        private SystemInfo systemInfo;
        private String speedTestWebsite;
        private List<ComparisonInfo> comparisonInfoTests;

        public TestBuilder(String testID) {
            this.testID = testID;
        }

        public TestBuilder addSystemInfo(SystemInfo systemInfo) {
            this.systemInfo = systemInfo;
            return this;
        }

        public TestBuilder addSpeedTestWebsite(String speedTestWebsite) {
            this.speedTestWebsite = speedTestWebsite;
            return this;
        }

        public TestBuilder addComparisonInfoTests(List<ComparisonInfo> comparisonInfoTests) {
            this.comparisonInfoTests = comparisonInfoTests;
            return this;
        }

        public Test build() {
            return new Test(this);
        }
    }
}
