package ro.medCare.view;

import ro.medCare.util.UIStyler;
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
        // Set up the main frame
        frame = new JFrame("MedCare - Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyler.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1024, 60));

        JLabel titleLabel = new JLabel("MedCare Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(UIStyler.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Create an improved sidebar menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(52, 73, 94)); // Dark blue
        menuPanel.setPreferredSize(new Dimension(200, 768));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Create a menu title
        JLabel menuTitleLabel = new JLabel("MAIN MENU");
        menuTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        menuTitleLabel.setForeground(Color.WHITE);
        menuTitleLabel.setAlignmentX(0.5f); // 0.5f is equivalent to Component.CENTER_ALIGNMENT
        menuPanel.add(menuTitleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create styled menu buttons
        userButton = createMenuButton("Users", "users", "users");
        doctorButton = createMenuButton("Doctors", "doctors", "doctors");
        serviceButton = createMenuButton("Services", "services", "services");
        reportButton = createMenuButton("Reports", "reports", "reports");
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        logoutButton = createMenuButton("Logout", "logout", "logout");
        logoutButton.setBackground(UIStyler.ACCENT_COLOR);

        // Add the buttons to the menu panel with spacing
        menuPanel.add(userButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(doctorButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(serviceButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(reportButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoutButton);

        // Set up the tabbed pane with improved styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setFont(UIStyler.REGULAR_FONT);
        tabbedPane.setBackground(UIStyler.BACKGROUND_COLOR);

        // Create empty panels for each section
        userManagementPanel = new JPanel(new BorderLayout());
        doctorManagementPanel = new JPanel(new BorderLayout());
        serviceManagementPanel = new JPanel(new BorderLayout());
        reportsPanel = new JPanel(new BorderLayout());

        // Style the panels
        UIStyler.stylePanel(userManagementPanel);
        UIStyler.stylePanel(doctorManagementPanel);
        UIStyler.stylePanel(serviceManagementPanel);
        UIStyler.stylePanel(reportsPanel);

        // Add the panels to the tabbed pane
        tabbedPane.addTab("Users", userManagementPanel);
        tabbedPane.addTab("Doctors", doctorManagementPanel);
        tabbedPane.addTab("Services", serviceManagementPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        // Add components to the main panel
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add components to the frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Set the initial active tab
        userButton.doClick();
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