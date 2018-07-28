package caseyellow.client.domain.message;

public class CmdLineMessageImpl implements MessagesService {

    @Override
    public void testDone() {
        System.out.println("test done!");

    }

    @Override
    public void subTestStart() {
        System.out.println("starting subtest");

    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);

    }

    @Override
    public void showDownloadFileProgress(long currentFileSize, long fullFileSize) {
        System.out.print(String.format("\rdownloading file %%%f", ((double)currentFileSize / (double) fullFileSize) * 100));
    }

    @Override
    public void startDownloadingFile(String fileName) {
        System.out.println(String.format("staring to download: %s", fileName));

    }

    @Override
    public void finishDownloadingFile() {
        System.out.println("file download done!");
    }
}
