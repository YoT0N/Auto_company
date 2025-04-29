package edu.ilkiv.auto_company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalExaminationDTO {

    private Long idTechnicalExamination;

    @NotBlank(message = "Державний номер автобуса не може бути порожнім")
    private String busCountryNumber;

    @NotNull(message = "Дата обов'язкова")
    @PastOrPresent(message = "Дата має бути у минулому або сьогодні")
    private LocalDate date;

    @NotBlank(message = "Результат огляду не може бути порожнім")
    @Pattern(regexp = "^(справний|несправний|потребує уваги)$",
            message = "Результат огляду має бути один з: справний, несправний, потребує уваги")
    private String examinationResult;

    @NotNull(message = "Стан ремонту обов'язковий")
    private Boolean sentForRepair;

    @DecimalMin(value = "0.0", message = "Ціна ремонту не може бути від'ємною")
    private Double repairPrice;
}