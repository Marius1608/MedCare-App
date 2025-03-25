package ro.medCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.medCare.exception.ResourceNotFoundException;
import ro.medCare.exception.ValidationException;
import ro.medCare.model.Doctor;
import ro.medCare.service.DoctorService;
import ro.medCare.view.DoctorManagementView;

import javax.swing.*;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class DoctorManagementController {

    private final DoctorManagementView doctorManagementView;
    private final DoctorService doctorService;
    private static final Pattern WORK_HOURS_PATTERN = Pattern.compile(
            "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]-([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");

    @Autowired
    public DoctorManagementController(DoctorManagementView doctorManagementView, DoctorService doctorService) {
        this.doctorManagementView = doctorManagementView;
        this.doctorService = doctorService;
    }

    public void initialize() {

        doctorManagementView.addCreateDoctorButtonListener(e -> handleCreateDoctor());
        doctorManagementView.addUpdateDoctorButtonListener(e -> handleUpdateDoctor());
        doctorManagementView.addDeleteDoctorButtonListener(e -> handleDeleteDoctor());

        refreshDoctorList();
    }

    private void handleCreateDoctor() {
        try {
            Doctor doctor = doctorManagementView.getDoctorFormData();

            if (!validateDoctorData(doctor)) {
                return;
            }

            doctorService.createDoctor(doctor);

            refreshDoctorList();
            doctorManagementView.displayDoctorForm();
            doctorManagementView.displayErrorMessage("Medicul a fost creat cu succes!");
        } catch (ValidationException ex) {
            doctorManagementView.displayErrorMessage("Eroare: " + ex.getMessage());
        } catch (Exception ex) {
            doctorManagementView.displayErrorMessage("Eroare la crearea medicului: " + ex.getMessage());
        }
    }

    private void handleUpdateDoctor() {
        try {
            Doctor selectedDoctor = doctorManagementView.getSelectedDoctor();
            if (selectedDoctor == null) {
                doctorManagementView.displayErrorMessage("Selectați un medic pentru actualizare!");
                return;
            }

            Doctor doctor = doctorManagementView.getDoctorFormData();

            if (!validateDoctorData(doctor)) {
                return;
            }

            doctorService.updateDoctor(doctor);

            refreshDoctorList();
            doctorManagementView.displayDoctorForm();
            doctorManagementView.displayErrorMessage("Medicul a fost actualizat cu succes!");
        } catch (ValidationException ex) {
            doctorManagementView.displayErrorMessage("Eroare: " + ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            doctorManagementView.displayErrorMessage("Medicul nu a fost găsit: " + ex.getMessage());
        } catch (Exception ex) {
            doctorManagementView.displayErrorMessage("Eroare la actualizarea medicului: " + ex.getMessage());
        }
    }

    private void handleDeleteDoctor() {
        try {
            Doctor selectedDoctor = doctorManagementView.getSelectedDoctor();
            if (selectedDoctor == null) {
                doctorManagementView.displayErrorMessage("Selectați un medic pentru ștergere!");
                return;
            }

            int option = JOptionPane.showConfirmDialog(
                    null,
                    "Sigur doriți să ștergeți medicul " + selectedDoctor.getName() + "?",
                    "Confirmare ștergere",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                doctorService.deleteDoctor(selectedDoctor.getId());

                refreshDoctorList();
                doctorManagementView.displayDoctorForm();
                doctorManagementView.displayErrorMessage("Medicul a fost șters cu succes!");
            }
        } catch (ResourceNotFoundException ex) {
            doctorManagementView.displayErrorMessage("Medicul nu a fost găsit: " + ex.getMessage());
        } catch (Exception ex) {
            doctorManagementView.displayErrorMessage("Eroare la ștergerea medicului: " + ex.getMessage());
        }
    }

    private void refreshDoctorList() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            doctorManagementView.refreshDoctorList(doctors);
        } catch (Exception ex) {
            doctorManagementView.displayErrorMessage("Eroare la încărcarea listei de medici: " + ex.getMessage());
        }
    }

    private boolean validateDoctorData(Doctor doctor) {
        StringBuilder errorMessage = new StringBuilder();

        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            errorMessage.append("Numele este obligatoriu!\n");
        } else if (doctor.getName().length() < 3 || doctor.getName().length() > 100) {
            errorMessage.append("Numele trebuie să aibă între 3 și 100 de caractere!\n");
        }

        if (doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty()) {
            errorMessage.append("Specializarea este obligatorie!\n");
        } else if (doctor.getSpecialization().length() < 3 || doctor.getSpecialization().length() > 100) {
            errorMessage.append("Specializarea trebuie să aibă între 3 și 100 de caractere!\n");
        }

        if (doctor.getWorkHours() == null || doctor.getWorkHours().trim().isEmpty()) {
            errorMessage.append("Programul de lucru este obligatoriu!\n");
        } else if (!WORK_HOURS_PATTERN.matcher(doctor.getWorkHours()).matches()) {
            errorMessage.append("Programul de lucru trebuie să fie în format HH:MM-HH:MM (ex: 09:00-17:00)!\n");
        }

        if (errorMessage.length() > 0) {
            doctorManagementView.displayErrorMessage(errorMessage.toString());
            return false;
        }

        return true;
    }
}