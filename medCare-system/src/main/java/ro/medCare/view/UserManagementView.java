package ro.medCare.view;

import ro.medCare.util.UIStyler;
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


    public UserManagementView() {
        initialize();
    }

    private void initialize() {
        // Initialize the main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add a title panel at the top
        JPanel titlePanel = UIStyler.createTitlePanel("User Management");
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Set up the table with improved styling
        String[] columnNames = {"ID", "Name", "Username", "Role"};
        userTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(userTableModel);
        UIStyler.styleTable(userTable);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);

        // Add mouse listener for row selection
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
                    selectedUser.setPassword(""); // Password is not displayed
                    selectedUser.setRole(role);

                    populateForm(selectedUser);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Create a styled scroll pane for the table
        scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create a container for the form and buttons
        JPanel formContainer = new JPanel(new BorderLayout(10, 10));
        formContainer.setBackground(UIStyler.BACKGROUND_COLOR);

        // Create the form panel with improved styling
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("User Details"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Name field
        JLabel nameLabel = new JLabel("Name:");
        UIStyler.styleLabel(nameLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(UIStyler.REGULAR_FONT);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(nameField, gbc);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        UIStyler.styleLabel(usernameLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(UIStyler.REGULAR_FONT);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        UIStyler.styleLabel(passwordLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(UIStyler.REGULAR_FONT);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(passwordField, gbc);

        // Role field
        JLabel roleLabel = new JLabel("Role:");
        UIStyler.styleLabel(roleLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(roleLabel, gbc);

        roleComboBox = new JComboBox<>(UserRole.values());
        roleComboBox.setFont(UIStyler.REGULAR_FONT);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(roleComboBox, gbc);

        // Create a panel for buttons with improved styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(UIStyler.BACKGROUND_COLOR);

        // Create styled buttons
        addButton = new JButton("Add");
        UIStyler.styleSuccessButton(addButton);
        UIStyler.applyHoverEffect(addButton);

        updateButton = new JButton("Update");
        UIStyler.styleButton(updateButton);
        UIStyler.applyHoverEffect(updateButton);
        updateButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        UIStyler.styleDangerButton(deleteButton);
        UIStyler.applyHoverEffect(deleteButton);
        deleteButton.setEnabled(false);

        clearButton = new JButton("Clear");
        UIStyler.styleWarningButton(clearButton);
        UIStyler.applyHoverEffect(clearButton);

        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Add the clear functionality
        clearButton.addActionListener(e -> clearForm());

        // Create error label at the bottom
        errorLabel = new JLabel("");
        errorLabel.setForeground(UIStyler.ACCENT_COLOR);
        errorLabel.setFont(UIStyler.SMALL_FONT);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to the container
        formContainer.add(formPanel, BorderLayout.NORTH);
        formContainer.add(buttonPanel, BorderLayout.CENTER);
        formContainer.add(errorLabel, BorderLayout.SOUTH);

        // Create a split pane for form and table
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formContainer, scrollPane);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(5);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);

        // Add the split pane to the main panel
        mainPanel.add(splitPane, BorderLayout.CENTER);
    }

    private void populateForm(User user) {
        nameField.setText(user.getName());
        usernameField.setText(user.getUsername());
        passwordField.setText(""); // Do not populate password for security reasons
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
        // This method is kept for compatibility with the original code
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