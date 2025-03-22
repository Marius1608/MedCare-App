package ro.medCare.view;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReceptionistDashboardView {
    private JFrame frame;
    private JPanel mainPanel;
    private JButton appointmentButton;
    private JButton logoutButton;
    private JPanel appointmentPanel;

    private final Map<String, ActionListener> menuListeners = new HashMap<>();

    public ReceptionistDashboardView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("MedCare - Panou Recepționist");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());

        JPanel menuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.setBackground(new Color(240, 240, 240));

        appointmentButton = createMenuButton("Programări", "appointments");
        logoutButton = createMenuButton("Deconectare", "logout");
        logoutButton.setBackground(new Color(255, 200, 200));

        menuPanel.add(appointmentButton);
        menuPanel.add(logoutButton);

        appointmentPanel = new JPanel(new BorderLayout());
        appointmentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(appointmentPanel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
    }

    private JButton createMenuButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setActionCommand(actionCommand);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }

    public void display() {
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }

    public void displayAppointmentManagementPanel() {
        // Aici pot fi adăugate alte acțiuni specifice pentru afișarea panoului de programări
    }

    public void addMenuButtonListeners(Map<String, ActionListener> listeners) {
        menuListeners.putAll(listeners);

        if (listeners.containsKey("appointments")) {
            appointmentButton.addActionListener(listeners.get("appointments"));
        }

        if (listeners.containsKey("logout")) {
            logoutButton.addActionListener(listeners.get("logout"));
        }
    }

    public JPanel getAppointmentPanel() {
        return appointmentPanel;
    }
}