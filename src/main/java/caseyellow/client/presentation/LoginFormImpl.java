package caseyellow.client.presentation;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class LoginFormImpl extends JFrame implements LoginForm {

    private JButton loginButton;
    private JButton cancelButton;
    private JTextField userText;
    private JPasswordField passwordText;
    private MainFrame mainFrame;

    public LoginFormImpl(MainFrame mainFrame) throws HeadlessException {
        super("Login");
        this.mainFrame = mainFrame;
        init();
    }

    private void init() {
        this.setSize(300, 170);
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
    }

    private void loginCommand() {
        String userName = userText.getText();
        char[] passwordArray = passwordText.getPassword();
        String password = Stream.of(passwordArray).map(String::valueOf).collect(joining());

        if (validateInput(userName, password)) {
//            JOptionPane.showMessageDialog(null, "Login - user: " + userName + ", password: " + password);
            mainFrame.login(userName, password);
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
        JOptionPane.showMessageDialog(null, "Bye Bye");
        System.exit(1);
    }

    @Override
    public void view() {
        this.setVisible(true);
    }
}