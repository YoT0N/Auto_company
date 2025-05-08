//package edu.ilkiv.auto_company.repository;
//
//import edu.ilkiv.auto_company.model.Keys;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface KeysRepository extends JpaRepository<Keys, Long> {
//    Optional<Keys> findByLogin(String login);
//    List<Keys> findByRole(String role);
//    List<Keys> findByTablenameEquals(String tablename);
//}