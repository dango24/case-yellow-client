package caseyellow.client.presentation;


import caseyellow.client.domain.logger.services.CYLogger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static caseyellow.client.common.FileUtils.getFileFromResources;
import static com.sun.jna.Platform.isWindows;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class ConnectionDetailsFormImpl extends JFrame implements ConnectionDetailsForm {

    private static CYLogger logger = new CYLogger(ConnectionDetailsFormImpl.class);

    private JButton saveButton;
    private JButton cancelButton;
    private JComboBox infrastructureCombo;
    private JComboBox speedCombo;
    private MainFrame mainFrame;

    public ConnectionDetailsFormImpl(MainFrame mainFrame) {
        super("Service");
        this.mainFrame = mainFrame;

        setIcon();
        init();
    }

    private void setIcon() {
        try {
            String pathToFileOnDisk = getFileFromResources(new File("bin", "icons"), "icon/connection_icon.png").getAbsolutePath();
            ImageIcon img = new ImageIcon(pathToFileOnDisk);
            this.setIconImage(img.getImage());
        } catch (IOException e) {
            logger.error("Failed to set logger to connection details form");
        }
    }

    private void init() {
        setSize();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        this.add(panel);
        placeComponents(panel);
        this.setLocationRelativeTo(null);
    }

    private void setSize() {
        if (isWindows()) {
            this.setSize(290, 170);
        } else {
            this.setSize(290, 130);
        }
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel infrastructureLabel = new JLabel("Infrastructure");

        if (isWindows()) {
            infrastructureLabel.setBounds(10, 10, 80, 25);
        } else {
            infrastructureLabel.setBounds(10, 10, 100, 25);
        }

        panel.add(infrastructureLabel);

        infrastructureCombo = new JComboBox();
        infrastructureCombo.insertItemAt("Choose", 0);
        infrastructureCombo.setSelectedIndex(0);
        infrastructureCombo.setMaximumRowCount(5);

        if (isWindows()) {
            infrastructureCombo.setBounds(100, 10, 160, 25);
        } else {
            infrastructureCombo.setBounds(120, 10, 160, 25);
        }

        panel.add(infrastructureCombo);


        JLabel speedLabel= new JLabel("Speed");


        speedLabel.setBounds(10, 40, 80, 25);
        panel.add(speedLabel);

        speedCombo = new JComboBox();
        speedCombo.insertItemAt("Choose", 0);
        speedCombo.setSelectedIndex(0);
        speedCombo.setMaximumRowCount(5);

        if (isWindows()) {
            speedCombo.setBounds(100, 40, 160, 25);
        } else {
            speedCombo.setBounds(120, 40, 160, 25);
        }

        panel.add(speedCombo);

        saveButton = new JButton("Save");
        saveButton.addActionListener(l -> saveCommand());

        if (isWindows()) {
            saveButton.setBounds(10, 80, 80, 25);
        } else {
            saveButton.setBounds(30, 80, 80, 25);
        }

        panel.add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener( l -> cancelCommand());

        if (isWindows()) {
            cancelButton.setBounds(180, 80, 80, 25);
        } else {
            cancelButton.setBounds(160, 80, 90, 25);
        }

        panel.add(cancelButton);
    }

    private void saveCommand() {
        if (validateInput()) {
            saveUserDetails();
        }
    }

    private boolean validateInput() {
        int infraIndex = infrastructureCombo.getSelectedIndex();
        int speedIndex = speedCombo.getSelectedIndex();

        if (infraIndex == 0) {
            JOptionPane.showMessageDialog(null, "You must choose infrastructure!", "",  JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (speedIndex == 0) {
            JOptionPane.showMessageDialog(null, "You must choose speed!", "",  JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    private void saveUserDetails() {
        String infa = String.valueOf(infrastructureCombo.getSelectedItem());
        String speed = String.valueOf(speedCombo.getSelectedItem());
        String message = String.format("Infrastructure: %s\nSpeed: %s", infa, speed);
        Object[] options = {"Confirm", "Change"};

        int result = JOptionPane.showOptionDialog(null, message, "Connection Details",
                                                  YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                                  options, options[0]);

        if (result == 0) {
            mainFrame.saveConnectionDetails(infa, speed);
        }
    }

    private void cancelCommand() {
        JOptionPane.showMessageDialog(null, "Bye Bye", "",  JOptionPane.INFORMATION_MESSAGE);
        System.exit(1);
    }

    @Override
    public void view(Map<String, List<String>> connectionDetails) {

        connectionDetails.get("infrastructure")
                         .stream()
                         .sorted()
                         .forEach(infrastructureCombo::addItem);

        infrastructureCombo.setSelectedIndex(0);

        connectionDetails.get("speed")
                         .stream()
                         .map(speed -> speed + " Mbps")
                         .forEach(speedCombo::addItem);

        speedCombo.setSelectedIndex(0);

        this.setVisible(true);
    }

    @Override
    public void close() {
        setVisible(false);
    }
}