package edu.ilkiv.auto_company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

import edu.ilkiv.auto_company.validation.ValidDriverAge;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDataDTO {

    @NotBlank(message = "Табельний номер не може бути порожнім")
    @Size(max = 20, message = "Табельний номер не може перевищувати 20 символів")
    @Pattern(regexp = "^[A-Z]\\d{5}$", message = "Табельний номер повинен бути у форматі: A00000")
    private String tabelNumber;

    @NotBlank(message = "Повне ім'я не може бути порожнім")
    @Size(max = 100, message = "Повне ім'я не може перевищувати 100 символів")
    private String fullName;

    @NotNull(message = "Дата народження обов'язкова")
    @Past(message = "Дата народження має бути у минулому")
    @ValidDriverAge(message = "Вік працівника має бути не менше 21 року для водіїв")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Стать не може бути порожньою")
    @Pattern(regexp = "^(чоловіча|жіноча)$", message = "Стать має бути 'чоловіча' або 'жіноча'")
    private String sex;

    @NotBlank(message = "Домашня адреса не може бути порожньою")
    private String homeAddress;

    @Pattern(regexp = "^(\\+380\\d{9})?$", message = "Домашній телефон має бути у форматі +380XXXXXXXXX або порожнім")
    private String homePhone;

    @NotBlank(message = "Номер телефону не може бути порожнім")
    @Pattern(regexp = "^\\+380\\d{9}$", message = "Номер телефону має бути у форматі +380XXXXXXXXX")
    private String phoneNumber;

}