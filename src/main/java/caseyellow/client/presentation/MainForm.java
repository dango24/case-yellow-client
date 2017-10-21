package caseyellow.client.presentation;

import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.domain.test.commands.StartProducingTestsCommand;
import caseyellow.client.domain.test.commands.StopProducingTestsCommand;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static caseyellow.client.common.Messages.churchillSpeech;

/**
 * Created by Dan on 7/7/2017.
 */
public class MainForm implements MessagesService {

    private final String dateFormatter = "HH:mm:ss";
    private Logger logger = Logger.getLogger(MainForm.class);

    // Constants
    private static final String LOADING_APP_MESSAGE = "loading app...  Please wait";

    // Fields
    private JFrame frame;
    private JButton startButton;
    private JButton stopButton;
    private JEditorPane editorPane;
    private JScrollPane editorScrollPane;
    private StartProducingTestsCommand startProducingTestsCommand;
    private StopProducingTestsCommand stopProducingTestsCommand;

    // Constructor
    public MainForm() {
        frame = new JFrame("Case Yellow");
        buildComponents();
    }

    // Setters

    @Autowired
    public void setStartProducingTestsCommand(StartProducingTestsCommand startProducingTestsCommand) {
        this.startProducingTestsCommand = startProducingTestsCommand;
    }

    @Autowired
    public void setStopProducingTestsCommand(StopProducingTestsCommand stopProducingTestsCommand) {
        this.stopProducingTestsCommand = stopProducingTestsCommand;
    }

    // Methods

    private void buildComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        buildButtons();
        buildEditorScrollPane();

        panel.add(startButton);
        panel.add(stopButton);
        panel.add(editorScrollPane);
        frame.add(panel);
        frame.setSize(350, 260);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public void enableApp() {
        startButton.setEnabled(true);
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
        SwingUtilities.invokeLater(startProducingTestsCommand::executeStartProducingTestsCommand);
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    private void stopProducingTests() {
        logger.info("Stop button pressed");
        showMessage("App halt, stop production tests");
        SwingUtilities.invokeLater(stopProducingTestsCommand::executeStopProducingCommand);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    public void terminate() {
        frame.dispose();
    }
}
