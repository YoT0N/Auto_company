package edu.ilkiv.auto_company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {

    @NotBlank(message = "Номер маршруту не може бути порожнім")
    @Pattern(regexp = "^\\d+[A-Z]?$", message = "Номер маршруту повинен бути у форматі: цифра(и) або цифра(и) з літерою")
    private String routeNumber;

    @NotBlank(message = "Назва маршруту не може бути порожньою")
    private String routeName;

    @NotNull(message = "Довжина маршруту обов'язкова")
    @Positive(message = "Довжина маршруту має бути більше 0")
    private Double routeLength;

    @NotNull(message = "Середній час рейсу обов'язковий")
    @Min(value = 10, message = "Мінімальний середній час рейсу - 10 хвилин")
    private Integer averageTime;

    @NotNull(message = "Заплановані рейси за зміну обов'язкові")
    @Min(value = 1, message = "Мінімальна кількість запланованих рейсів - 1")
    private Integer plannedTripsPerShift;
}