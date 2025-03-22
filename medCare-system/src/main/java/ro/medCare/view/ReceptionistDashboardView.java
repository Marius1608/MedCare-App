package ro.medCare.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.medCare.controller.ReceptionistDashboardController;

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

    private final ReceptionistDashboardController controller;
    private final Map<String, ActionListener> menuListeners = new HashMap<>();

    @Autowired
    public ReceptionistDashboardView(ReceptionistDashboardController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        // Setăm frame-ul principal
        frame = new JFrame("MedCare - Panou Recepționist");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Panoul principal cu BorderLayout
        mainPanel = new JPanel(new BorderLayout());

        // Panoul meniu din stânga
        JPanel menuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.setBackground(new Color(240, 240, 240));

        // Butoanele din meniu
        appointmentButton = createMenuButton("Programări", "appointments");
        logoutButton = createMenuButton("Deconectare", "logout");
        logoutButton.setBackground(new Color(255, 200, 200));

        // Adăugăm butoanele la panoul de meniu
        menuPanel.add(appointmentButton);
        menuPanel.add(logoutButton);

        // Panoul pentru conținut
        appointmentPanel = new JPanel(new BorderLayout());
        appointmentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adăugăm componentele la panoul principal
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(appointmentPanel, BorderLayout.CENTER);

        // Adăugăm panoul principal la frame
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
        // Afișăm automat panoul de programări
        appointmentButton.doClick();
    }

    public void close() {
        frame.dispose();
    }

    public void displayAppointmentManagementPanel() {
        // Acest panel va fi gestionat de controllerul de programări
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