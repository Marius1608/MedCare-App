package ro.medCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import ro.medCare.view.AdminDashboardView;
import ro.medCare.view.LoginView;

import jakarta.annotation.PostConstruct;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminDashboardController {

    private final AdminDashboardView adminDashboardView;
    private final UserManagementController userController;
    private final DoctorManagementController doctorController;
    private final ServiceManagementController serviceController;
    private final ReportController reportController;
    private final ApplicationContext context;

    @Autowired
    public AdminDashboardController(
            AdminDashboardView adminDashboardView,
            UserManagementController userController,
            DoctorManagementController doctorController,
            ServiceManagementController serviceController,
            ReportController reportController,
            ApplicationContext context) {
        this.adminDashboardView = adminDashboardView;
        this.userController = userController;
        this.doctorController = doctorController;
        this.serviceController = serviceController;
        this.reportController = reportController;
        this.context = context;
    }

    @PostConstruct
    public void initialize() {
        setupMenuListeners();
    }

    private void setupMenuListeners() {
        Map<String, ActionListener> listeners = new HashMap<>();

        listeners.put("users", e -> {
            adminDashboardView.displayUserManagementPanel();
            userController.initialize();
        });

        listeners.put("doctors", e -> {
            adminDashboardView.displayDoctorManagementPanel();
            doctorController.initialize();
        });

        listeners.put("services", e -> {
            adminDashboardView.displayServiceManagementPanel();
            serviceController.initialize();
        });

        listeners.put("reports", e -> {
            adminDashboardView.displayReportsPanel();
            reportController.initialize();
        });

        listeners.put("logout", e -> handleLogout());
        adminDashboardView.addMenuButtonListeners(listeners);
    }

    private void handleLogout() {
        adminDashboardView.close();
        LoginView loginView = context.getBean(LoginView.class);
        loginView.display();
    }
}