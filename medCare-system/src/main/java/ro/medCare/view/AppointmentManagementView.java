package ro.medCare.view;

import com.toedter.calendar.JDateChooser;
import org.springframework.stereotype.Component;
import ro.medCare.model.Appointment;
import ro.medCare.model.AppointmentStatus;
import ro.medCare.model.Doctor;
import ro.medCare.model.MedicalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class AppointmentManagementView {
    private JTable appointmentTable;
    private DefaultTableModel appointmentTableModel;
    private JTextField patientNameField;
    private JComboBox<Doctor> doctorComboBox;
    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private JComboBox<MedicalService> serviceComboBox;
    private JComboBox<AppointmentStatus> statusComboBox;
    private JButton createButton;
    private JButton updateStatusButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JScrollPane scrollPane;
    private JPanel formPanel;
    private JPanel mainPanel;
    private JLabel errorLabel;

    private Appointment selectedAppointment;

    public AppointmentManagementView() {
        initialize();
    }

    private void initialize() {
        mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set up table
        String[] columnNames = {"ID", "Patient", "Doctor", "Date & Time", "Service", "Duration", "Status"};
        appointmentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentTable = new JTable(appointmentTableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentTable.getTableHeader().setReorderingAllowed(false);

        appointmentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Long id = Long.parseLong(appointmentTable.getValueAt(selectedRow, 0).toString());
                    updateStatusButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // Create form panel
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Patient name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Patient Name:"), gbc);

        gbc.gridx = 1;
        patientNameField = new JTextField(20);
        formPanel.add(patientNameField, gbc);

        // Doctor field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Doctor:"), gbc);

        gbc.gridx = 1;
        doctorComboBox = new JComboBox<>();
        formPanel.add(doctorComboBox, gbc);

        // Date field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd.MM.yyyy");
        dateChooser.setDate(new Date());
        formPanel.add(dateChooser, gbc);

        // Time field
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Time:"), gbc);

        gbc.gridx = 1;
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeModel.setCalendarField(Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        timeSpinner.setValue(calendar.getTime());
        formPanel.add(timeSpinner, gbc);

        // Service field
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Service:"), gbc);

        gbc.gridx = 1;
        serviceComboBox = new JComboBox<>();
        formPanel.add(serviceComboBox, gbc);

        // Status field
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        statusComboBox = new JComboBox<>(AppointmentStatus.values());
        statusComboBox.setSelectedItem(AppointmentStatus.NEW);
        formPanel.add(statusComboBox, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        createButton = new JButton("Create Appointment");
        updateStatusButton = new JButton("Update Status");
        deleteButton = new JButton("Delete Appointment");
        clearButton = new JButton("Clear");

        updateStatusButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(createButton);
        buttonPanel.add(updateStatusButton);
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

    private void clearForm() {
        patientNameField.setText("");
        doctorComboBox.setSelectedIndex(0);
        dateChooser.setDate(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        timeSpinner.setValue(calendar.getTime());

        serviceComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedItem(AppointmentStatus.NEW);
        errorLabel.setText("");
        selectedAppointment = null;
        updateStatusButton.setEnabled(false);
        deleteButton.setEnabled(false);
        appointmentTable.clearSelection();
    }

    public void displayAppointmentForm() {
        clearForm();
    }

    public Appointment getAppointmentFormData() {
        Appointment appointment = new Appointment();
        if (selectedAppointment != null) {
            appointment.setId(selectedAppointment.getId());
        }

        appointment.setPatientName(patientNameField.getText());
        appointment.setDoctor((Doctor) doctorComboBox.getSelectedItem());

        // Combine date and time to get LocalDateTime
        Date selectedDate = dateChooser.getDate();
        Date selectedTime = (Date) timeSpinner.getValue();

        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(selectedDate);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(selectedTime);

        Calendar combinedCal = Calendar.getInstance();
        combinedCal.set(Calendar.YEAR, dateCal.get(Calendar.YEAR));
        combinedCal.set(Calendar.MONTH, dateCal.get(Calendar.MONTH));
        combinedCal.set(Calendar.DAY_OF_MONTH, dateCal.get(Calendar.DAY_OF_MONTH));
        combinedCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        combinedCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        combinedCal.set(Calendar.SECOND, 0);
        combinedCal.set(Calendar.MILLISECOND, 0);

        LocalDateTime dateTime = combinedCal.getTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        appointment.setDateTime(dateTime);
        appointment.setService((MedicalService) serviceComboBox.getSelectedItem());
        appointment.setStatus((AppointmentStatus) statusComboBox.getSelectedItem());

        return appointment;
    }

    public void populateDoctorComboBox(List<Doctor> doctors) {
        doctorComboBox.removeAllItems();
        for (Doctor doctor : doctors) {
            doctorComboBox.addItem(doctor);
        }
    }

    public void populateServiceComboBox(List<MedicalService> services) {
        serviceComboBox.removeAllItems();
        for (MedicalService service : services) {
            serviceComboBox.addItem(service);
        }
    }

    public void addCreateAppointmentButtonListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }

    public void addUpdateAppointmentStatusButtonListener(ActionListener listener) {
        updateStatusButton.addActionListener(listener);
    }

    public void addDeleteAppointmentButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void refreshAppointmentList(List<Appointment> appointments) {
        appointmentTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        for (Appointment appointment : appointments) {
            Date date = Date.from(appointment.getDateTime().atZone(ZoneId.systemDefault()).toInstant());
            String formattedDate = dateFormat.format(date);

            appointmentTableModel.addRow(new Object[]{
                    appointment.getId(),
                    appointment.getPatientName(),
                    appointment.getDoctor().getName(),
                    formattedDate,
                    appointment.getService().getName(),
                    appointment.getService().getDuration() + " min",
                    appointment.getStatus()
            });
        }
    }

    public void displayErrorMessage(String message) {
        errorLabel.setText(message);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Long getSelectedAppointmentId() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            return Long.parseLong(appointmentTable.getValueAt(selectedRow, 0).toString());
        }
        return null;
    }

    public AppointmentStatus getSelectedStatus() {
        return (AppointmentStatus) statusComboBox.getSelectedItem();
    }

    public void setSelectedAppointment(Appointment appointment) {
        this.selectedAppointment = appointment;

        if (appointment != null) {
            patientNameField.setText(appointment.getPatientName());

            for (int i = 0; i < doctorComboBox.getItemCount(); i++) {
                Doctor doctor = doctorComboBox.getItemAt(i);
                if (doctor.getId().equals(appointment.getDoctor().getId())) {
                    doctorComboBox.setSelectedIndex(i);
                    break;
                }
            }

            Date dateTime = Date.from(appointment.getDateTime().atZone(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(dateTime);
            timeSpinner.setValue(dateTime);

            for (int i = 0; i < serviceComboBox.getItemCount(); i++) {
                MedicalService service = serviceComboBox.getItemAt(i);
                if (service.getId().equals(appointment.getService().getId())) {
                    serviceComboBox.setSelectedIndex(i);
                    break;
                }
            }

            statusComboBox.setSelectedItem(appointment.getStatus());

            updateStatusButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }
}