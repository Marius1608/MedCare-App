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
        frame = new JFrame("MedCare - Receptionist Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("MedCare Receptionist Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Create menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(52, 73, 94));

        appointmentButton = new JButton("Appointments");
        appointmentButton.setMaximumSize(new Dimension(150, 40));
        logoutButton = new JButton("Logout");
        logoutButton.setMaximumSize(new Dimension(150, 40));

        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(appointmentButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoutButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create appointment panel
        appointmentPanel = new JPanel(new BorderLayout());

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(appointmentPanel, BorderLayout.CENTER);

        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public void display() {
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }

    public void displayAppointmentManagementPanel() {
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