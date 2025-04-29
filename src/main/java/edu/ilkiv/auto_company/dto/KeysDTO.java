package edu.ilkiv.auto_company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeysDTO {

    private Long idKeys;

    @NotBlank(message = "Логін не може бути порожнім")
    @Size(min = 4, max = 50, message = "Логін повинен бути від 4 до 50 символів")
    private String login;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 8, max = 255, message = "Пароль повинен бути від 8 до 255 символів")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Пароль повинен містити мінімум одну цифру, одну малу літеру, одну велику літеру та один спеціальний символ")
    private String password;

    @NotBlank(message = "Роль не може бути порожньою")
    @Pattern(regexp = "^(ADMIN|USER|MANAGER)$", message = "Роль повинна бути однією з: ADMIN, USER, MANAGER")
    private String role;

    @Size(max = 50, message = "Назва таблиці не може перевищувати 50 символів")
    private String tablename;

    @Size(max = 255, message = "Поля не можуть перевищувати 255 символів")
    private String fields;
}