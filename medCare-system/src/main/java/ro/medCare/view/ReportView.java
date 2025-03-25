package ro.medCare.view;

import com.toedter.calendar.JDateChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
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
        mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Control panel for date selection and buttons
        controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Report Settings"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Start date
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(new JLabel("Start Date:"), gbc);

        gbc.gridx = 1;
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("dd.MM.yyyy");
        // Set default to one month ago
        Date today = new Date();
        Date oneMonthAgo = new Date(today.getTime() - 30L * 24 * 60 * 60 * 1000);
        startDateChooser.setDate(oneMonthAgo);
        controlPanel.add(startDateChooser, gbc);

        // End date
        gbc.gridx = 0;
        gbc.gridy = 1;
        controlPanel.add(new JLabel("End Date:"), gbc);

        gbc.gridx = 1;
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("dd.MM.yyyy");
        endDateChooser.setDate(today);
        controlPanel.add(endDateChooser, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        generateButton = new JButton("Generate Report");
        exportCSVButton = new JButton("Export to CSV");
        exportXMLButton = new JButton("Export to XML");

        exportCSVButton.setEnabled(false);
        exportXMLButton.setEnabled(false);

        buttonPanel.add(generateButton);
        buttonPanel.add(exportCSVButton);
        buttonPanel.add(exportXMLButton);

        // Set up table
        String[] columnNames = {"ID", "Patient", "Doctor", "Specialization", "Date & Time", "Service", "Price", "Status"};
        reportTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(reportTableModel);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(reportTable);

        // Create chart panels
        chartPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        doctorChartPanel = new JPanel(new BorderLayout());
        doctorChartPanel.setBorder(BorderFactory.createTitledBorder("Top Doctors"));

        serviceChartPanel = new JPanel(new BorderLayout());
        serviceChartPanel.setBorder(BorderFactory.createTitledBorder("Top Services"));

        chartPanel.add(doctorChartPanel);
        chartPanel.add(serviceChartPanel);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        // File chooser
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");

        // Assemble all panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(chartPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);
    }

    public void displayDateRangeSelector() {
        // This method is kept for compatibility
    }

    public void displayReportTable() {
        // This method is kept for compatibility
    }

    public void displayExportOptions() {
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

        // Create charts
        JFreeChart doctorChart = createBarChart(report.getDoctorStatistics(), "Top Requested Doctors");
        doctorChartPanel.removeAll();
        doctorChartPanel.add(new ChartPanel(doctorChart), BorderLayout.CENTER);

        JFreeChart serviceChart = createBarChart(report.getServiceStatistics(), "Top Requested Services");
        serviceChartPanel.removeAll();
        serviceChartPanel.add(new ChartPanel(serviceChart), BorderLayout.CENTER);

        doctorChartPanel.revalidate();
        doctorChartPanel.repaint();
        serviceChartPanel.revalidate();
        serviceChartPanel.repaint();

        displayExportOptions();
    }

    public JFreeChart createBarChart(Map<?, Long> data, String title) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (data != null) {
            int count = 0;
            for (Map.Entry<?, Long> entry : data.entrySet()) {
                if (count >= 5) break; // Limit to top 5

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