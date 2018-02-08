package caseyellow.client.domain.file.model;

public class FileDownloadProperties {

    private int size;
    private String url;
    private String md5;
    private String identifier;

    public FileDownloadProperties() {
    }

    public FileDownloadProperties(String identifier, String url) {
        this(identifier, url, 0, null);
    }

    public FileDownloadProperties(String identifier, String url, int size, String md5) {
        this.url = url;
        this.size = size;
        this.md5 = md5;
        this.identifier = identifier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
