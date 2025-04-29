package edu.ilkiv.auto_company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

import edu.ilkiv.auto_company.validation.ValidBus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidBus
public class BusDTO {

    @NotBlank(message = "Державний номер не може бути порожнім")
    @Size(max = 20, message = "Державний номер не може перевищувати 20 символів")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}[A-Z]{2}$", message = "Державний номер повинен бути у форматі: AA0000AA")
    private String countryNumber;

    @NotBlank(message = "Бортовий номер не може бути порожнім")
    private String boardingNumber;

    @NotBlank(message = "Марка не може бути порожньою")
    @Size(max = 50, message = "Марка не може перевищувати 50 символів")
    private String brand;

    @NotNull(message = "Кількість місць для пасажирів обов'язкова")
    @Min(value = 10, message = "Мінімальна кількість місць - 10")
    @Max(value = 100, message = "Максимальна кількість місць - 100")
    private Integer passengerCapacity;

    @NotNull(message = "Рік випуску обов'язковий")
    @Min(value = 1980, message = "Рік випуску має бути не раніше 1980")
    @Max(value = 2025, message = "Рік випуску має бути не пізніше поточного року")
    private Integer yearOfManufacture;

    @NotNull(message = "Пробіг обов'язковий")
    @PositiveOrZero(message = "Пробіг не може бути від'ємним")
    private Double mileage;

    @NotNull(message = "Дата отримання обов'язкова")
    @Past(message = "Дата отримання має бути у минулому")
    private LocalDate dateOfReceipt;

    private LocalDate writeoffDate;
}