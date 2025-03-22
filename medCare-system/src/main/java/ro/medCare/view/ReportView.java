package ro.medCare.view;

import com.toedter.calendar.JDateChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
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

    @Autowired
    public ReportView() {
        initialize();
    }

    private void initialize() {
        // Inițializăm panoul principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurăm panoul de control
        controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Generare raport"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adăugăm etichetele și câmpurile
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(new JLabel("Data de început:"), gbc);

        gbc.gridx = 1;
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("dd.MM.yyyy");
        // Setăm data de început la 1 luna în urmă
        Date today = new Date();
        Date oneMonthAgo = new Date(today.getTime() - 30L * 24 * 60 * 60 * 1000);
        startDateChooser.setDate(oneMonthAgo);
        controlPanel.add(startDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        controlPanel.add(new JLabel("Data de sfârșit:"), gbc);

        gbc.gridx = 1;
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("dd.MM.yyyy");
        endDateChooser.setDate(today);
        controlPanel.add(endDateChooser, gbc);

        // Adăugăm butoanele
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        generateButton = new JButton("Generează raport");
        exportCSVButton = new JButton("Exportă CSV");
        exportXMLButton = new JButton("Exportă XML");

        // Dezactivăm butoanele de export până când nu este generat un raport
        exportCSVButton.setEnabled(false);
        exportXMLButton.setEnabled(false);

        buttonPanel.add(generateButton);
        buttonPanel.add(exportCSVButton);
        buttonPanel.add(exportXMLButton);

        // Configurăm tabelul pentru raport
        String[] columnNames = {"ID", "Pacient", "Medic", "Specializare", "Data și ora", "Serviciu", "Preț", "Status"};
        reportTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celulele nu sunt editabile
            }
        };

        reportTable = new JTable(reportTableModel);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.getTableHeader().setReorderingAllowed(false);

        scrollPane = new JScrollPane(reportTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        // Configurăm panoul pentru grafice
        chartPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Statistici"));

        doctorChartPanel = new JPanel(new BorderLayout());
        doctorChartPanel.setBorder(BorderFactory.createTitledBorder("Top medici"));

        serviceChartPanel = new JPanel(new BorderLayout());
        serviceChartPanel.setBorder(BorderFactory.createTitledBorder("Top servicii"));

        chartPanel.add(doctorChartPanel);
        chartPanel.add(serviceChartPanel);

        // Label pentru mesaje de eroare
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        // File chooser pentru export
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvare raport");

        // Organizăm toate componentele în panoul principal
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(chartPanel, BorderLayout.SOUTH);
        mainPanel.add(errorLabel, BorderLayout.PAGE_END);
    }

    public void displayDateRangeSelector() {
        // Este deja afișat în panoul de control
    }

    public void displayReportTable() {
        // Este deja afișat în panoul principal
    }

    public void displayExportOptions() {
        // Activăm butoanele de export
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

        // Actualizăm tabelul cu programările
        reportTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        for (Appointment appointment : report.getAppointments()) {
            // Convertim LocalDateTime în Date pentru afișare formatată
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

        // Creăm graficul pentru medici
        JFreeChart doctorChart = createBarChart(report.getDoctorStatistics(), "Top medici solicitați");
        doctorChartPanel.removeAll();
        doctorChartPanel.add(new ChartPanel(doctorChart), BorderLayout.CENTER);

        // Creăm graficul pentru servicii
        JFreeChart serviceChart = createBarChart(report.getServiceStatistics(), "Top servicii solicitate");
        serviceChartPanel.removeAll();
        serviceChartPanel.add(new ChartPanel(serviceChart), BorderLayout.CENTER);

        // Actualizăm UI
        doctorChartPanel.revalidate();
        doctorChartPanel.repaint();
        serviceChartPanel.revalidate();
        serviceChartPanel.repaint();

        // Activăm butoanele de export
        displayExportOptions();
    }

    public JFreeChart createBarChart(Map<?, Long> data, String title) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Populăm dataset-ul cu datele primite
        if (data != null) {
            // Limităm la top 5 pentru claritate
            int count = 0;
            for (Map.Entry<?, Long> entry : data.entrySet()) {
                if (count >= 5) break;

                String name = "";
                if (entry.getKey() instanceof Doctor) {
                    name = ((Doctor) entry.getKey()).getName();
                } else if (entry.getKey() instanceof MedicalService) {
                    name = ((MedicalService) entry.getKey()).getName();
                } else {
                    name = entry.getKey().toString();
                }

                dataset.addValue(entry.getValue(), "Programări", name);
                count++;
            }
        }

        // Creăm chart-ul
        JFreeChart chart = ChartFactory.createBarChart(
                title,
                "",
                "Număr programări",
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