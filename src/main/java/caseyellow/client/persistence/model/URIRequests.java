package caseyellow.client.persistence.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by dango on 6/27/17.
 */
@Component
@ConfigurationProperties
public class URIRequests {

   private String saveTestCommand;
   private String nextUrlsQuery;
   private String nextSpeedTestWebSiteQuery;

   public URIRequests() {}

   public String getNextUrlsQuery() {
      return nextUrlsQuery;
   }

   public void setNextUrlsQuery(String nextUrlsQuery) {
      this.nextUrlsQuery = nextUrlsQuery;
   }

   public String getNextSpeedTestWebSiteQuery() {
      return nextSpeedTestWebSiteQuery;
   }

   public void setNextSpeedTestWebSiteQuery(String nextSpeedTestWebSiteQuery) {
      this.nextSpeedTestWebSiteQuery = nextSpeedTestWebSiteQuery;
   }

   public String getSaveTestCommand() {
      return saveTestCommand;
   }

   public void setSaveTestCommand(String saveTestCommand) {
      this.saveTestCommand = saveTestCommand;
   }
}
