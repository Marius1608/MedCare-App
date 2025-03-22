package ro.medCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import ro.medCare.model.User;
import ro.medCare.model.UserRole;
import ro.medCare.service.UserService;
import ro.medCare.view.AdminDashboardView;
import ro.medCare.view.LoginView;
import ro.medCare.view.ReceptionistDashboardView;

import javax.swing.*;

@Controller
public class LoginController {
    private final LoginView loginView;
    private final UserService userService;
    private final ApplicationContext context;

    @Autowired
    public LoginController(LoginView loginView, UserService userService, ApplicationContext context) {
        this.loginView = loginView;
        this.userService = userService;
        this.context = context;
    }

    public void handleLogin() {
        String username = loginView.getUsernameField().getText();
        String password = new String(loginView.getPasswordField().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            loginView.displayErrorMessage("Introduceți username și parola!");
            return;
        }

        try {
            User user = userService.authenticate(username, password);
            showAppropriateView(user);
        } catch (Exception e) {
            loginView.displayErrorMessage(e.getMessage());
        }
    }

    private void showAppropriateView(User user) {
        loginView.close();

        SwingUtilities.invokeLater(() -> {
            if (user.getRole() == UserRole.ADMIN) {
                AdminDashboardView adminView = context.getBean(AdminDashboardView.class);
                adminView.display();
            } else if (user.getRole() == UserRole.RECEPTIONIST) {
                ReceptionistDashboardView receptionistView = context.getBean(ReceptionistDashboardView.class);
                receptionistView.display();
            }
        });
    }
}