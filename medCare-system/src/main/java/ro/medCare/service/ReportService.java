package ro.medCare.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.medCare.dto.ReportDTO;
import ro.medCare.model.Appointment;
import ro.medCare.model.Doctor;
import ro.medCare.model.MedicalService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class ReportService {

    private final AppointmentService appointmentService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public ReportService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Generează un raport cu toate programările între două date și statistici despre medici și servicii
     */
    public ReportDTO generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDateRange(startDate, endDate);
        Map<Doctor, Long> doctorStatistics = appointmentService.getMostRequestedDoctors();
        Map<MedicalService, Long> serviceStatistics = appointmentService.getMostRequestedServices();

        return new ReportDTO(appointments, doctorStatistics, serviceStatistics, startDate, endDate);
    }

    /**
     * Exportă raportul în format CSV
     */
    public File exportToCSV(ReportDTO report) throws IOException {
        File csvFile = File.createTempFile("report_", ".csv");

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Scrie header-ul
            writer.write("ID,Patient Name,Doctor,Specialization,Date & Time,Service,Price,Duration,Status\n");

            // Scrie datele programărilor
            for (Appointment appointment : report.getAppointments()) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%.2f,%d,%s\n",
                        appointment.getId(),
                        appointment.getPatientName(),
                        appointment.getDoctor().getName(),
                        appointment.getDoctor().getSpecialization(),
                        appointment.getDateTime().format(DATE_FORMATTER),
                        appointment.getService().getName(),
                        appointment.getService().getPrice(),
                        appointment.getService().getDuration(),
                        appointment.getStatus().name()
                ));
            }

            // Adaugă secțiunea de statistici pentru medici
            writer.write("\nDoctor Statistics\n");
            writer.write("Doctor,Specialization,Appointments\n");

            for (Map.Entry<Doctor, Long> entry : report.getDoctorStatistics().entrySet()) {
                writer.write(String.format("%s,%s,%d\n",
                        entry.getKey().getName(),
                        entry.getKey().getSpecialization(),
                        entry.getValue()
                ));
            }

            // Adaugă secțiunea de statistici pentru servicii
            writer.write("\nService Statistics\n");
            writer.write("Service,Price,Duration,Appointments\n");

            for (Map.Entry<MedicalService, Long> entry : report.getServiceStatistics().entrySet()) {
                writer.write(String.format("%s,%.2f,%d,%d\n",
                        entry.getKey().getName(),
                        entry.getKey().getPrice(),
                        entry.getKey().getDuration(),
                        entry.getValue()
                ));
            }
        }

        return csvFile;
    }

    /**
     * Exportă raportul în format XML
     */
    public File exportToXML(ReportDTO report) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Creează elementul rădăcină
        Element reportElement = document.createElement("report");
        document.appendChild(reportElement);

        // Adaugă metadate
        Element metadataElement = document.createElement("metadata");
        reportElement.appendChild(metadataElement);

        Element startDateElement = document.createElement("startDate");
        startDateElement.setTextContent(report.getStartDate().format(DATE_FORMATTER));
        metadataElement.appendChild(startDateElement);

        Element endDateElement = document.createElement("endDate");
        endDateElement.setTextContent(report.getEndDate().format(DATE_FORMATTER));
        metadataElement.appendChild(endDateElement);

        // Adaugă programările
        Element appointmentsElement = document.createElement("appointments");
        reportElement.appendChild(appointmentsElement);

        for (Appointment appointment : report.getAppointments()) {
            Element appointmentElement = document.createElement("appointment");
            appointmentsElement.appendChild(appointmentElement);

            appendTextElement(document, appointmentElement, "id", String.valueOf(appointment.getId()));
            appendTextElement(document, appointmentElement, "patientName", appointment.getPatientName());

            Element doctorElement = document.createElement("doctor");
            appointmentElement.appendChild(doctorElement);
            appendTextElement(document, doctorElement, "name", appointment.getDoctor().getName());
            appendTextElement(document, doctorElement, "specialization", appointment.getDoctor().getSpecialization());

            appendTextElement(document, appointmentElement, "dateTime", appointment.getDateTime().format(DATE_FORMATTER));

            Element serviceElement = document.createElement("service");
            appointmentElement.appendChild(serviceElement);
            appendTextElement(document, serviceElement, "name", appointment.getService().getName());
            appendTextElement(document, serviceElement, "price", String.valueOf(appointment.getService().getPrice()));
            appendTextElement(document, serviceElement, "duration", String.valueOf(appointment.getService().getDuration()));

            appendTextElement(document, appointmentElement, "status", appointment.getStatus().name());
        }

        // Adaugă statisticile pentru medici
        Element doctorStatsElement = document.createElement("doctorStatistics");
        reportElement.appendChild(doctorStatsElement);

        for (Map.Entry<Doctor, Long> entry : report.getDoctorStatistics().entrySet()) {
            Element doctorStatElement = document.createElement("doctorStat");
            doctorStatsElement.appendChild(doctorStatElement);

            appendTextElement(document, doctorStatElement, "name", entry.getKey().getName());
            appendTextElement(document, doctorStatElement, "specialization", entry.getKey().getSpecialization());
            appendTextElement(document, doctorStatElement, "appointments", String.valueOf(entry.getValue()));
        }

        // Adaugă statisticile pentru servicii
        Element serviceStatsElement = document.createElement("serviceStatistics");
        reportElement.appendChild(serviceStatsElement);

        for (Map.Entry<MedicalService, Long> entry : report.getServiceStatistics().entrySet()) {
            Element serviceStatElement = document.createElement("serviceStat");
            serviceStatsElement.appendChild(serviceStatElement);

            appendTextElement(document, serviceStatElement, "name", entry.getKey().getName());
            appendTextElement(document, serviceStatElement, "price", String.valueOf(entry.getKey().getPrice()));
            appendTextElement(document, serviceStatElement, "duration", String.valueOf(entry.getKey().getDuration()));
            appendTextElement(document, serviceStatElement, "appointments", String.valueOf(entry.getValue()));
        }

        // Transformă documentul XML într-un fișier
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        File xmlFile = null;
        try {
            xmlFile = File.createTempFile("report_", ".xml");
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlFile;
    }

    /**
     * Utilitar pentru a adăuga un element text la un element părinte
     */
    private void appendTextElement(Document document, Element parent, String name, String value) {
        Element element = document.createElement(name);
        element.setTextContent(value);
        parent.appendChild(element);
    }
}