package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.model.Bus;
import edu.ilkiv.auto_company.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    private final BusRepository busRepository;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Optional<Bus> getBusById(String countryNumber) {
        return busRepository.findById(countryNumber);
    }

    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }

    public void deleteBus(String countryNumber) {
        busRepository.deleteById(countryNumber);
    }

    public List<Bus> findByBrand(String brand) {
        return busRepository.findByBrand(brand);
    }

    public List<Bus> findByYearOfManufacture(Integer year) {
        return busRepository.findByYearOfManufacture(year);
    }

    public List<Bus> findByDateOfReceiptBetween(LocalDate start, LocalDate end) {
        return busRepository.findByDateOfReceiptBetween(start, end);
    }

    public List<Bus> findActiveBuses() {
        return busRepository.findByWriteoffDateIsNull();
    }

    public boolean existsById(String countryNumber) {
        return busRepository.existsById(countryNumber);
    }

    public Bus updateBus(String countryNumber, Bus busDetails) {
        Bus bus = busRepository.findById(countryNumber)
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + countryNumber));

        bus.setBoardingNumber(busDetails.getBoardingNumber());
        bus.setBrand(busDetails.getBrand());
        bus.setPassengerCapacity(busDetails.getPassengerCapacity());
        bus.setYearOfManufacture(busDetails.getYearOfManufacture());
        bus.setMileage(busDetails.getMileage());
        bus.setDateOfReceipt(busDetails.getDateOfReceipt());
        bus.setWriteoffDate(busDetails.getWriteoffDate());

        return busRepository.save(bus);
    }
}