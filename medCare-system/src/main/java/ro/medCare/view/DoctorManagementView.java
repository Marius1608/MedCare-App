package ro.medCare.view;

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
        mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set up table
        String[] columnNames = {"ID", "Name", "Specialization", "Working Hours"};
        doctorTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(doctorTableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.getTableHeader().setReorderingAllowed(false);

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

        scrollPane = new JScrollPane(doctorTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // Create form panel
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Doctor Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Specialization field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Specialization:"), gbc);

        gbc.gridx = 1;
        specializationField = new JTextField(20);
        formPanel.add(specializationField, gbc);

        // Work hours field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Working Hours (HH:MM-HH:MM):"), gbc);

        gbc.gridx = 1;
        workHoursField = new JTextField(20);
        formPanel.add(workHoursField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        // Add clear button functionality
        clearButton.addActionListener(e -> clearForm());

        // Assemble all panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);
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

    public void displayDoctorList() {
        // Method kept for compatibility
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