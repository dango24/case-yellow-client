package caseyellow.client.domain.website.service;

import caseyellow.client.domain.website.model.SpeedTestMetaData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
public class SpeedTestWebSiteFactory {

    private final String SPEED_TEST_METADATA_LOCATION = "/speed_test_meta_data.json";

    private Map<String, SpeedTestMetaData> speedTestDTOMap;

    @PostConstruct
    private void init() throws URISyntaxException, IOException {
        String speedTestMetaData = IOUtils.toString(SpeedTestWebSiteFactory.class.getResourceAsStream(SPEED_TEST_METADATA_LOCATION), Charset.forName("UTF-8"));
        SpeedTestMetaDataWrapper speedTestMetaDataWrapper = new ObjectMapper().readValue(speedTestMetaData, SpeedTestMetaDataWrapper.class);

        speedTestDTOMap = speedTestMetaDataWrapper.getSpeedTestMetaData()
                                                  .stream()
                                                  .collect(toMap(SpeedTestMetaData::getIdentifier, Function.identity()));
    }

    public SpeedTestMetaData getSpeedTestWebSiteFromIdentifier(String identifier) {
        return speedTestDTOMap.get(identifier);
    }

    private static class SpeedTestMetaDataWrapper {

        private List<SpeedTestMetaData> speedTestMetaData;

        public SpeedTestMetaDataWrapper() {
        }

        public List<SpeedTestMetaData> getSpeedTestMetaData() {
            return speedTestMetaData;
        }

        public void setSpeedTestMetaData(List<SpeedTestMetaData> speedTestMetaData) {
            this.speedTestMetaData = speedTestMetaData;
        }
    }
}
