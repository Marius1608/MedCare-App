package ro.medCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.medCare.dto.ReportDTO;
import ro.medCare.service.ReportService;
import ro.medCare.view.ReportView;

import javax.swing.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class ReportController {

    private final ReportView reportView;
    private final ReportService reportService;
    private ReportDTO currentReport;

    @Autowired
    public ReportController(ReportView reportView, ReportService reportService) {
        this.reportView = reportView;
        this.reportService = reportService;
    }

    public void initialize() {

        reportView.addGenerateReportButtonListener(e -> handleGenerateReport());
        reportView.addExportToCSVButtonListener(e -> handleExportToCSV());
        reportView.addExportToXMLButtonListener(e -> handleExportToXML());
    }

    private void handleGenerateReport() {
        try {
            Date startDate = reportView.getStartDate();
            Date endDate = reportView.getEndDate();

            if (!validateDateRange(startDate, endDate)) {
                return;
            }

            LocalDateTime startDateTime = startDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            LocalDateTime endDateTime = endDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .plusDays(1)  // Adăugăm o zi pentru a include și sfârșitul zilei
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);

            currentReport = reportService.generateReport(startDateTime, endDateTime);

            reportView.displayReport(currentReport);
            reportView.displayErrorMessage("Raport generat cu succes!");
        } catch (Exception ex) {
            reportView.displayErrorMessage("Eroare la generarea raportului: " + ex.getMessage());
        }
    }

    private void handleExportToCSV() {
        try {
            if (currentReport == null) {
                reportView.displayErrorMessage("Nu există un raport generat! Generați mai întâi un raport.");
                return;
            }

            JFileChooser fileChooser = reportView.getFileChooser();
            fileChooser.setSelectedFile(new File("raport.csv"));
            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
                }

                File csvFile = reportService.exportToCSV(currentReport);

                java.nio.file.Files.copy(
                        csvFile.toPath(),
                        selectedFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                csvFile.delete();

                reportView.displayErrorMessage("Raport exportat cu succes în format CSV!");
            }
        } catch (Exception ex) {
            reportView.displayErrorMessage("Eroare la exportul raportului în CSV: " + ex.getMessage());
        }
    }

    private void handleExportToXML() {
        try {
            if (currentReport == null) {
                reportView.displayErrorMessage("Nu există un raport generat! Generați mai întâi un raport.");
                return;
            }

            JFileChooser fileChooser = reportView.getFileChooser();
            fileChooser.setSelectedFile(new File("raport.xml"));
            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                if (!selectedFile.getName().toLowerCase().endsWith(".xml")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".xml");
                }

                File xmlFile = reportService.exportToXML(currentReport);

                java.nio.file.Files.copy(
                        xmlFile.toPath(),
                        selectedFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                xmlFile.delete();

                reportView.displayErrorMessage("Raport exportat cu succes în format XML!");
            }
        } catch (Exception ex) {
            reportView.displayErrorMessage("Eroare la exportul raportului în XML: " + ex.getMessage());
        }
    }

    private boolean validateDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            reportView.displayErrorMessage("Selectați datele de început și sfârșit!");
            return false;
        }

        if (startDate.after(endDate)) {
            reportView.displayErrorMessage("Data de început trebuie să fie înainte de data de sfârșit!");
            return false;
        }

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

        if (diffInDays > 365) {
            reportView.displayErrorMessage("Intervalul selectat este prea mare! Selectați un interval de maximum 1 an.");
            return false;
        }

        return true;
    }
}