package ro.medCare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.medCare.exception.ResourceNotFoundException;
import ro.medCare.exception.ValidationException;
import ro.medCare.model.*;
import ro.medCare.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorService doctorService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
    }

    public Appointment createAppointment(Appointment appointment) {
        // Verifică disponibilitatea medicului
        if (!doctorService.checkAvailability(
                appointment.getDoctor().getId(),
                appointment.getDateTime(),
                appointment.getService().getDuration())) {
            throw new ValidationException("Medicul nu este disponibil în intervalul specificat!");
        }

        // Setează statusul inițial la NEW
        appointment.setStatus(AppointmentStatus.NEW);

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Appointment appointment) {
        if (!appointmentRepository.existsById(appointment.getId())) {
            throw new ResourceNotFoundException("Programarea nu a fost găsită!");
        }

        Appointment existingAppointment = appointmentRepository.findById(appointment.getId()).orElse(null);

        // Dacă se modifică data/ora sau medicul sau serviciul, verifică disponibilitatea
        if (existingAppointment != null &&
                ((!existingAppointment.getDateTime().equals(appointment.getDateTime())) ||
                        (!existingAppointment.getDoctor().getId().equals(appointment.getDoctor().getId())) ||
                        (!existingAppointment.getService().getId().equals(appointment.getService().getId())))) {

            if (!doctorService.checkAvailability(
                    appointment.getDoctor().getId(),
                    appointment.getDateTime(),
                    appointment.getService().getDuration())) {
                throw new ValidationException("Medicul nu este disponibil în intervalul specificat!");
            }
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programarea nu a fost găsită!"));

        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Programarea nu a fost găsită!");
        }
        appointmentRepository.deleteById(id);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programarea nu a fost găsită!"));
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDateTimeBetween(start, end);
    }

    public Map<Doctor, Long> getMostRequestedDoctors() {
        List<Appointment> appointments = appointmentRepository.findAll();

        // Groupează programările după medic și numără
        Map<Doctor, Long> doctorCounts = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getDoctor, Collectors.counting()));

        // Sortează după numărul de programări (descrescător)
        return doctorCounts.entrySet().stream()
                .sorted(Map.Entry.<Doctor, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<MedicalService, Long> getMostRequestedServices() {
        List<Appointment> appointments = appointmentRepository.findAll();

        // Groupează programările după serviciu și numără
        Map<MedicalService, Long> serviceCounts = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getService, Collectors.counting()));

        // Sortează după numărul de programări (descrescător)
        return serviceCounts.entrySet().stream()
                .sorted(Map.Entry.<MedicalService, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}