package caseyellow.client.presentation;


import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static caseyellow.client.common.FileUtils.getFileFromResources;
import static com.sun.jna.Platform.isLinux;
import static java.util.stream.Collectors.joining;

public class LoginFormImpl extends JFrame implements LoginForm {

    private JButton loginButton;
    private JButton cancelButton;
    private JTextField userText;
    private JPasswordField passwordText;
    private MainFrame mainFrame;

    public LoginFormImpl(MainFrame mainFrame) throws HeadlessException, IOException {
        super("Login");
        this.mainFrame = mainFrame;
        setIcon();
        init();
    }

    private void setIcon() throws IOException {
        String pathToFileOnDisk = getFileFromResources(new File("bin", "icons"), "icon/login_icon.png").getAbsolutePath();
        ImageIcon img = new ImageIcon(pathToFileOnDisk);
        this.setIconImage(img.getImage());
    }


    private void init() {

        if(isLinux()) {
            this.setSize(290, 140);
        } else {
            this.setSize(300, 170);
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        this.add(panel);
        placeComponents(panel);
        this.setLocationRelativeTo(null);
    }

    private void placeComponents(JPanel panel) {

        panel.setLayout(null);

        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100, 10, 160, 25);
        panel.add(userText);

        JLabel passwordLabel= new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 40, 160, 25);
        panel.add(passwordText);

        loginButton = new JButton("login");
        loginButton.addActionListener( l -> loginCommand());
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        cancelButton = new JButton("cancel");
        cancelButton.addActionListener( l -> cancelCommand());
        cancelButton.setBounds(180, 80, 80, 25);
        panel.add(cancelButton);

//        addSignInLink(panel);

        this.getRootPane().setDefaultButton(loginButton);
    }

    private void addSignInLink(JPanel panel) {
        final String strURL = "http://www.yahoo.com";
        final JLabel label = new JLabel("<html><a href=\" " + strURL + "\">sign in</a></html>");
        label.setBounds(30, 110, 40, 25);
        panel.add(label);

        label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent me) {
                label.setCursor(Cursor.getDefaultCursor());
            }
            public void mouseClicked(MouseEvent me) {
                JOptionPane.showMessageDialog(null, "Here come the heroes");
            }
        });
    }

    private void loginCommand() {
        String userName = userText.getText();
        char[] passwordArray = passwordText.getPassword();
        String password = Stream.of(passwordArray).map(String::valueOf).collect(joining());

        if (validateInput(userName, password)) {
            login(userName, password);
        }
    }

    private void login(String userName, String password) {
        try {
            mainFrame.login(userName, password);

        } catch (Exception e) {
            String errorMessage = StringUtils.isNotEmpty(e.getMessage()) ? ", " + e.getMessage() : "";
            JOptionPane.showMessageDialog(null, "Failed to login" + errorMessage);
        }
    }

    private boolean validateInput(String userName, String password) {
        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "user name is empty");
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "password is empty");
            return false;
        }

        return true;
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
    }
}