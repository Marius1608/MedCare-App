package ro.medCare.view;

import ro.medCare.util.UIStyler;
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
        // Set up the main frame
        frame = new JFrame("MedCare - Receptionist Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);

        // Create the main panel
        mainPanel = new JPanel(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyler.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1024, 60));

        JLabel titleLabel = new JLabel("MedCare Receptionist Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(UIStyler.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Create the sidebar menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(52, 73, 94)); // Dark blue
        menuPanel.setPreferredSize(new Dimension(200, 768));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Add menu title
        JLabel menuTitleLabel = new JLabel("MAIN MENU");
        menuTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        menuTitleLabel.setForeground(Color.WHITE);
        menuTitleLabel.setAlignmentX(0.5f); // 0.5f is equivalent to Component.CENTER_ALIGNMENT
        menuPanel.add(menuTitleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create menu buttons with icons
        appointmentButton = createMenuButton("Appointments", "appointments", "appointments");
        menuPanel.add(Box.createVerticalGlue());
        logoutButton = createMenuButton("Logout", "logout", "logout");
        logoutButton.setBackground(UIStyler.ACCENT_COLOR);

        // Add buttons to menu panel
        menuPanel.add(appointmentButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoutButton);

        // Create the appointments panel
        appointmentPanel = new JPanel(new BorderLayout());
        appointmentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        appointmentPanel.setBackground(UIStyler.BACKGROUND_COLOR);

        // Add panels to the main panel
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(appointmentPanel, BorderLayout.CENTER);

        // Add components to the frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text, String actionCommand, String iconName) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setActionCommand(actionCommand);
        button.setPreferredSize(new Dimension(180, 45));
        button.setMaximumSize(new Dimension(180, 45));
        button.setFont(UIStyler.REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94)); // Match sidebar color
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // Apply hover effect
        Color originalColor = button.getBackground();
        Color hoverColor = new Color(44, 62, 80); // Slightly darker on hover

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    public void display() {
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }

    public void displayAppointmentManagementPanel() {
        // This method is kept for maintaining the original functionality
        // Additional visual enhancements can be implemented here if needed
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