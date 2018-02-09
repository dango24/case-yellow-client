package caseyellow.client.presentation;


import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static caseyellow.client.common.Utils.getTempFileFromResources;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class ConnectionDetailsFormImpl extends JFrame implements ConnectionDetailsForm {

    private Logger logger = Logger.getLogger(ConnectionDetailsFormImpl.class);

    private JButton saveButton;
    private JButton cancelButton;
    private JComboBox infrastructureCombo;
    private JComboBox ispCombo;
    private JComboBox speedCombo;
    private MainFrame mainFrame;
    private Map<String, List<String>> connectionDetails;

    public ConnectionDetailsFormImpl(MainFrame mainFrame, Map<String, List<String>> connectionDetails) {
        super("Your Service");
        this.mainFrame = mainFrame;
        this.connectionDetails = connectionDetails;

        setIcon();
        init();
        view();
    }

    private void setIcon() {
        try {
            String pathToFileOnDisk = getTempFileFromResources("icon/connection_icon.png").getAbsolutePath();
            ImageIcon img = new ImageIcon(pathToFileOnDisk);
            this.setIconImage(img.getImage());
        } catch (IOException e) {
            logger.error("Failed to set logger to connection details form");
        }
    }

    private void init() {
        this.setSize(310, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        this.add(panel);
        placeComponents(panel);
        this.setLocationRelativeTo(null);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel infrastructureLabel = new JLabel("Infrastructure");
        infrastructureLabel.setBounds(10, 10, 80, 25);
        panel.add(infrastructureLabel);

        infrastructureCombo = new JComboBox(connectionDetails.get("infrastructure").stream().sorted().toArray());
        infrastructureCombo.insertItemAt("Choose", 0);
        infrastructureCombo.setSelectedIndex(0);
        infrastructureCombo.setMaximumRowCount(5);
        infrastructureCombo.setBounds(100, 10, 160, 25);
        panel.add(infrastructureCombo);

        JLabel ispLabel= new JLabel("ISP");
        ispLabel.setBounds(10, 40, 80, 25);
        panel.add(ispLabel);

        ispCombo = new JComboBox(connectionDetails.get("isp").stream().sorted().toArray());
        ispCombo.insertItemAt("Choose", 0);
        ispCombo.setSelectedIndex(0);
        ispCombo.setMaximumRowCount(5);
        ispCombo.setBounds(100, 40, 160, 25);
        panel.add(ispCombo);

        JLabel speedLabel= new JLabel("Speed");
        speedLabel.setBounds(10, 70, 80, 25);
        panel.add(speedLabel);

        speedCombo = new JComboBox(connectionDetails.get("speed").stream().map(speed -> speed + " Mbps").toArray());
        speedCombo.insertItemAt("Choose", 0);
        speedCombo.setSelectedIndex(0);
        speedCombo.setMaximumRowCount(5);
        speedCombo.setBounds(100, 70, 160, 25);
        panel.add(speedCombo);

        saveButton = new JButton("Save");
        saveButton.addActionListener(l -> saveCommand());
        saveButton.setBounds(10, 110, 80, 25);
        panel.add(saveButton);

        cancelButton = new JButton("cancel");
        cancelButton.addActionListener( l -> cancelCommand());
        cancelButton.setBounds(180, 110, 80, 25);
        panel.add(cancelButton);
    }

    private void saveCommand() {
        if (validateInput()) {
            saveUserDetails();
        }
    }

    private boolean validateInput() {
        int infraIndex = infrastructureCombo.getSelectedIndex();
        int ispIndex = ispCombo.getSelectedIndex();
        int speedIndex = speedCombo.getSelectedIndex();

        if (infraIndex == 0) {
            JOptionPane.showMessageDialog(null, "You must choose infrastructure!", "",  JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (ispIndex == 0) {
            JOptionPane.showMessageDialog(null, "You must choose ISP!", "",  JOptionPane.INFORMATION_MESSAGE);
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
        String isp = String.valueOf(ispCombo.getSelectedItem());
        String speed = String.valueOf(speedCombo.getSelectedItem());
        String message = String.format("Infrastructure: %s\nISP: %s\nSpeed: %s", infa, isp, speed);
        Object[] options = {"Confirm", "Change"};

        int result = JOptionPane.showOptionDialog(null, message, "Connection Details",
                                                  YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                                  options, options[0]);

        if (result == 0) {
            SwingUtilities.invokeLater( () -> this.dispose());
            SwingUtilities.invokeLater( () -> mainFrame.saveConnectionDetails(infa, isp, speed));
        }
    }

    private void cancelCommand() {
        JOptionPane.showMessageDialog(null, "Bye Bye", "",  JOptionPane.INFORMATION_MESSAGE);
        System.exit(1);
    }

    @Override
    public void view() {
        this.setVisible(true);
    }

    @Override
    public void close() {
        setVisible(false);
        dispose();
    }
}