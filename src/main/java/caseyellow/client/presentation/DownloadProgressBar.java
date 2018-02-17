package caseyellow.client.presentation;

public interface DownloadProgressBar {
    void startDownloading(String fileName);
    void stopDownloading();
    void showFileDownloadState(double currentFileSize, double fullFileSize);
}
