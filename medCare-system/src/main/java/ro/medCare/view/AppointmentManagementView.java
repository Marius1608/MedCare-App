package ro.medCare.view;

import com.toedter.calendar.JDateChooser;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.TimeZone;

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

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"ID", "Pacient", "Medic", "Data și ora", "Serviciu", "Durată", "Status"};
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

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Detalii programare"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nume pacient:"), gbc);

        gbc.gridx = 1;
        patientNameField = new JTextField(20);
        formPanel.add(patientNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Medic:"), gbc);

        gbc.gridx = 1;
        doctorComboBox = new JComboBox<>();
        formPanel.add(doctorComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Data:"), gbc);

        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd.MM.yyyy");
        dateChooser.setDate(new Date());
        formPanel.add(dateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Ora:"), gbc);

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

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Serviciu:"), gbc);

        gbc.gridx = 1;
        serviceComboBox = new JComboBox<>();
        formPanel.add(serviceComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        statusComboBox = new JComboBox<>(AppointmentStatus.values());
        statusComboBox.setSelectedItem(AppointmentStatus.NEW);
        formPanel.add(statusComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        createButton = new JButton("Crează programare");
        updateStatusButton = new JButton("Actualizează status");
        deleteButton = new JButton("Șterge programare");
        clearButton = new JButton("Clear");

        updateStatusButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(createButton);
        buttonPanel.add(updateStatusButton);
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

        // Combinăm data și ora pentru a obține LocalDateTime
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
            // Convertim LocalDateTime în Date pentru afișare formatată
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
}