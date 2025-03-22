package ro.medCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import ro.medCare.view.LoginView;
import ro.medCare.view.ReceptionistDashboardView;

import javax.annotation.PostConstruct;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ReceptionistDashboardController {
    private final ReceptionistDashboardView receptionistDashboardView;
    private final AppointmentManagementController appointmentController;
    private final ApplicationContext context;

    @Autowired
    public ReceptionistDashboardController(
            ReceptionistDashboardView receptionistDashboardView,
            AppointmentManagementController appointmentController,
            ApplicationContext context) {
        this.receptionistDashboardView = receptionistDashboardView;
        this.appointmentController = appointmentController;
        this.context = context;
    }

    @PostConstruct
    public void initialize() {
        setupMenuListeners();
    }

    private void setupMenuListeners() {
        Map<String, ActionListener> listeners = new HashMap<>();

        // Listener pentru secțiunea Programări
        listeners.put("appointments", e -> {
            receptionistDashboardView.displayAppointmentManagementPanel();
            appointmentController.initialize();
        });

        // Listener pentru Logout
        listeners.put("logout", e -> handleLogout());

        // Adăugăm listenerii la view
        receptionistDashboardView.addMenuButtonListeners(listeners);
    }

    private void handleLogout() {
        receptionistDashboardView.close();
        LoginView loginView = context.getBean(LoginView.class);
        loginView.display();
    }
}