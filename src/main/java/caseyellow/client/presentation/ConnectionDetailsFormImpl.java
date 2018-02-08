package caseyellow.client.presentation;


import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static caseyellow.client.common.Utils.getTempFileFromResources;

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
        super("Service");
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

        infrastructureCombo = new JComboBox(connectionDetails.get("infrastructure").toArray());
        infrastructureCombo.setMaximumRowCount(5);
        infrastructureCombo.setBounds(100, 10, 160, 25);
        panel.add(infrastructureCombo);

        JLabel ispLabel= new JLabel("ISP");
        ispLabel.setBounds(10, 40, 80, 25);
        panel.add(ispLabel);

        ispCombo = new JComboBox(connectionDetails.get("isp").toArray());
        ispCombo.setMaximumRowCount(5);
        ispCombo.setBounds(100, 40, 160, 25);
        panel.add(ispCombo);

        JLabel speedLabel= new JLabel("Speed");
        speedLabel.setBounds(10, 70, 80, 25);
        panel.add(speedLabel);

        speedCombo = new JComboBox(connectionDetails.get("speed").toArray());
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
        JOptionPane.showMessageDialog(null, "Welcome");
    }

    private void cancelCommand() {
        JOptionPane.showMessageDialog(null, "Bye Bye");
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