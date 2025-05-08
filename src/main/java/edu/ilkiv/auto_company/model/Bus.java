package edu.ilkiv.auto_company.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @Column(name = "country_number", length = 20)
    private String countryNumber;  // Primary key - державний номер

    @Column(name = "boarding_number", nullable = false, unique = true)
    private String boardingNumber;  // бортовий номер

    @Column(name = "brand", nullable = false, length = 50)
    private String brand;  // марка

    @Column(name = "passenger_capacity", nullable = false)
    private Integer passengerCapacity;  // місць для пасажирів

    @Column(name = "year_of_manufacture", nullable = false)
    private Integer yearOfManufacture;  // рік випуску

    @Column(name = "mileage", nullable = false)
    private Double mileage;  // пробіг

    @Column(name = "date_of_receipt", nullable = false)
    private LocalDate dateOfReceipt;  // дата отримання

    @Column(name = "writeoff_date")
    private LocalDate writeoffDate;  // дата списання

//    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
//    private List<RouteSheet> routeSheets;
//
//    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
//    private List<TechnicalExamination> technicalExaminations;
}