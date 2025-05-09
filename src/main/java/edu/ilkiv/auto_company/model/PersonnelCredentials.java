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
    private String tabelNumber; // Не потрібно @Column, бо він буде такий самий як у PersonalData

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tabel_number")
    private PersonalData personalData;

    @Column(nullable = false, length = 50)
    private String position;

    @Column(nullable = false)
    private LocalDate dateOfEmployment;
}