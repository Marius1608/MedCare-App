package ro.medCare.service;

import jakarta.persistence.spi.TransformerException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ro.medCare.dto.ReportDTO;
import ro.medCare.model.Appointment;
import ro.medCare.model.Doctor;
import ro.medCare.model.MedicalService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class XMLReportExporter implements ReportExporter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public File export(ReportDTO report) throws ParserConfigurationException, IOException, TransformerException, TransformerConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element reportElement = document.createElement("report");
        document.appendChild(reportElement);

        Element metadataElement = document.createElement("metadata");
        reportElement.appendChild(metadataElement);

        Element startDateElement = document.createElement("startDate");
        startDateElement.setTextContent(report.getStartDate().format(DATE_FORMATTER));
        metadataElement.appendChild(startDateElement);

        Element endDateElement = document.createElement("endDate");
        endDateElement.setTextContent(report.getEndDate().format(DATE_FORMATTER));
        metadataElement.appendChild(endDateElement);

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

        Element doctorStatsElement = document.createElement("doctorStatistics");
        reportElement.appendChild(doctorStatsElement);

        for (Map.Entry<Doctor, Long> entry : report.getDoctorStatistics().entrySet()) {
            Element doctorStatElement = document.createElement("doctorStat");
            doctorStatsElement.appendChild(doctorStatElement);

            appendTextElement(document, doctorStatElement, "name", entry.getKey().getName());
            appendTextElement(document, doctorStatElement, "specialization", entry.getKey().getSpecialization());
            appendTextElement(document, doctorStatElement, "appointments", String.valueOf(entry.getValue()));
        }

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

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        File xmlFile = null;
        try {
            xmlFile = File.createTempFile("report_", ".xml");
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
        } catch (IOException | javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }

        return xmlFile;
    }

    private void appendTextElement(Document document, Element parent, String name, String value) {
        Element element = document.createElement(name);
        element.setTextContent(value);
        parent.appendChild(element);
    }
}