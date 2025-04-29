package edu.ilkiv.auto_company.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "physical_examination")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_physical_examination")
    private Long idPhysicalExamination;  // ідентифікатор медичного огляду

    @ManyToOne
    @JoinColumn(name = "tabel_number", nullable = false)
    private PersonalData personnel;  // табельний номер

    @Column(name = "date", nullable = false)
    private LocalDate date;  // дата

    @Column(name = "examination_result", nullable = false)
    private String examinationResult;  // результат огляду
}