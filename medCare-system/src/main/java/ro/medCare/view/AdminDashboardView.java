package ro.medCare.view;

import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminDashboardView {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JPanel userManagementPanel;
    private JPanel doctorManagementPanel;
    private JPanel serviceManagementPanel;
    private JPanel reportsPanel;
    private JButton userButton;
    private JButton doctorButton;
    private JButton serviceButton;
    private JButton reportButton;
    private JButton logoutButton;

    private final Map<String, ActionListener> menuListeners = new HashMap<>();

    public AdminDashboardView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("MedCare - Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("MedCare Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Create menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(52, 73, 94));

        userButton = new JButton("Users");
        userButton.setMaximumSize(new Dimension(150, 40));
        doctorButton = new JButton("Doctors");
        doctorButton.setMaximumSize(new Dimension(150, 40));
        serviceButton = new JButton("Services");
        serviceButton.setMaximumSize(new Dimension(150, 40));
        reportButton = new JButton("Reports");
        reportButton.setMaximumSize(new Dimension(150, 40));
        logoutButton = new JButton("Logout");
        logoutButton.setMaximumSize(new Dimension(150, 40));

        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(userButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(doctorButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(serviceButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(reportButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoutButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Set up tabbed pane
        tabbedPane = new JTabbedPane();

        userManagementPanel = new JPanel(new BorderLayout());
        doctorManagementPanel = new JPanel(new BorderLayout());
        serviceManagementPanel = new JPanel(new BorderLayout());
        reportsPanel = new JPanel(new BorderLayout());

        tabbedPane.addTab("Users", userManagementPanel);
        tabbedPane.addTab("Doctors", doctorManagementPanel);
        tabbedPane.addTab("Services", serviceManagementPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public void display() {
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }

    public void displayUserManagementPanel() {
        tabbedPane.setSelectedIndex(0);
    }

    public void displayDoctorManagementPanel() {
        tabbedPane.setSelectedIndex(1);
    }

    public void displayServiceManagementPanel() {
        tabbedPane.setSelectedIndex(2);
    }

    public void displayReportsPanel() {
        tabbedPane.setSelectedIndex(3);
    }

    public void addMenuButtonListeners(Map<String, ActionListener> listeners) {
        menuListeners.putAll(listeners);

        if (listeners.containsKey("users")) {
            userButton.addActionListener(listeners.get("users"));
        }

        if (listeners.containsKey("doctors")) {
            doctorButton.addActionListener(listeners.get("doctors"));
        }

        if (listeners.containsKey("services")) {
            serviceButton.addActionListener(listeners.get("services"));
        }

        if (listeners.containsKey("reports")) {
            reportButton.addActionListener(listeners.get("reports"));
        }

        if (listeners.containsKey("logout")) {
            logoutButton.addActionListener(listeners.get("logout"));
        }
    }

    public JPanel getUserManagementPanel() {
        return userManagementPanel;
    }

    public JPanel getDoctorManagementPanel() {
        return doctorManagementPanel;
    }

    public JPanel getServiceManagementPanel() {
        return serviceManagementPanel;
    }

    public JPanel getReportsPanel() {
        return reportsPanel;
    }
}