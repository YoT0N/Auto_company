//package edu.ilkiv.auto_company.service;
//
//import edu.ilkiv.auto_company.dto.KeysDTO;
//import edu.ilkiv.auto_company.mappers.KeysMapper;
//import edu.ilkiv.auto_company.model.Keys;
//import edu.ilkiv.auto_company.repository.KeysRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import jakarta.validation.Valid;
//import org.springframework.validation.annotation.Validated;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Validated
//public class KeysService {
//
//    private final KeysRepository keysRepository;
//    private final KeysMapper keysMapper;
//
//    @Autowired
//    public KeysService(KeysRepository keysRepository, KeysMapper keysMapper) {
//        this.keysRepository = keysRepository;
//        this.keysMapper = keysMapper;
//    }
//
//    public List<KeysDTO> getAllKeys() {
//        return keysRepository.findAll().stream()
//                .map(keysMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public Optional<KeysDTO> getKeysById(Long id) {
//        return keysRepository.findById(id)
//                .map(keysMapper::toDto);
//    }
//
//    public Optional<KeysDTO> findByLogin(String login) {
//        return keysRepository.findByLogin(login)
//                .map(keysMapper::toDto);
//    }
//
//    public KeysDTO saveKeys(@Valid KeysDTO keysDTO) {
//        Keys keys = keysMapper.toEntity(keysDTO);
//        Keys savedKeys = keysRepository.save(keys);
//        return keysMapper.toDto(savedKeys);
//    }
//
//    public void deleteKeys(Long id) {
//        keysRepository.deleteById(id);
//    }
//
//    public List<KeysDTO> findByRole(String role) {
//        return keysRepository.findByRole(role).stream()
//                .map(keysMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public List<KeysDTO> findByTablenameEquals(String tablename) {
//        return keysRepository.findByTablenameEquals(tablename).stream()
//                .map(keysMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public boolean existsById(Long id) {
//        return keysRepository.existsById(id);
//    }
//
//    public KeysDTO updateKeys(Long id, @Valid KeysDTO keysDTO) {
//        if (!keysRepository.existsById(id)) {
//            throw new RuntimeException("Keys not found with id: " + id);
//        }
//
//        // Ensure the DTO's ID matches the path parameter
//        keysDTO.setIdKeys(id);
//
//        Keys keys = keysMapper.toEntity(keysDTO);
//        Keys updatedKeys = keysRepository.save(keys);
//        return keysMapper.toDto(updatedKeys);
//    }
//}