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

    @NotBlank(message = "Табельний номер не може бути порожнім")
    @Size(max = 20, message = "Табельний номер не може перевищувати 20 символів")
    @Pattern(regexp = "^[A-Z]\\d{5}$", message = "Табельний номер повинен бути у форматі: A00000")
    private String tabelNumber;

    @NotBlank(message = "Посада не може бути порожньою")
    @Size(max = 50, message = "Посада не може перевищувати 50 символів")
    private String position;

    @NotNull(message = "Дата працевлаштування обов'язкова")
    @Past(message = "Дата працевлаштування має бути у минулому")
    private LocalDate dateOfEmployment;
}