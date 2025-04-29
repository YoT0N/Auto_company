package edu.ilkiv.auto_company.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "route")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @Column(name = "route_number")
    private String routeNumber;  // Primary key - номер маршруту

    @Column(name = "route_name", nullable = false)
    private String routeName;  // назва маршруту

    @Column(name = "route_length", nullable = false)
    private Double routeLength;  // довжина маршруту

    @Column(name = "average_time", nullable = false)
    private Integer averageTime;  // середній час рейсу (у хвилинах)

    @Column(name = "planned_trips_per_shift", nullable = false)
    private Integer plannedTripsPerShift;  // заплановані рейси за зміну

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<RouteSheet> routeSheets;
}