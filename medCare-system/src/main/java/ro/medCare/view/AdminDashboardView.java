package ro.medCare.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.medCare.controller.AdminDashboardController;

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

    private final AdminDashboardController controller;
    private final Map<String, ActionListener> menuListeners = new HashMap<>();

    @Autowired
    public AdminDashboardView(AdminDashboardController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        // Setăm frame-ul principal
        frame = new JFrame("MedCare - Panou Administrator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Panoul principal cu BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panoul meniu din stânga
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.setBackground(new Color(240, 240, 240));

        // Butoanele din meniu
        userButton = createMenuButton("Utilizatori", "users");
        doctorButton = createMenuButton("Medici", "doctors");
        serviceButton = createMenuButton("Servicii", "services");
        reportButton = createMenuButton("Rapoarte", "reports");
        logoutButton = createMenuButton("Deconectare", "logout");
        logoutButton.setBackground(new Color(255, 200, 200));

        // Adăugăm butoanele la panoul de meniu
        menuPanel.add(userButton);
        menuPanel.add(doctorButton);
        menuPanel.add(serviceButton);
        menuPanel.add(reportButton);
        menuPanel.add(logoutButton);

        // Panoul cu taburi pentru conținut
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);

        // Creăm panourile goale pentru fiecare secțiune
        userManagementPanel = new JPanel();
        doctorManagementPanel = new JPanel();
        serviceManagementPanel = new JPanel();
        reportsPanel = new JPanel();

        // Adăugăm panourile la tabbed pane
        tabbedPane.addTab("Utilizatori", userManagementPanel);
        tabbedPane.addTab("Medici", doctorManagementPanel);
        tabbedPane.addTab("Servicii", serviceManagementPanel);
        tabbedPane.addTab("Rapoarte", reportsPanel);

        // Adăugăm componentele la panoul principal
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Adăugăm panoul principal la frame
        frame.getContentPane().add(mainPanel);

        // Acțiune implicită - afișăm panoul de utilizatori la început
        userButton.doClick();
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