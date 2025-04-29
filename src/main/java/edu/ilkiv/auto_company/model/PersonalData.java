package edu.ilkiv.auto_company.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "personal_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalData {

    @Id
    @Column(name = "tabel_number", length = 20)
    private String tabelNumber;  // Primary key - табельний номер

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;  // повне ім'я

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;  // дата народження

    @Column(name = "sex", nullable = false, length = 10)
    private String sex;  // стать

    @Column(name = "home_address", nullable = false)
    private String homeAddress;  // домашній адрес

    @Column(name = "home_phone", length = 20)
    private String homePhone;  // домашній телефон

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;  // робочий телефон

    @OneToOne(mappedBy = "personalData", cascade = CascadeType.ALL)
    private PersonnelCredentials personnelCredentials;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<RouteSheet> driverRouteSheets;

    @OneToMany(mappedBy = "conductor", cascade = CascadeType.ALL)
    private List<RouteSheet> conductorRouteSheets;

    @OneToMany(mappedBy = "personnel", cascade = CascadeType.ALL)
    private List<PhysicalExamination> physicalExaminations;
}