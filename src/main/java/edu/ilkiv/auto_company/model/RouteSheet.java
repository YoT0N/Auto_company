package edu.ilkiv.auto_company.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "route_sheet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_route_sheet")
    private Long idRouteSheet;  // ідентифікатор маршрутного листа

    @ManyToOne
    @JoinColumn(name = "route_number", nullable = false)
    private Route route;  // номер маршруту

    @ManyToOne
    @JoinColumn(name = "bus_country_number", nullable = false)
    private Bus bus;  // державний номер автобуса

    @Column(name = "date", nullable = false)
    private LocalDate date;  // дата

    @Column(name = "trips", nullable = false)
    private Integer trips;  // рейси

    @ManyToOne
    @JoinColumn(name = "driver_tabel_number", nullable = false)
    private PersonalData driver;  // табельний номер водія

    @ManyToOne
    @JoinColumn(name = "conductor_tabel_number", nullable = false)
    private PersonalData conductor;  // табельний номер кондуктора

    @Column(name = "total_passengers", nullable = false)
    private Integer totalPassengers;  // кількість пасажирів за зміну
}
