package edu.ilkiv.auto_company.mappers;


import edu.ilkiv.auto_company.dto.RouteSheetDTO;
import edu.ilkiv.auto_company.model.RouteSheet;
import org.springframework.stereotype.Component;

@Component
public class RouteSheetMapper implements EntityMapper<RouteSheetDTO, RouteSheet> {

    @Override
    public RouteSheet toEntity(RouteSheetDTO dto) {
        if (dto == null) {
            return null;
        }

        RouteSheet routeSheet = new RouteSheet();
        routeSheet.setIdRouteSheet(dto.getIdRouteSheet());
        routeSheet.setDate(dto.getDate());
        routeSheet.setTrips(dto.getTrips());
        routeSheet.setTotalPassengers(dto.getTotalPassengers());

        // Зв'язки з іншими сутностями встановлюються в сервісному шарі

        return routeSheet;
    }

    @Override
    public RouteSheetDTO toDto(RouteSheet entity) {
        if (entity == null) {
            return null;
        }

        RouteSheetDTO dto = new RouteSheetDTO();
        dto.setIdRouteSheet(entity.getIdRouteSheet());
        dto.setDate(entity.getDate());
        dto.setTrips(entity.getTrips());
        dto.setTotalPassengers(entity.getTotalPassengers());

        if (entity.getRoute() != null) {
            dto.setRouteNumber(entity.getRoute().getRouteNumber());
        }

        if (entity.getBus() != null) {
            dto.setBusCountryNumber(entity.getBus().getCountryNumber());
        }

        if (entity.getDriver() != null) {
            dto.setDriverTabelNumber(entity.getDriver().getTabelNumber());
        }

        if (entity.getConductor() != null) {
            dto.setConductorTabelNumber(entity.getConductor().getTabelNumber());
        }

        return dto;
    }
}