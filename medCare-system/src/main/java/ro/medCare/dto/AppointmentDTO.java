package ro.medCare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.medCare.model.AppointmentStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;

    @NotBlank(message = "Numele pacientului este obligatoriu")
    @Size(min = 3, max = 100, message = "Numele pacientului trebuie să aibă între 3 și 100 de caractere")
    private String patientName;

    @NotNull(message = "Medicul este obligatoriu")
    private Long doctorId;

    @NotNull(message = "Data și ora sunt obligatorii")
    @Future(message = "Data și ora trebuie să fie în viitor")
    private LocalDateTime dateTime;

    @NotNull(message = "Serviciul medical este obligatoriu")
    private Long serviceId;

    private AppointmentStatus status;
}