package edu.ilkiv.auto_company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalExaminationDTO {

    private Long idPhysicalExamination;

    @NotBlank(message = "Табельний номер не може бути порожнім")
    private String personnelTabelNumber;

    @NotNull(message = "Дата обов'язкова")
    @PastOrPresent(message = "Дата має бути у минулому або сьогодні")
    private LocalDate date;

    @NotBlank(message = "Результат огляду не може бути порожнім")
    @Pattern(regexp = "^(придатний|непридатний|умовно придатний)$",
            message = "Результат огляду має бути один з: придатний, непридатний, умовно придатний")
    private String examinationResult;
}