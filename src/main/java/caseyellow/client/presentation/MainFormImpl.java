package caseyellow.client.presentation;

import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.domain.test.commands.StartProducingTestsCommand;
import caseyellow.client.domain.test.commands.StopProducingTestsCommand;
import caseyellow.client.exceptions.LoginException;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.gateway.services.GatewayService;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static caseyellow.client.common.Messages.churchillSpeech;

/**
 * Created by Dan on 7/7/2017.
 */
public class MainFormImpl implements MessagesService, MainFrame {

    private final String dateFormatter = "HH:mm:ss";
    private Logger logger = Logger.getLogger(MainFormImpl.class);

    // Constants
    private static final String LOADING_APP_MESSAGE = "loading app...  Please wait";

    // Fields
    private JFrame mainFrame;
    private LoginForm loginForm;
    private JButton startButton;
    private JButton stopButton;
    private JEditorPane editorPane;
    private JScrollPane editorScrollPane;
    private StartProducingTestsCommand startProducingTestsCommand;
    private StopProducingTestsCommand stopProducingTestsCommand;
    private GatewayService gatewayService;

    // Constructor
    public MainFormImpl() {
        mainFrame = new JFrame("Case Yellow");
        buildComponents();
    }

    private void buildComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        buildButtons();
        buildEditorScrollPane();

        panel.add(startButton);
        panel.add(stopButton);
        panel.add(editorScrollPane);
        mainFrame.add(panel);
        mainFrame.setSize(350, 260);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginForm = new LoginFormImpl(this);
    }

    private void buildEditorScrollPane() {
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        showMessage(LOADING_APP_MESSAGE);
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
        logger.info("Message show to the user: " + message);
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
        showMessage(churchillSpeech());
    }

    public void disableApp() {
        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        stopProducingTests();
    }

    private void startProducingTests() {
        logger.info("Start button pressed");
        showMessage("Start producing tests");
        SwingUtilities.invokeLater(() -> startButton.setEnabled(false));
        SwingUtilities.invokeLater(() -> stopButton.setEnabled(true));
        SwingUtilities.invokeLater(startProducingTestsCommand::executeStartProducingTestsCommand);
    }

    private void stopProducingTests() {
        logger.info("Stop button pressed");
        showMessage("App halt, stop production tests");
        SwingUtilities.invokeLater(() -> startButton.setEnabled(true));
        SwingUtilities.invokeLater(() -> stopButton.setEnabled(false));
        SwingUtilities.invokeLater(stopProducingTestsCommand::executeStopProducingCommand);
    }

    public void terminate() {
        mainFrame.dispose();
    }


    public void setStartProducingTestsCommand(StartProducingTestsCommand startProducingTestsCommand) {
        this.startProducingTestsCommand = startProducingTestsCommand;
    }

    public void setStopProducingTestsCommand(StopProducingTestsCommand stopProducingTestsCommand) {
        this.stopProducingTestsCommand = stopProducingTestsCommand;
    }

    public void setGatewayService(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @Override
    public void login(String userName, String password) throws IOException, LoginException {
        boolean loginSucceed = gatewayService.login(new AccountCredentials(userName, password));

        if (loginSucceed) {
            JOptionPane.showMessageDialog(null, "Login succeed");
            SwingUtilities.invokeLater(() -> loginForm.close());
            SwingUtilities.invokeLater(() -> startButton.setEnabled(true));
        }
    }
}
