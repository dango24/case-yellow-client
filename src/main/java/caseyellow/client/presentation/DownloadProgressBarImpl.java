package caseyellow.client.presentation;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

import static caseyellow.client.common.Utils.getTempFileFromResources;

public class DownloadProgressBarImpl implements DownloadProgressBar {

    private static final String TITLE_MESSAGE = "Downloading file: %s";

    private JFrame frame;
    private JProgressBar progressBar;

    public DownloadProgressBarImpl() throws IOException {
        init();
        setIcon();
    }

    private void init() {
        frame = new JFrame(String.format(TITLE_MESSAGE, ""));
        frame.setLocationRelativeTo(null);
        frame.setAutoRequestFocus(false);
        Container content = frame.getContentPane();
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Downloading...");
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);
        frame.setSize(420, 100);
        frame.setVisible(false);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
    }

    private void setIcon() throws IOException {
        String pathToFileOnDisk = getTempFileFromResources("icon/download_icon.png").getAbsolutePath();
        ImageIcon img = new ImageIcon(pathToFileOnDisk);
        frame.setIconImage(img.getImage());
    }

    @Override
    public void startDownloading(String fileName) {
        frame.setTitle(String.format(TITLE_MESSAGE, fileName));
        frame.setVisible(true);
    }

    @Override
    public void stopDownloading() {
        frame.setVisible(false);
    }

    @Override
    public void showFileDownloadState(double currentFileSize, double fullFileSize) {
        int percent = (int)((currentFileSize / fullFileSize) * 100);
        progressBar.setValue(percent);
    }
}
