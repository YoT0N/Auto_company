package edu.ilkiv.auto_company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

import edu.ilkiv.auto_company.validation.DifferentPersonnel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DifferentPersonnel
public class RouteSheetDTO {

    private Long idRouteSheet;

    @NotBlank(message = "Номер маршруту не може бути порожнім")
    private String routeNumber;

    @NotBlank(message = "Державний номер автобуса не може бути порожнім")
    private String busCountryNumber;

    @NotNull(message = "Дата обов'язкова")
    @PastOrPresent(message = "Дата має бути у минулому або сьогодні")
    private LocalDate date;

    @NotNull(message = "Кількість рейсів обов'язкова")
    @Min(value = 1, message = "Мінімальна кількість рейсів - 1")
    private Integer trips;

    @NotBlank(message = "Табельний номер водія не може бути порожнім")
    private String driverTabelNumber;

    @NotBlank(message = "Табельний номер кондуктора не може бути порожнім")
    private String conductorTabelNumber;

    @NotNull(message = "Загальна кількість пасажирів обов'язкова")
    @PositiveOrZero(message = "Загальна кількість пасажирів не може бути від'ємною")
    private Integer totalPassengers;
}