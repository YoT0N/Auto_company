package edu.ilkiv.auto_company.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "personnel_credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelCredentials {

    @Id
    @Column(name = "tabel_number", length = 20)
    private String tabelNumber;  // Primary key - табельний номер

    @OneToOne
    @MapsId
    @JoinColumn(name = "tabel_number_fr")
    private PersonalData personalData;

    @Column(name = "position", nullable = false, length = 50)
    private String position;  // посада

    @Column(name = "date_of_employment", nullable = false)
    private LocalDate dateOfEmployment;  // дата працевлаштування
}