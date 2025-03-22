package ro.medCare.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.medCare.controller.LoginController;

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

    private final LoginController loginController;

    @Autowired
    public LoginView(LoginController loginController) {
        this.loginController = loginController;
        initialize();
    }

    private void initialize() {
        // Creăm frame-ul principal
        frame = new JFrame("MedCare - Autentificare");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Panoul principal cu GridBagLayout pentru poziționare exactă
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Titlul aplicației
        JLabel titleLabel = new JLabel("MedCare System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(titleLabel, gbc);

        // Label Username
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(usernameLabel, gbc);

        // Câmp Username
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);

        // Label Parolă
        JLabel passwordLabel = new JLabel("Parolă:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(passwordLabel, gbc);

        // Câmp Parolă
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // Buton Login
        loginButton = new JButton("Autentificare");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(loginButton, gbc);

        // Label eroare
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(errorLabel, gbc);

        // Adăugăm panoul principal la frame
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Adăugăm action listener pentru butonul de login
        loginButton.addActionListener(e -> loginController.handleLogin());

        // Permite login prin apăsarea tastei Enter în câmpul de parolă
        passwordField.addActionListener(e -> loginController.handleLogin());
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
    }

    public void close() {
        frame.dispose();
    }
}