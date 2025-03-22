package ro.medCare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalServiceDTO {

    private Long id;

    @NotBlank(message = "Numele serviciului este obligatoriu")
    @Size(min = 3, max = 100, message = "Numele serviciului trebuie să aibă între 3 și 100 de caractere")
    private String name;

    @NotNull(message = "Prețul este obligatoriu")
    @Min(value = 0, message = "Prețul nu poate fi negativ")
    private Double price;

    @NotNull(message = "Durata este obligatorie")
    @Min(value = 5, message = "Durata minimă este de 5 minute")
    private Integer duration;
}