package ro.medCare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.medCare.model.User;
import ro.medCare.model.UserRole;
import ro.medCare.service.UserService;

import jakarta.annotation.PostConstruct;

@Configuration
public class AuthConfig {

    private final UserService userService;

    @Autowired
    public AuthConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void initAdmin() {
        if (userService.getAllUsers().isEmpty()) {
            User adminUser = new User();
            adminUser.setName("Administrator");
            adminUser.setUsername("admin");
            adminUser.setPassword("admin123");
            adminUser.setRole(UserRole.ADMIN);

            try {
                userService.createUser(adminUser);
                System.out.println("Administrator creat cu succes! Username: admin, Parola: admin123");
            } catch (Exception e) {
                System.err.println("Eroare la crearea administratorului: " + e.getMessage());
            }
        }
    }
}