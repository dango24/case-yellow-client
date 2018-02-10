package caseyellow.client.presentation;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.Closeable;
import java.io.IOException;

import static caseyellow.client.common.Utils.getTempFileFromResources;

public class DownloadProgressBar implements Closeable {

    private static final String TITLE_MESSAGE = "Downloading file: %s";

    private JFrame frame;
    private JProgressBar progressBar;

    public DownloadProgressBar() throws IOException {
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
    }

    private void setIcon() throws IOException {
        String pathToFileOnDisk = getTempFileFromResources("icon/download_icon.png").getAbsolutePath();
        ImageIcon img = new ImageIcon(pathToFileOnDisk);
        frame.setIconImage(img.getImage());
    }

    public void showFileDownloadState(String fileName, double currentFileSize, double fullFileSize) {
        frame.setTitle(String.format(TITLE_MESSAGE, fileName));

        if (currentFileSize == fullFileSize) {
            frame.setVisible(false);
        } else {
            frame.setVisible(true);
            int percent = (int)((currentFileSize / fullFileSize) * 100);
            progressBar.setValue(percent);
        }
    }

    @Override
    public void close() throws IOException {
        frame.setVisible(false);
    }
}
