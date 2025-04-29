package edu.ilkiv.auto_company.mappers;


import edu.ilkiv.auto_company.dto.RouteDTO;
import edu.ilkiv.auto_company.model.Route;
import org.springframework.stereotype.Component;

@Component
public class RouteMapper implements EntityMapper<RouteDTO, Route> {

    @Override
    public Route toEntity(RouteDTO dto) {
        if (dto == null) {
            return null;
        }

        Route route = new Route();
        route.setRouteNumber(dto.getRouteNumber());
        route.setRouteName(dto.getRouteName());
        route.setRouteLength(dto.getRouteLength());
        route.setAverageTime(dto.getAverageTime());
        route.setPlannedTripsPerShift(dto.getPlannedTripsPerShift());

        return route;
    }

    @Override
    public RouteDTO toDto(Route entity) {
        if (entity == null) {
            return null;
        }

        RouteDTO dto = new RouteDTO();
        dto.setRouteNumber(entity.getRouteNumber());
        dto.setRouteName(entity.getRouteName());
        dto.setRouteLength(entity.getRouteLength());
        dto.setAverageTime(entity.getAverageTime());
        dto.setPlannedTripsPerShift(entity.getPlannedTripsPerShift());

        return dto;
    }
}