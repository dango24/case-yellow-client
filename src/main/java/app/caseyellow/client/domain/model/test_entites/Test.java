package app.caseyellow.client.domain.model.test_entites;

import app.caseyellow.client.domain.model.SystemInfo;
import app.caseyellow.client.domain.model.web_site_entites.SpeedTestWebSite;

import java.util.List;

/**
 * Created by Dan on 24/10/2016.
 */
public class Test {

    // Fields
    private String testID;
    private SystemInfo systemInfo;
    private SpeedTestWebSite speedTestWebsite;
    private List<ComparisonInfo> comparisonInfoTests;

    // Constructor
    private Test(TestBuilder testBuilder) {
        this.testID = testBuilder.testID;
        this.systemInfo = testBuilder.systemInfo;
        this.speedTestWebsite = testBuilder.speedTestWebsite;
        this.comparisonInfoTests = testBuilder.comparisonInfoTests;
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

    // TestBuilder Helper
    public static class TestBuilder {

        // Fields
        private String               testID;
        private SystemInfo systemInfo;
        private SpeedTestWebSite     speedTestWebsite;
        private List<ComparisonInfo> comparisonInfoTests;

        public TestBuilder(String testID) {
            this.testID = testID;
        }

        public TestBuilder addSystemInfo(SystemInfo systemInfo) {
            this.systemInfo = systemInfo;
            return this;
        }

        public TestBuilder addSpeedTestWebsite(SpeedTestWebSite speedTestWebsite) {
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
