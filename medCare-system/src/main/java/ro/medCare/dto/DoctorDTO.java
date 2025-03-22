package ro.medCare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

    private Long id;

    @NotBlank(message = "Numele este obligatoriu")
    @Size(min = 3, max = 100, message = "Numele trebuie să aibă între 3 și 100 de caractere")
    private String name;

    @NotBlank(message = "Specializarea este obligatorie")
    @Size(min = 3, max = 100, message = "Specializarea trebuie să aibă între 3 și 100 de caractere")
    private String specialization;

    @NotBlank(message = "Programul de lucru este obligatoriu")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]-([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Programul de lucru trebuie să fie în format HH:MM-HH:MM (ex: 09:00-17:00)")
    private String workHours;
}