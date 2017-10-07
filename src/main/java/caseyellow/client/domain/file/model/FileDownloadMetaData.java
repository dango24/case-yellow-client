package caseyellow.client.domain.file.model;

public class FileDownloadMetaData {

    private String fileName;
    private String fileURL;

    public FileDownloadMetaData() {
    }

    public FileDownloadMetaData(String fileName, String fileURL) {
        this.fileName = fileName;
        this.fileURL = fileURL;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
}
