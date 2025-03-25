package ro.medCare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;

@SpringBootApplication
@EnableJpaRepositories
public class MedCareSystemApplication {

    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.FlatLightLaf.install();

        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SpringApplication.run(MedCareSystemApplication.class, args);
    }
}