package caseyellow.client.presistance;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by dango on 6/27/17.
 */
@Component
@ConfigurationProperties
public class UrlRequestSchema {

   private String nextUrls;
   private String nextSpeedTestWebSite;

   public UrlRequestSchema() {}

   public String getNextUrls() {
      return nextUrls;
   }

   public void setNextUrls(String nextUrls) {
      this.nextUrls = nextUrls;
   }

   public String getNextSpeedTestWebSite() {
      return nextSpeedTestWebSite;
   }

   public void setNextSpeedTestWebSite(String nextSpeedTestWebSite) {
      this.nextSpeedTestWebSite = nextSpeedTestWebSite;
   }
}
