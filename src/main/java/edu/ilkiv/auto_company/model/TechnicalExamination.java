package edu.ilkiv.auto_company.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "technical_examination")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_technical_examination")
    private Long idTechnicalExamination;  // ідентифікатор технічного огляду

    @ManyToOne
    @JoinColumn(name = "bus_country_number", nullable = false)
    private Bus bus;  // державний номер автобуса

    @Column(name = "date", nullable = false)
    private LocalDate date;  // дата

    @Column(name = "examination_result", nullable = false)
    private String examinationResult;  // результат огляду

    @Column(name = "sent_for_repair", nullable = false)
    private Boolean sentForRepair;  // відправлений на ремонт чи ні

    @Column(name = "repair_price")
    private Double repairPrice;  // ціна ремонту
}