package edu.ilkiv.auto_company.repository;

import edu.ilkiv.auto_company.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, String> {
    List<Bus> findByBrand(String brand);
    List<Bus> findByYearOfManufacture(Integer year);
    List<Bus> findByDateOfReceiptBetween(LocalDate start, LocalDate end);
    List<Bus> findByWriteoffDateIsNull();
}