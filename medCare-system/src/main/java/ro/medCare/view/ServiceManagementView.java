package ro.medCare.view;

import ro.medCare.util.UIStyler;
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


    public ServiceManagementView() {
        initialize();
    }

    private void initialize() {
        // Initialize the main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add a title panel at the top
        JPanel titlePanel = UIStyler.createTitlePanel("Medical Service Management");
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Set up the table with improved styling
        String[] columnNames = {"ID", "Name", "Price (RON)", "Duration (min)"};
        serviceTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        serviceTable = new JTable(serviceTableModel);
        UIStyler.styleTable(serviceTable);
        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceTable.getTableHeader().setReorderingAllowed(false);

        // Add mouse listener for row selection
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

        // Create a styled scroll pane for the table
        scrollPane = new JScrollPane(serviceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create a container for the form and buttons
        JPanel formContainer = new JPanel(new BorderLayout(10, 10));
        formContainer.setBackground(UIStyler.BACKGROUND_COLOR);

        // Create the form panel with improved styling
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Service Details"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Service name field
        JLabel nameLabel = new JLabel("Service Name:");
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

        // Price field
        JLabel priceLabel = new JLabel("Price (RON):");
        UIStyler.styleLabel(priceLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(priceLabel, gbc);

        priceField = new JTextField(20);
        priceField.setFont(UIStyler.REGULAR_FONT);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(priceField, gbc);

        // Duration field
        JLabel durationLabel = new JLabel("Duration (minutes):");
        UIStyler.styleLabel(durationLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(durationLabel, gbc);

        durationField = new JTextField(20);
        durationField.setFont(UIStyler.REGULAR_FONT);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(durationField, gbc);

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
        // This method is kept for compatibility with the original code
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