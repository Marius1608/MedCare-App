package ro.medCare.view;

import ro.medCare.util.UIStyler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.medCare.model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component
public class DoctorManagementView {
    private JTable doctorTable;
    private DefaultTableModel doctorTableModel;
    private JTextField nameField;
    private JTextField specializationField;
    private JTextField workHoursField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JScrollPane scrollPane;
    private JPanel formPanel;
    private JPanel mainPanel;
    private JLabel errorLabel;

    private Doctor selectedDoctor;


    public DoctorManagementView() {
        initialize();
    }

    private void initialize() {
        // Initialize the main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add a title panel at the top
        JPanel titlePanel = UIStyler.createTitlePanel("Doctor Management");
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Set up the table with improved styling
        String[] columnNames = {"ID", "Name", "Specialization", "Working Hours"};
        doctorTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(doctorTableModel);
        UIStyler.styleTable(doctorTable);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.getTableHeader().setReorderingAllowed(false);

        // Add mouse listener for row selection
        doctorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Long id = Long.parseLong(doctorTable.getValueAt(selectedRow, 0).toString());
                    String name = doctorTable.getValueAt(selectedRow, 1).toString();
                    String specialization = doctorTable.getValueAt(selectedRow, 2).toString();
                    String workHours = doctorTable.getValueAt(selectedRow, 3).toString();

                    selectedDoctor = new Doctor();
                    selectedDoctor.setId(id);
                    selectedDoctor.setName(name);
                    selectedDoctor.setSpecialization(specialization);
                    selectedDoctor.setWorkHours(workHours);

                    populateForm(selectedDoctor);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Create a styled scroll pane for the table
        scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create a container for the form and buttons
        JPanel formContainer = new JPanel(new BorderLayout(10, 10));
        formContainer.setBackground(UIStyler.BACKGROUND_COLOR);

        // Create the form panel with improved styling
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Doctor Details"),
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

        // Specialization field
        JLabel specializationLabel = new JLabel("Specialization:");
        UIStyler.styleLabel(specializationLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(specializationLabel, gbc);

        specializationField = new JTextField(20);
        specializationField.setFont(UIStyler.REGULAR_FONT);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(specializationField, gbc);

        // Working hours field
        JLabel workHoursLabel = new JLabel("Working Hours (HH:MM-HH:MM):");
        UIStyler.styleLabel(workHoursLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(workHoursLabel, gbc);

        workHoursField = new JTextField(20);
        workHoursField.setFont(UIStyler.REGULAR_FONT);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(workHoursField, gbc);

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
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(5);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);

        // Add the split pane to the main panel
        mainPanel.add(splitPane, BorderLayout.CENTER);
    }

    private void populateForm(Doctor doctor) {
        nameField.setText(doctor.getName());
        specializationField.setText(doctor.getSpecialization());
        workHoursField.setText(doctor.getWorkHours());
    }

    private void clearForm() {
        nameField.setText("");
        specializationField.setText("");
        workHoursField.setText("");
        errorLabel.setText("");
        selectedDoctor = null;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        doctorTable.clearSelection();
    }

    public void displayDoctorForm() {
        clearForm();
    }

    public Doctor getDoctorFormData() {
        Doctor doctor = new Doctor();
        if (selectedDoctor != null) {
            doctor.setId(selectedDoctor.getId());
        }
        doctor.setName(nameField.getText());
        doctor.setSpecialization(specializationField.getText());
        doctor.setWorkHours(workHoursField.getText());
        return doctor;
    }

    public void addCreateDoctorButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addUpdateDoctorButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void addDeleteDoctorButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void refreshDoctorList(List<Doctor> doctors) {
        doctorTableModel.setRowCount(0);
        for (Doctor doctor : doctors) {
            doctorTableModel.addRow(new Object[]{
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getWorkHours()
            });
        }
    }

    public void displayErrorMessage(String message) {
        errorLabel.setText(message);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor;
    }
}