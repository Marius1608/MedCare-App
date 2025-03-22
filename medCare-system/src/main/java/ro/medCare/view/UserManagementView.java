package ro.medCare.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.medCare.model.User;
import ro.medCare.model.UserRole;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component
public class UserManagementView {
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<UserRole> roleComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JScrollPane scrollPane;
    private JPanel formPanel;
    private JPanel mainPanel;
    private JLabel errorLabel;

    private User selectedUser;

    @Autowired
    public UserManagementView() {
        initialize();
    }

    private void initialize() {

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"ID", "Nume", "Username", "Rol"};
        userTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(userTableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Long id = Long.parseLong(userTable.getValueAt(selectedRow, 0).toString());
                    String name = userTable.getValueAt(selectedRow, 1).toString();
                    String username = userTable.getValueAt(selectedRow, 2).toString();
                    UserRole role = UserRole.valueOf(userTable.getValueAt(selectedRow, 3).toString());

                    selectedUser = new User();
                    selectedUser.setId(id);
                    selectedUser.setName(name);
                    selectedUser.setUsername(username);
                    selectedUser.setPassword(""); // Parola nu este afișată
                    selectedUser.setRole(role);

                    populateForm(selectedUser);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Detalii utilizator"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nume:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Parolă:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Rol:"), gbc);

        gbc.gridx = 1;
        roleComboBox = new JComboBox<>(UserRole.values());
        formPanel.add(roleComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Adaugă");
        updateButton = new JButton("Actualizează");
        deleteButton = new JButton("Șterge");
        clearButton = new JButton("Curăță");

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        clearButton.addActionListener(e -> clearForm());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);
    }

    private void populateForm(User user) {
        nameField.setText(user.getName());
        usernameField.setText(user.getUsername());
        passwordField.setText(""); // Nu populam parola din motive de securitate
        roleComboBox.setSelectedItem(user.getRole());
    }

    private void clearForm() {
        nameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        errorLabel.setText("");
        selectedUser = null;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        userTable.clearSelection();
    }

    public void displayUserForm() {
        clearForm();
    }

    public void displayUserList() {
    }

    public User getUserFormData() {
        User user = new User();
        if (selectedUser != null) {
            user.setId(selectedUser.getId());
        }
        user.setName(nameField.getText());
        user.setUsername(usernameField.getText());
        user.setPassword(new String(passwordField.getPassword()));
        user.setRole((UserRole) roleComboBox.getSelectedItem());
        return user;
    }

    public void addCreateUserButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addUpdateUserButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void addDeleteUserButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void refreshUserList(List<User> users) {
        userTableModel.setRowCount(0);
        for (User user : users) {
            userTableModel.addRow(new Object[]{
                    user.getId(),
                    user.getName(),
                    user.getUsername(),
                    user.getRole()
            });
        }
    }

    public void displayErrorMessage(String message) {
        errorLabel.setText(message);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public User getSelectedUser() {
        return selectedUser;
    }
}