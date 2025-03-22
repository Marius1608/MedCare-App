package ro.medCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.medCare.exception.ResourceNotFoundException;
import ro.medCare.exception.ValidationException;
import ro.medCare.model.User;
import ro.medCare.service.UserService;
import ro.medCare.view.UserManagementView;

import javax.swing.*;
import java.util.List;

@Controller
public class UserManagementController {
    private final UserManagementView userManagementView;
    private final UserService userService;

    @Autowired
    public UserManagementController(UserManagementView userManagementView, UserService userService) {
        this.userManagementView = userManagementView;
        this.userService = userService;
    }

    public void initialize() {
        // Configurăm listenerii pentru butoane
        userManagementView.addCreateUserButtonListener(e -> handleCreateUser());
        userManagementView.addUpdateUserButtonListener(e -> handleUpdateUser());
        userManagementView.addDeleteUserButtonListener(e -> handleDeleteUser());

        // Încărcăm lista de utilizatori
        refreshUserList();
    }

    private void handleCreateUser() {
        try {
            User user = userManagementView.getUserFormData();

            // Validăm datele
            if (!validateUserData(user)) {
                return;
            }

            // Creăm utilizatorul
            userService.createUser(user);

            // Actualizăm lista și curățăm formularul
            refreshUserList();
            userManagementView.displayUserForm();
            userManagementView.displayErrorMessage("Utilizatorul a fost creat cu succes!");
        } catch (ValidationException ex) {
            userManagementView.displayErrorMessage("Eroare: " + ex.getMessage());
        } catch (Exception ex) {
            userManagementView.displayErrorMessage("Eroare la crearea utilizatorului: " + ex.getMessage());
        }
    }

    private void handleUpdateUser() {
        try {
            User selectedUser = userManagementView.getSelectedUser();
            if (selectedUser == null) {
                userManagementView.displayErrorMessage("Selectați un utilizator pentru actualizare!");
                return;
            }

            User user = userManagementView.getUserFormData();

            // Validăm datele
            if (!validateUserData(user)) {
                return;
            }

            // Dacă parola este goală, păstrăm parola existentă
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                // În acest caz, service-ul trebuie să știe să nu actualizeze parola
                // sau să obțină parola existentă din baza de date
            }

            // Actualizăm utilizatorul
            userService.updateUser(user);

            // Actualizăm lista și curățăm formularul
            refreshUserList();
            userManagementView.displayUserForm();
            userManagementView.displayErrorMessage("Utilizatorul a fost actualizat cu succes!");
        } catch (ValidationException ex) {
            userManagementView.displayErrorMessage("Eroare: " + ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            userManagementView.displayErrorMessage("Utilizatorul nu a fost găsit: " + ex.getMessage());
        } catch (Exception ex) {
            userManagementView.displayErrorMessage("Eroare la actualizarea utilizatorului: " + ex.getMessage());
        }
    }

    private void handleDeleteUser() {
        try {
            User selectedUser = userManagementView.getSelectedUser();
            if (selectedUser == null) {
                userManagementView.displayErrorMessage("Selectați un utilizator pentru ștergere!");
                return;
            }

            // Confirmarea ștergerii
            int option = JOptionPane.showConfirmDialog(
                    null,
                    "Sigur doriți să ștergeți utilizatorul " + selectedUser.getName() + "?",
                    "Confirmare ștergere",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                userService.deleteUser(selectedUser.getId());

                // Actualizăm lista și curățăm formularul
                refreshUserList();
                userManagementView.displayUserForm();
                userManagementView.displayErrorMessage("Utilizatorul a fost șters cu succes!");
            }
        } catch (ResourceNotFoundException ex) {
            userManagementView.displayErrorMessage("Utilizatorul nu a fost găsit: " + ex.getMessage());
        } catch (Exception ex) {
            userManagementView.displayErrorMessage("Eroare la ștergerea utilizatorului: " + ex.getMessage());
        }
    }

    private void refreshUserList() {
        try {
            List<User> users = userService.getAllUsers();
            userManagementView.refreshUserList(users);
        } catch (Exception ex) {
            userManagementView.displayErrorMessage("Eroare la încărcarea listei de utilizatori: " + ex.getMessage());
        }
    }

    private boolean validateUserData(User user) {
        StringBuilder errorMessage = new StringBuilder();

        // Validăm numele
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            errorMessage.append("Numele este obligatoriu!\n");
        } else if (user.getName().length() < 3 || user.getName().length() > 100) {
            errorMessage.append("Numele trebuie să aibă între 3 și 100 de caractere!\n");
        }

        // Validăm username-ul
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            errorMessage.append("Username-ul este obligatoriu!\n");
        } else if (user.getUsername().length() < 3 || user.getUsername().length() > 50) {
            errorMessage.append("Username-ul trebuie să aibă între 3 și 50 de caractere!\n");
        }

        // Validăm parola doar pentru utilizatori noi sau dacă a fost modificată
        if (user.getId() == null || (user.getPassword() != null && !user.getPassword().isEmpty())) {
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                errorMessage.append("Parola este obligatorie!\n");
            } else if (user.getPassword().length() < 6) {
                errorMessage.append("Parola trebuie să aibă cel puțin 6 caractere!\n");
            }
        }

        // Dacă avem erori, le afișăm
        if (errorMessage.length() > 0) {
            userManagementView.displayErrorMessage(errorMessage.toString());
            return false;
        }

        return true;
    }
}