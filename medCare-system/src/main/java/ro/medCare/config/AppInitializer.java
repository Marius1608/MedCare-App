package ro.medCare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ro.medCare.view.LoginView;

import jakarta.annotation.PostConstruct;
import javax.swing.SwingUtilities;

@Component
public class AppInitializer {

    private final ApplicationContext context;

    @Autowired
    public AppInitializer(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void initializeApp() {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = context.getBean(LoginView.class);
            loginView.display();
        });
    }
}