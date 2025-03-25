package ro.medCare.view;

import com.toedter.calendar.JDateChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import ro.medCare.util.UIStyler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.medCare.dto.ReportDTO;
import ro.medCare.model.Appointment;
import ro.medCare.model.Doctor;
import ro.medCare.model.MedicalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class ReportView {
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JButton generateButton;
    private JButton exportCSVButton;
    private JButton exportXMLButton;
    private JTable reportTable;
    private DefaultTableModel reportTableModel;
    private JPanel chartPanel;
    private JPanel doctorChartPanel;
    private JPanel serviceChartPanel;
    private JFileChooser fileChooser;
    private JScrollPane scrollPane;
    private JPanel controlPanel;
    private JPanel mainPanel;
    private JLabel errorLabel;


    public ReportView() {
        initialize();
    }

    private void initialize() {
        // Initialize the main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add a title panel at the top
        JPanel titlePanel = UIStyler.createTitlePanel("Report Generation");
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Create the control panel with improved styling
        controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Report Settings"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Start date field
        JLabel startDateLabel = new JLabel("Start Date:");
        UIStyler.styleLabel(startDateLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(startDateLabel, gbc);

        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("dd.MM.yyyy");
        startDateChooser.setFont(UIStyler.REGULAR_FONT);

        Date today = new Date();
        Date oneMonthAgo = new Date(today.getTime() - 30L * 24 * 60 * 60 * 1000);
        startDateChooser.setDate(oneMonthAgo);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        controlPanel.add(startDateChooser, gbc);

        // End date field
        JLabel endDateLabel = new JLabel("End Date:");
        UIStyler.styleLabel(endDateLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        controlPanel.add(endDateLabel, gbc);

        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("dd.MM.yyyy");
        endDateChooser.setFont(UIStyler.REGULAR_FONT);
        endDateChooser.setDate(today);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        controlPanel.add(endDateChooser, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(UIStyler.BACKGROUND_COLOR);

        // Create styled buttons
        generateButton = new JButton("Generate Report");
        UIStyler.styleSuccessButton(generateButton);
        UIStyler.applyHoverEffect(generateButton);

        exportCSVButton = new JButton("Export to CSV");
        UIStyler.styleButton(exportCSVButton);
        UIStyler.applyHoverEffect(exportCSVButton);
        exportCSVButton.setEnabled(false);

        exportXMLButton = new JButton("Export to XML");
        UIStyler.styleButton(exportXMLButton);
        UIStyler.applyHoverEffect(exportXMLButton);
        exportXMLButton.setEnabled(false);

        buttonPanel.add(generateButton);
        buttonPanel.add(exportCSVButton);
        buttonPanel.add(exportXMLButton);

        // Set up the table with improved styling
        String[] columnNames = {"ID", "Patient", "Doctor", "Specialization", "Date & Time", "Service", "Price", "Status"};
        reportTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(reportTableModel);
        UIStyler.styleTable(reportTable);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.getTableHeader().setReorderingAllowed(false);

        // Create a styled scroll pane for the table
        scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        scrollPane.setPreferredSize(new Dimension(600, 200));

        // Create chart panels
        chartPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chartPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        doctorChartPanel = new JPanel(new BorderLayout());
        doctorChartPanel.setBackground(Color.WHITE);
        doctorChartPanel.setBorder(BorderFactory.createTitledBorder("Top Doctors"));

        serviceChartPanel = new JPanel(new BorderLayout());
        serviceChartPanel.setBackground(Color.WHITE);
        serviceChartPanel.setBorder(BorderFactory.createTitledBorder("Top Services"));

        chartPanel.add(doctorChartPanel);
        chartPanel.add(serviceChartPanel);

        // Create error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(UIStyler.ACCENT_COLOR);
        errorLabel.setFont(UIStyler.SMALL_FONT);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Set up file chooser
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");

        // Create a top panel for control and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create a main content panel with card layout
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(chartPanel, BorderLayout.SOUTH);

        // Add everything to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);
    }

    public void displayDateRangeSelector() {
        // This method is kept for compatibility with the original code
    }

    public void displayReportTable() {
        // This method is kept for compatibility with the original code
    }

    public void displayExportOptions() {
        // Enable export buttons
        exportCSVButton.setEnabled(true);
        exportXMLButton.setEnabled(true);
    }

    public Date getStartDate() {
        return startDateChooser.getDate();
    }

    public Date getEndDate() {
        return endDateChooser.getDate();
    }

    public void addGenerateReportButtonListener(ActionListener listener) {
        generateButton.addActionListener(listener);
    }

    public void addExportToCSVButtonListener(ActionListener listener) {
        exportCSVButton.addActionListener(listener);
    }

    public void addExportToXMLButtonListener(ActionListener listener) {
        exportXMLButton.addActionListener(listener);
    }

    public void displayReport(ReportDTO report) {
        if (report == null) {
            return;
        }

        reportTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        for (Appointment appointment : report.getAppointments()) {
            Date date = Date.from(appointment.getDateTime().atZone(ZoneId.systemDefault()).toInstant());
            String formattedDate = dateFormat.format(date);

            reportTableModel.addRow(new Object[]{
                    appointment.getId(),
                    appointment.getPatientName(),
                    appointment.getDoctor().getName(),
                    appointment.getDoctor().getSpecialization(),
                    formattedDate,
                    appointment.getService().getName(),
                    appointment.getService().getPrice() + " RON",
                    appointment.getStatus()
            });
        }

        // Create and display doctor chart
        JFreeChart doctorChart = createBarChart(report.getDoctorStatistics(), "Top Requested Doctors");
        doctorChartPanel.removeAll();
        doctorChartPanel.add(new ChartPanel(doctorChart), BorderLayout.CENTER);

        // Create and display service chart
        JFreeChart serviceChart = createBarChart(report.getServiceStatistics(), "Top Requested Services");
        serviceChartPanel.removeAll();
        serviceChartPanel.add(new ChartPanel(serviceChart), BorderLayout.CENTER);

        // Refresh the panels
        doctorChartPanel.revalidate();
        doctorChartPanel.repaint();
        serviceChartPanel.revalidate();
        serviceChartPanel.repaint();

        // Enable export options
        displayExportOptions();
    }

    public JFreeChart createBarChart(Map<?, Long> data, String title) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (data != null) {
            int count = 0;
            for (Map.Entry<?, Long> entry : data.entrySet()) {
                if (count >= 5) break; // Limit to top 5 for better visualization

                String name = "";
                if (entry.getKey() instanceof Doctor) {
                    name = ((Doctor) entry.getKey()).getName();
                } else if (entry.getKey() instanceof MedicalService) {
                    name = ((MedicalService) entry.getKey()).getName();
                } else {
                    name = entry.getKey().toString();
                }

                dataset.addValue(entry.getValue(), "Appointments", name);
                count++;
            }
        }

        // Create an improved chart with better styling
        JFreeChart chart = ChartFactory.createBarChart(
                title,
                "",
                "Number of Appointments",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        // Customize chart appearance
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setPaint(UIStyler.TEXT_COLOR);
        chart.getTitle().setFont(UIStyler.HEADER_FONT);

        // Customize plot
        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(Color.LIGHT_GRAY);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.LIGHT_GRAY);
        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, UIStyler.PRIMARY_COLOR);

        return chart;
    }

    public void displayErrorMessage(String message) {
        errorLabel.setText(message);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }
}