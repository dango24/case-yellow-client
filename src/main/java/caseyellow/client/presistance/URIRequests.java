package caseyellow.client.presistance;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by dango on 6/27/17.
 */
@Component
@ConfigurationProperties
public class URIRequests {

   private String saveTestRequest;
   private String nextUrlsRequest;
   private String nextSpeedTestWebSiteRequest;

   public URIRequests() {}

   public String getNextUrlsRequest() {
      return nextUrlsRequest;
   }

   public void setNextUrlsRequest(String nextUrlsRequest) {
      this.nextUrlsRequest = nextUrlsRequest;
   }

   public String getNextSpeedTestWebSiteRequest() {
      return nextSpeedTestWebSiteRequest;
   }

   public void setNextSpeedTestWebSiteRequest(String nextSpeedTestWebSiteRequest) {
      this.nextSpeedTestWebSiteRequest = nextSpeedTestWebSiteRequest;
   }

   public String getSaveTestRequest() {
      return saveTestRequest;
   }

   public void setSaveTestRequest(String saveTestRequest) {
      this.saveTestRequest = saveTestRequest;
   }
}
