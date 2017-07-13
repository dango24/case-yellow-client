package caseyellow.client.presentation;

import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.domain.test.commands.StartProducingTestsCommand;
import caseyellow.client.domain.test.commands.StopProducingTestsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

import static caseyellow.client.common.Messages.churchillSpeech;

/**
 * Created by Dan on 7/7/2017.
 */
@Component
public class MainForm implements MessagesService {

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
        editorPane.setText(churchillSpeech());
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
        startButton.addActionListener( (l) -> startProducingTests());
        stopButton.addActionListener( (l) -> stopProducingTests());
    }

    @Override
    public void showMessage(String message) {
        editorPane.setText(message);
    }

    public void view() {
        frame.setVisible(true);
    }

    private void startProducingTests() {
        showMessage("Start producing tests");
        SwingUtilities.invokeLater(startProducingTestsCommand::executeStartProducingTestsCommand);
    }

    private void stopProducingTests() {
        showMessage("App stopped, After the current test will be finish there will be no more test production");
        SwingUtilities.invokeLater(stopProducingTestsCommand::executeStopProducingCommand);
    }
}

