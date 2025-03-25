package ro.medCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.medCare.exception.ResourceNotFoundException;
import ro.medCare.exception.ValidationException;
import ro.medCare.model.Appointment;
import ro.medCare.model.Doctor;
import ro.medCare.model.MedicalService;
import ro.medCare.service.AppointmentService;
import ro.medCare.service.DoctorService;
import ro.medCare.service.MedicalServiceService;
import ro.medCare.view.AppointmentManagementView;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AppointmentManagementController {

    private final AppointmentManagementView appointmentManagementView;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final MedicalServiceService medicalServiceService;

    @Autowired
    public AppointmentManagementController(
            AppointmentManagementView appointmentManagementView,
            AppointmentService appointmentService,
            DoctorService doctorService,
            MedicalServiceService medicalServiceService) {
        this.appointmentManagementView = appointmentManagementView;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.medicalServiceService = medicalServiceService;
    }

    public void initialize() {

        appointmentManagementView.addCreateAppointmentButtonListener(e -> handleCreateAppointment());
        appointmentManagementView.addUpdateAppointmentStatusButtonListener(e -> handleUpdateAppointmentStatus());
        appointmentManagementView.addDeleteAppointmentButtonListener(e -> handleDeleteAppointment());

        loadDoctorsAndServices();
        refreshAppointmentList();
    }

    private void loadDoctorsAndServices() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            appointmentManagementView.populateDoctorComboBox(doctors);
            List<MedicalService> services = medicalServiceService.getAllMedicalServices();
            appointmentManagementView.populateServiceComboBox(services);

        } catch (Exception ex) {
            appointmentManagementView.displayErrorMessage("Eroare la încărcarea datelor: " + ex.getMessage());
        }
    }

    private void handleCreateAppointment() {
        try {
            Appointment appointment = appointmentManagementView.getAppointmentFormData();

            if (!validateAppointmentData(appointment)) {
                return;
            }

            if (!checkDoctorAvailability(appointment.getDoctor().getId(),
                    appointment.getDateTime(),
                    appointment.getService().getDuration())) {
                appointmentManagementView.displayErrorMessage("Medicul nu este disponibil în acest interval orar!");
                return;
            }

            appointmentService.createAppointment(appointment);

            refreshAppointmentList();
            appointmentManagementView.displayAppointmentForm();
            appointmentManagementView.displayErrorMessage("Programarea a fost creată cu succes!");
        } catch (ValidationException ex) {
            appointmentManagementView.displayErrorMessage("Eroare: " + ex.getMessage());
        } catch (Exception ex) {
            appointmentManagementView.displayErrorMessage("Eroare la crearea programării: " + ex.getMessage());
        }
    }

    private void handleUpdateAppointmentStatus() {
        try {
            Long appointmentId = appointmentManagementView.getSelectedAppointmentId();
            if (appointmentId == null) {
                appointmentManagementView.displayErrorMessage("Selectați o programare pentru actualizare!");
                return;
            }

            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            if (appointment == null) {
                appointmentManagementView.displayErrorMessage("Programarea nu a fost găsită!");
                return;
            }
            appointmentService.updateAppointmentStatus(appointmentId, appointmentManagementView.getSelectedStatus());

            refreshAppointmentList();
            appointmentManagementView.displayAppointmentForm();
            appointmentManagementView.displayErrorMessage("Statusul programării a fost actualizat cu succes!");
        } catch (ResourceNotFoundException ex) {
            appointmentManagementView.displayErrorMessage("Programarea nu a fost găsită: " + ex.getMessage());
        } catch (Exception ex) {
            appointmentManagementView.displayErrorMessage("Eroare la actualizarea statusului programării: " + ex.getMessage());
        }
    }

    private void handleDeleteAppointment() {
        try {
            Long appointmentId = appointmentManagementView.getSelectedAppointmentId();
            if (appointmentId == null) {
                appointmentManagementView.displayErrorMessage("Selectați o programare pentru ștergere!");
                return;
            }

            int option = JOptionPane.showConfirmDialog(
                    null,
                    "Sigur doriți să ștergeți această programare?",
                    "Confirmare ștergere",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                appointmentService.deleteAppointment(appointmentId);

                refreshAppointmentList();
                appointmentManagementView.displayAppointmentForm();
                appointmentManagementView.displayErrorMessage("Programarea a fost ștearsă cu succes!");
            }
        } catch (ResourceNotFoundException ex) {
            appointmentManagementView.displayErrorMessage("Programarea nu a fost găsită: " + ex.getMessage());
        } catch (Exception ex) {
            appointmentManagementView.displayErrorMessage("Eroare la ștergerea programării: " + ex.getMessage());
        }
    }

    private void refreshAppointmentList() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            appointmentManagementView.refreshAppointmentList(appointments);
        } catch (Exception ex) {
            appointmentManagementView.displayErrorMessage("Eroare la încărcarea listei de programări: " + ex.getMessage());
        }
    }

    private boolean checkDoctorAvailability(Long doctorId, LocalDateTime dateTime, int duration) {
        try {
            return doctorService.checkAvailability(doctorId, dateTime, duration);
        } catch (Exception ex) {
            appointmentManagementView.displayErrorMessage("Eroare la verificarea disponibilității medicului: " + ex.getMessage());
            return false;
        }
    }

    private boolean validateAppointmentData(Appointment appointment) {
        StringBuilder errorMessage = new StringBuilder();

        if (appointment.getPatientName() == null || appointment.getPatientName().trim().isEmpty()) {
            errorMessage.append("Numele pacientului este obligatoriu!\n");
        } else if (appointment.getPatientName().length() < 3 || appointment.getPatientName().length() > 100) {
            errorMessage.append("Numele pacientului trebuie să aibă între 3 și 100 de caractere!\n");
        }

        if (appointment.getDoctor() == null) {
            errorMessage.append("Selectați un medic pentru programare!\n");
        }

        if (appointment.getDateTime() == null) {
            errorMessage.append("Data și ora sunt obligatorii!\n");
        } else if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
            errorMessage.append("Data și ora trebuie să fie în viitor!\n");
        }

        if (appointment.getService() == null) {
            errorMessage.append("Selectați un serviciu medical!\n");
        }

        if (!errorMessage.isEmpty()) {
            appointmentManagementView.displayErrorMessage(errorMessage.toString());
            return false;
        }

        return true;
    }
}