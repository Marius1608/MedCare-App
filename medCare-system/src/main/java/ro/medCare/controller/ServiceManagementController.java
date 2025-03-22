package ro.medCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.medCare.exception.ResourceNotFoundException;
import ro.medCare.exception.ValidationException;
import ro.medCare.model.MedicalService;
import ro.medCare.service.MedicalServiceService;
import ro.medCare.view.ServiceManagementView;

import javax.swing.*;
import java.util.List;

@Controller
public class ServiceManagementController {
    private final ServiceManagementView serviceManagementView;
    private final MedicalServiceService medicalServiceService;

    @Autowired
    public ServiceManagementController(ServiceManagementView serviceManagementView, MedicalServiceService medicalServiceService) {
        this.serviceManagementView = serviceManagementView;
        this.medicalServiceService = medicalServiceService;
    }

    public void initialize() {

        serviceManagementView.addCreateServiceButtonListener(e -> handleCreateService());
        serviceManagementView.addUpdateServiceButtonListener(e -> handleUpdateService());
        serviceManagementView.addDeleteServiceButtonListener(e -> handleDeleteService());

        refreshServiceList();
    }

    private void handleCreateService() {
        try {

            MedicalService service = serviceManagementView.getServiceFormData();

            if (!validateServiceData(service)) {
                return;
            }

            medicalServiceService.createMedicalService(service);

            refreshServiceList();
            serviceManagementView.displayServiceForm();
            serviceManagementView.displayErrorMessage("Serviciul medical a fost creat cu succes!");
        } catch (ValidationException ex) {
            serviceManagementView.displayErrorMessage("Eroare: " + ex.getMessage());
        } catch (Exception ex) {
            serviceManagementView.displayErrorMessage("Eroare la crearea serviciului medical: " + ex.getMessage());
        }
    }

    private void handleUpdateService() {
        try {
            MedicalService selectedService = serviceManagementView.getSelectedService();
            if (selectedService == null) {
                serviceManagementView.displayErrorMessage("Selectați un serviciu medical pentru actualizare!");
                return;
            }

            MedicalService service = serviceManagementView.getServiceFormData();

            if (!validateServiceData(service)) {
                return;
            }

            medicalServiceService.updateMedicalService(service);

            refreshServiceList();
            serviceManagementView.displayServiceForm();
            serviceManagementView.displayErrorMessage("Serviciul medical a fost actualizat cu succes!");
        } catch (ValidationException ex) {
            serviceManagementView.displayErrorMessage("Eroare: " + ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            serviceManagementView.displayErrorMessage("Serviciul medical nu a fost găsit: " + ex.getMessage());
        } catch (Exception ex) {
            serviceManagementView.displayErrorMessage("Eroare la actualizarea serviciului medical: " + ex.getMessage());
        }
    }

    private void handleDeleteService() {
        try {
            MedicalService selectedService = serviceManagementView.getSelectedService();
            if (selectedService == null) {
                serviceManagementView.displayErrorMessage("Selectați un serviciu medical pentru ștergere!");
                return;
            }

            int option = JOptionPane.showConfirmDialog(
                    null,
                    "Sigur doriți să ștergeți serviciul medical " + selectedService.getName() + "?",
                    "Confirmare ștergere",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                medicalServiceService.deleteMedicalService(selectedService.getId());
                refreshServiceList();
                serviceManagementView.displayServiceForm();
                serviceManagementView.displayErrorMessage("Serviciul medical a fost șters cu succes!");
            }
        } catch (ResourceNotFoundException ex) {
            serviceManagementView.displayErrorMessage("Serviciul medical nu a fost găsit: " + ex.getMessage());
        } catch (Exception ex) {
            serviceManagementView.displayErrorMessage("Eroare la ștergerea serviciului medical: " + ex.getMessage());
        }
    }

    private void refreshServiceList() {
        try {
            List<MedicalService> services = medicalServiceService.getAllMedicalServices();
            serviceManagementView.refreshServiceList(services);
        } catch (Exception ex) {
            serviceManagementView.displayErrorMessage("Eroare la încărcarea listei de servicii medicale: " + ex.getMessage());
        }
    }

    private boolean validateServiceData(MedicalService service) {
        StringBuilder errorMessage = new StringBuilder();

        if (service.getName() == null || service.getName().trim().isEmpty()) {
            errorMessage.append("Numele serviciului este obligatoriu!\n");
        } else if (service.getName().length() < 3 || service.getName().length() > 100) {
            errorMessage.append("Numele serviciului trebuie să aibă între 3 și 100 de caractere!\n");
        }

        if (service.getPrice() < 0) {
            errorMessage.append("Prețul nu poate fi negativ!\n");
        }

        if (service.getDuration() < 5) {
            errorMessage.append("Durata minimă este de 5 minute!\n");
        }

        if (errorMessage.length() > 0) {
            serviceManagementView.displayErrorMessage(errorMessage.toString());
            return false;
        }

        return true;
    }
}