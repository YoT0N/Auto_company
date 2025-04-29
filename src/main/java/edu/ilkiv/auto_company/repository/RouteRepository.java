package edu.ilkiv.auto_company.repository;

import edu.ilkiv.auto_company.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    List<Route> findByRouteNameContaining(String keyword);
    List<Route> findByRouteLengthGreaterThan(Double length);
    List<Route> findByAverageTimeLessThan(Integer time);
}