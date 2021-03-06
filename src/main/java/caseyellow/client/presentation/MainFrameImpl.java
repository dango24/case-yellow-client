package caseyellow.client.presentation;

import caseyellow.client.domain.logger.services.CYLogger;
import caseyellow.client.domain.logger.services.LoggerUploader;
import caseyellow.client.domain.test.model.ConnectionDetails;
import caseyellow.client.domain.test.service.TestGenerator;
import caseyellow.client.exceptions.LoginException;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.gateway.model.LoginDetails;
import caseyellow.client.sevices.gateway.services.GatewayService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static caseyellow.client.common.FileUtils.getFileFromResources;
import static com.sun.jna.Platform.isLinux;

/**
 * Created by Dan on 7/7/2017.
 */
public class MainFrameImpl implements MainFrame {

    private static CYLogger logger = new CYLogger(MainFrameImpl.class);

    private static final String BOOT_MESSAGE = "Turn on, tune in, drop out";
    private static final String dateFormatter = "HH:mm:ss";
    private static final String LOADING_APP_MESSAGE = "loading app... Please wait";

    private JFrame mainFrame;
    private JButton startButton;
    private JButton stopButton;
    private JEditorPane editorPane;
    private JScrollPane editorScrollPane;
    private TestGenerator testGenerator;
    private LoginForm loginForm;
    private LoggerUploader loggerUploader;
    private DownloadProgressBar downloadProgressBar;
    private ConnectionDetailsForm connectionDetailsForm;
    private GatewayService gatewayService;

    public MainFrameImpl() throws IOException {
        mainFrame = new JFrame("Servile");
        downloadProgressBar = new DownloadProgressBarImpl();
        setIcon();
        buildComponents();
        mainFrame.setLocationRelativeTo(null);
    }

    private void setIcon() throws IOException {
        String pathToFileOnDisk = getFileFromResources(new File("bin", "icons"), "icon/main_icon.png").getAbsolutePath();
        ImageIcon img = new ImageIcon(pathToFileOnDisk);
        mainFrame.setIconImage(img.getImage());
    }

    private void buildComponents() throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        buildButtons();
        buildEditorScrollPane();

        panel.add(startButton);
        panel.add(stopButton);
        panel.add(editorScrollPane);
        mainFrame.add(panel);

        if(isLinux()) {
            mainFrame.setSize(355, 230);
        } else {
            mainFrame.setSize(370, 260);
        }

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginForm = new LoginFormImpl(this);
        connectionDetailsForm = new ConnectionDetailsFormImpl(this);
    }

    private void buildEditorScrollPane() {
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        showMessageToUser(LOADING_APP_MESSAGE);
        editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));
    }

    private void buildButtons() {
        startButton = new JButton();
        stopButton = new JButton();
        startButton.setText("Start Producing Tests");
        stopButton.setText("Stop");
        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        startButton.addActionListener( (l) -> startProducingTests());
        stopButton.addActionListener( (l) -> stopProducingTests());
    }

    @Override
    public void showMessage(String message) {
        message = new SimpleDateFormat(dateFormatter).format(new Date()) + " - " + message;
        showMessageToUser(message);
    }

    private void showMessageToUser(String message) {
        SwingUtilities.invokeLater( () -> editorPane.setText(message));
    }

    public void view() {
        SwingUtilities.invokeLater(() -> mainFrame.setVisible(true));
    }

    public void enableApp() {
        loginForm.view();
        showMessage(BOOT_MESSAGE);
    }

    @Override
    public void disableApp(boolean tokenExpired) {
        stopProducingTests();
        SwingUtilities.invokeLater(() -> startButton.setEnabled(false));
        SwingUtilities.invokeLater(() -> stopButton.setEnabled(false));

        if (tokenExpired) {
            showMessageToUser("Your account expired please login again");
            JOptionPane.showMessageDialog(null, "Your account expired please login again");
            loginForm.view();
        }
    }

    private void startProducingTests() {
        logger.info("Start button pressed");
        showMessage("Start producing tests");
        SwingUtilities.invokeLater(() -> startButton.setEnabled(false));
        SwingUtilities.invokeLater(() -> stopButton.setEnabled(true));
        SwingUtilities.invokeLater(testGenerator::startProducingTests);
    }

    private void stopProducingTests() {
        testDone();
        logger.info("Stop button pressed");
        showMessage("App halt, stop production tests");
        SwingUtilities.invokeLater(() -> startButton.setEnabled(true));
        SwingUtilities.invokeLater(() -> stopButton.setEnabled(false));
        SwingUtilities.invokeLater(() -> downloadProgressBar.stopDownloading());
        SwingUtilities.invokeLater(testGenerator::stopProducingTests);
    }

    public void terminate() {
        mainFrame.dispose();
    }

    public void setLoggerUploader(LoggerUploader loggerUploader) {
        this.loggerUploader = loggerUploader;
    }

    public void setGatewayService(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    public void setTestGenerator(TestGenerator testGenerator) {
        this.testGenerator = testGenerator;
    }

    @Override
    public void login(String userName, String password) throws IOException, LoginException, InterruptedException {
        LoginDetails loginDetails = gatewayService.login(new AccountCredentials(userName, password));

        if (loginDetails.isSucceed()) {

            loggerUploader.uploadLogs();
            testGenerator.updateTestLifeCycle();

            if (loginDetails.isRegistration()) {
                mainFrame.setEnabled(false);
                Map<String, List<String>> connectionDetails = gatewayService.getConnectionDetails();
                loginForm.close();
                connectionDetailsForm.view(connectionDetails);
            } else {
                formInitView();
            }
        }
    }

    @Override
    public void saveConnectionDetails(String infrastructure, String speed) {
        connectionDetailsForm.close();
        mainFrame.setEnabled(true);
        ConnectionDetails connectionDetails = new ConnectionDetails(infrastructure, Integer.valueOf(speed.replaceAll("Mbps", "").trim()));
        gatewayService.saveConnectionDetails(connectionDetails);
        formInitView();
    }

    @Override
    public void formInitView() {
        JOptionPane.showMessageDialog(null, "Justice will be served", "", JOptionPane.INFORMATION_MESSAGE);
        showMessageToUser(BOOT_MESSAGE);
        loginForm.close();
        startButton.setEnabled(true);
    }

    @Override
    public void subTestStart() {
       // Do nothing
    }

    @Override
    public void testDone() {
        showMessage("Test Done");
    }

    @Override
    public void testStopped() {
        logger.warn("Test Stopped");
        showMessage("Test Stopped");
    }

    @Override
    public void startDownloadingFile(String fileName) {
        SwingUtilities.invokeLater(() -> downloadProgressBar.startDownloading(fileName));
    }

    @Override
    public void finishDownloadingFile() {
        SwingUtilities.invokeLater(() -> downloadProgressBar.stopDownloading());
    }

    @Override
    public void showDownloadFileProgress(long currentFileSize, long fullFileSize) {
        SwingUtilities.invokeLater(() -> downloadProgressBar.showFileDownloadState(currentFileSize, fullFileSize));
    }

}
