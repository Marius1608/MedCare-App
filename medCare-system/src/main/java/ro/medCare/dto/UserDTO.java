package ro.medCare.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.medCare.model.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Numele este obligatoriu")
    @Size(min = 3, max = 100, message = "Numele trebuie să aibă între 3 și 100 de caractere")
    private String name;

    @NotBlank(message = "Numele de utilizator este obligatoriu")
    @Size(min = 3, max = 50, message = "Numele de utilizator trebuie să aibă între 3 și 50 de caractere")
    private String username;

    @NotBlank(message = "Parola este obligatorie")
    @Size(min = 6, message = "Parola trebuie să aibă cel puțin 6 caractere")
    private String password;

    private UserRole role;
}