package edu.ilkiv.auto_company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelCredentialsDTO {
    @NotBlank
    @Pattern(regexp = "^[A-Z]\\d{5}$")
    private String tabelNumber;

    @NotBlank
    private String position;

    @NotNull
    private LocalDate dateOfEmployment;
}