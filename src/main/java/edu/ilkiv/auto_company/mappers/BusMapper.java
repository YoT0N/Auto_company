package edu.ilkiv.auto_company.mappers;

import edu.ilkiv.auto_company.dto.BusDTO;
import edu.ilkiv.auto_company.model.Bus;
import org.springframework.stereotype.Component;

@Component
public class BusMapper implements EntityMapper<BusDTO, Bus> {

    @Override
    public Bus toEntity(BusDTO dto) {
        if (dto == null) {
            return null;
        }

        Bus bus = new Bus();
        bus.setCountryNumber(dto.getCountryNumber());
        bus.setBoardingNumber(dto.getBoardingNumber());
        bus.setBrand(dto.getBrand());
        bus.setPassengerCapacity(dto.getPassengerCapacity());
        bus.setYearOfManufacture(dto.getYearOfManufacture());
        bus.setMileage(dto.getMileage());
        bus.setDateOfReceipt(dto.getDateOfReceipt());
        bus.setWriteoffDate(dto.getWriteoffDate());

        return bus;
    }

    @Override
    public BusDTO toDto(Bus entity) {
        if (entity == null) {
            return null;
        }

        BusDTO dto = new BusDTO();
        dto.setCountryNumber(entity.getCountryNumber());
        dto.setBoardingNumber(entity.getBoardingNumber());
        dto.setBrand(entity.getBrand());
        dto.setPassengerCapacity(entity.getPassengerCapacity());
        dto.setYearOfManufacture(entity.getYearOfManufacture());
        dto.setMileage(entity.getMileage());
        dto.setDateOfReceipt(entity.getDateOfReceipt());
        dto.setWriteoffDate(entity.getWriteoffDate());

        return dto;
    }
}