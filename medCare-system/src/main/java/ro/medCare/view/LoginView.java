package ro.medCare.view;

import ro.medCare.util.UIStyler;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@Component
public class LoginView {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;
    private JPanel mainPanel;

    public LoginView() {
        initialize();
    }

    private void initialize() {
        // Set up the frame with improved appearance
        frame = new JFrame("MedCare - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        // Create a panel for the logo/header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyler.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(500, 80));

        JLabel logoLabel = new JLabel("MedCare System", SwingConstants.CENTER);
        logoLabel.setFont(UIStyler.TITLE_FONT);
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.CENTER);

        // Create the main content panel with improved styling
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create a panel for the login form with rounded corners
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyler.SECONDARY_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints loginGbc = new GridBagConstraints();
        loginGbc.insets = new Insets(5, 5, 5, 5);
        loginGbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field with label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(UIStyler.REGULAR_FONT);
        usernameLabel.setForeground(UIStyler.TEXT_COLOR);
        loginGbc.gridx = 0;
        loginGbc.gridy = 0;
        loginGbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameLabel, loginGbc);

        usernameField = new JTextField(20);
        usernameField.setFont(UIStyler.REGULAR_FONT);
        loginGbc.gridx = 0;
        loginGbc.gridy = 1;
        loginGbc.gridwidth = 2;
        loginPanel.add(usernameField, loginGbc);

        // Password field with label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(UIStyler.REGULAR_FONT);
        passwordLabel.setForeground(UIStyler.TEXT_COLOR);
        loginGbc.gridx = 0;
        loginGbc.gridy = 2;
        loginGbc.gridwidth = 1;
        loginPanel.add(passwordLabel, loginGbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(UIStyler.REGULAR_FONT);
        loginGbc.gridx = 0;
        loginGbc.gridy = 3;
        loginGbc.gridwidth = 2;
        loginPanel.add(passwordField, loginGbc);

        // Login button with improved styling
        loginButton = new JButton("Login");
        UIStyler.styleButton(loginButton);
        UIStyler.applyHoverEffect(loginButton);

        loginGbc.gridx = 0;
        loginGbc.gridy = 4;
        loginGbc.gridwidth = 2;
        loginGbc.insets = new Insets(20, 5, 5, 5);
        loginPanel.add(loginButton, loginGbc);

        // Error label for displaying messages
        errorLabel = new JLabel("");
        errorLabel.setForeground(UIStyler.ACCENT_COLOR);
        errorLabel.setFont(UIStyler.SMALL_FONT);
        loginGbc.gridx = 0;
        loginGbc.gridy = 5;
        loginGbc.gridwidth = 2;
        loginPanel.add(errorLabel, loginGbc);

        // Add the login panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(loginPanel, gbc);

        // Add all components to the frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public void display() {
        frame.setVisible(true);
    }

    public void displayErrorMessage(String message) {
        errorLabel.setText(message);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
        passwordField.addActionListener(listener);
    }

    public void close() {
        frame.dispose();
    }
}