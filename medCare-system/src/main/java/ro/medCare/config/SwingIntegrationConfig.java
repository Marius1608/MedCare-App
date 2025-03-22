package ro.medCare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ro.medCare.view.*;

import jakarta.annotation.PostConstruct;

@Configuration
public class SwingIntegrationConfig {

    private final AdminDashboardView adminDashboardView;
    private final ReceptionistDashboardView receptionistDashboardView;
    private final UserManagementView userManagementView;
    private final DoctorManagementView doctorManagementView;
    private final ServiceManagementView serviceManagementView;
    private final AppointmentManagementView appointmentManagementView;
    private final ReportView reportView;

    @Autowired
    public SwingIntegrationConfig(
            AdminDashboardView adminDashboardView,
            ReceptionistDashboardView receptionistDashboardView,
            UserManagementView userManagementView,
            DoctorManagementView doctorManagementView,
            ServiceManagementView serviceManagementView,
            AppointmentManagementView appointmentManagementView,
            ReportView reportView) {
        this.adminDashboardView = adminDashboardView;
        this.receptionistDashboardView = receptionistDashboardView;
        this.userManagementView = userManagementView;
        this.doctorManagementView = doctorManagementView;
        this.serviceManagementView = serviceManagementView;
        this.appointmentManagementView = appointmentManagementView;
        this.reportView = reportView;
    }

    @PostConstruct
    public void initialize() {
        setupPanels();
    }

    private void setupPanels() {
        adminDashboardView.getUserManagementPanel().setLayout(new java.awt.BorderLayout());
        adminDashboardView.getUserManagementPanel().add(userManagementView.getMainPanel());

        adminDashboardView.getDoctorManagementPanel().setLayout(new java.awt.BorderLayout());
        adminDashboardView.getDoctorManagementPanel().add(doctorManagementView.getMainPanel());

        adminDashboardView.getServiceManagementPanel().setLayout(new java.awt.BorderLayout());
        adminDashboardView.getServiceManagementPanel().add(serviceManagementView.getMainPanel());

        adminDashboardView.getReportsPanel().setLayout(new java.awt.BorderLayout());
        adminDashboardView.getReportsPanel().add(reportView.getMainPanel());

        receptionistDashboardView.getAppointmentPanel().setLayout(new java.awt.BorderLayout());
        receptionistDashboardView.getAppointmentPanel().add(appointmentManagementView.getMainPanel());
    }
}