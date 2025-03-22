package ro.medCare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ro.medCare.view.LoginView;

import javax.swing.*;

@SpringBootApplication
@EnableJpaRepositories
public class MedCareDesktopApplication {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConfigurableApplicationContext context = SpringApplication.run(MedCareDesktopApplication.class, args);

        SwingUtilities.invokeLater(() -> {
            // Obținem loginView din context Spring
            LoginView loginView = context.getBean(LoginView.class);
            loginView.display();
        });
    }
}