package caseyellow.client.presentation;

import caseyellow.client.presentation.interfaces.DomainInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dan on 7/7/2017.
 */
@org.springframework.stereotype.Component
public class MainForm {

    private JFrame frame;
    private JPanel panel;

    private DomainInteractor domainInteractor;

    @Autowired
    public MainForm(DomainInteractor domainInteractor) {
        this.domainInteractor = domainInteractor;
        frame = new JFrame("Case Yellow");
        panel = new JPanel();
        buildComponents();
    }

    private void buildComponents() {
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("This is a label!");
        JButton startButton = new JButton();
        JButton stopButton = new JButton();
        startButton.setText("Start Producing Tests");
        stopButton.setText("Stop");
        startButton.addActionListener( (l) -> startProducingTests());
        stopButton.addActionListener( (l) -> stopProducingTests());
       // panel.add(label);
        panel.add(startButton);
        panel.add(stopButton);
        frame.add(panel);
        frame.setSize(350, 300);
       // frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setUndecorated(true);
    }

    public void view() {
        frame.setVisible(true);
    }

    private void startProducingTests() {
        SwingUtilities.invokeLater(domainInteractor::startProducingTests);
    }

    private void stopProducingTests() {
        SwingUtilities.invokeLater(domainInteractor::stopProducingTests);
    }
}

