package caseyellow.client.domain.analyze.model;

public class Image {

    private String content; // Image In Base64;
    private String md5;

    public Image() {
    }

    public Image(String content) {
        this.content = content;
    }

    public Image(String content, String md5) {
        this.content = content;
        this.md5 = md5;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
