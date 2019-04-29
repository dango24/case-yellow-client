package caseyellow.client.domain.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dan on 24/10/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Test {

    private String testID;
    private String clientVersion;
    private String isp;
    private String computerIdentifier;
    private long startTime;
    private long endTime;
    private boolean classicTest;
    private SystemInfo systemInfo;
    private String speedTestWebsiteIdentifier;
    private List<ComparisonInfo> comparisonInfoTests;

    private Test(TestBuilder testBuilder) {
        this.testID = testBuilder.testID;
        this.isp = testBuilder.isp;
        this.classicTest = testBuilder.classicTest;
        this.systemInfo = testBuilder.systemInfo;
        this.speedTestWebsiteIdentifier = testBuilder.speedTestWebsite;
        this.comparisonInfoTests = testBuilder.comparisonInfoTests;
    }

    // TestBuilder Helper
    public static class TestBuilder {

        // Fields
        private String testID;
        private String isp;
        private SystemInfo systemInfo;
        private String speedTestWebsite;
        private boolean classicTest;
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

        public TestBuilder addISP(String isp) {
            this.isp = isp;
            return this;
        }

        public TestBuilder addClassicTest(boolean classicTest) {
            this.classicTest = classicTest;
            return this;
        }

        public Test build() {
            return new Test(this);
        }

    }
}
