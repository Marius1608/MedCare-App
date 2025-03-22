package ro.medCare.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.medCare.model.MedicalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component
public class ServiceManagementView {
    private JTable serviceTable;
    private DefaultTableModel serviceTableModel;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField durationField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JScrollPane scrollPane;
    private JPanel formPanel;
    private JPanel mainPanel;
    private JLabel errorLabel;

    private MedicalService selectedService;

    @Autowired
    public ServiceManagementView() {
        initialize();
    }

    private void initialize() {
        // Inițializăm panoul principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurăm tabelul
        String[] columnNames = {"ID", "Nume", "Preț (RON)", "Durată (min)"};
        serviceTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celulele nu sunt editabile
            }
        };

        serviceTable = new JTable(serviceTableModel);
        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceTable.getTableHeader().setReorderingAllowed(false);

        // Ascultător pentru click pe rând din tabel
        serviceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = serviceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Long id = Long.parseLong(serviceTable.getValueAt(selectedRow, 0).toString());
                    String name = serviceTable.getValueAt(selectedRow, 1).toString();
                    Double price = Double.parseDouble(serviceTable.getValueAt(selectedRow, 2).toString());
                    Integer duration = Integer.parseInt(serviceTable.getValueAt(selectedRow, 3).toString());

                    selectedService = new MedicalService();
                    selectedService.setId(id);
                    selectedService.setName(name);
                    selectedService.setPrice(price);
                    selectedService.setDuration(duration);

                    populateForm(selectedService);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        scrollPane = new JScrollPane(serviceTable);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        // Configurăm panoul de formular
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Detalii serviciu medical"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adăugăm etichetele și câmpurile
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nume serviciu:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Preț (RON):"), gbc);

        gbc.gridx = 1;
        priceField = new JTextField(20);
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Durată (minute):"), gbc);

        gbc.gridx = 1;
        durationField = new JTextField(20);
        formPanel.add(durationField, gbc);

        // Adăugăm butoanele
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Adaugă");
        updateButton = new JButton("Actualizează");
        deleteButton = new JButton("Șterge");
        clearButton = new JButton("Curăță");

        // Dezactivăm butoanele până când nu este selectat un serviciu
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Label pentru mesaje de eroare
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        // Adăugăm acțiune pentru butonul "Curăță"
        clearButton.addActionListener(e -> clearForm());

        // Organizăm toate componentele în panoul principal
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);
    }

    private void populateForm(MedicalService service) {
        nameField.setText(service.getName());
        priceField.setText(String.valueOf(service.getPrice()));
        durationField.setText(String.valueOf(service.getDuration()));
    }

    private void clearForm() {
        nameField.setText("");
        priceField.setText("");
        durationField.setText("");
        errorLabel.setText("");
        selectedService = null;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        serviceTable.clearSelection();
    }

    public void displayServiceForm() {
        clearForm();
    }

    public void displayServiceList() {
        // Lista va fi populată de controller
    }

    public MedicalService getServiceFormData() {
        MedicalService service = new MedicalService();
        if (selectedService != null) {
            service.setId(selectedService.getId());
        }
        service.setName(nameField.getText());

        try {
            service.setPrice(Double.parseDouble(priceField.getText()));
        } catch (NumberFormatException e) {
            service.setPrice(0.0);
        }

        try {
            service.setDuration(Integer.parseInt(durationField.getText()));
        } catch (NumberFormatException e) {
            service.setDuration(0);
        }

        return service;
    }

    public void addCreateServiceButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addUpdateServiceButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void addDeleteServiceButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void refreshServiceList(List<MedicalService> services) {
        serviceTableModel.setRowCount(0);
        for (MedicalService service : services) {
            serviceTableModel.addRow(new Object[]{
                    service.getId(),
                    service.getName(),
                    service.getPrice(),
                    service.getDuration()
            });
        }
    }

    public void displayErrorMessage(String message) {
        errorLabel.setText(message);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public MedicalService getSelectedService() {
        return selectedService;
    }
}