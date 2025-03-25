package ro.medCare.view;

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

    public ServiceManagementView() {
        initialize();
    }

    private void initialize() {
        mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set up table
        String[] columnNames = {"ID", "Name", "Price (RON)", "Duration (min)"};
        serviceTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        serviceTable = new JTable(serviceTableModel);
        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceTable.getTableHeader().setReorderingAllowed(false);

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
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // Create form panel
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Service Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Service Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Price field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Price (RON):"), gbc);

        gbc.gridx = 1;
        priceField = new JTextField(20);
        formPanel.add(priceField, gbc);

        // Duration field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Duration (minutes):"), gbc);

        gbc.gridx = 1;
        durationField = new JTextField(20);
        formPanel.add(durationField, gbc);

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
        // Method kept for compatibility
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