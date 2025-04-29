package edu.ilkiv.auto_company.repository;


import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.model.PersonalData;
import edu.ilkiv.auto_company.model.Route;
import edu.ilkiv.auto_company.model.RouteSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RouteSheetRepository extends JpaRepository<RouteSheet, Long> {
    List<RouteSheet> findByRoute(Route route);
    List<RouteSheet> findByBus(Bus bus);
    List<RouteSheet> findByDriver(PersonalData driver);
    List<RouteSheet> findByConductor(PersonalData conductor);
    List<RouteSheet> findByDateBetween(LocalDate start, LocalDate end);
    List<RouteSheet> findByTotalPassengersGreaterThan(Integer passengers);
}