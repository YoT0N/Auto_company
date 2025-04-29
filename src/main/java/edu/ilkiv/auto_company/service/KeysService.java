package edu.ilkiv.auto_company.service;

import edu.ilkiv.auto_company.model.Keys;
import edu.ilkiv.auto_company.repository.KeysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeysService {

    private final KeysRepository keysRepository;

    @Autowired
    public KeysService(KeysRepository keysRepository) {
        this.keysRepository = keysRepository;
    }

    public List<Keys> getAllKeys() {
        return keysRepository.findAll();
    }

    public Optional<Keys> getKeysById(Long id) {
        return keysRepository.findById(id);
    }

    public Optional<Keys> findByLogin(String login) {
        return keysRepository.findByLogin(login);
    }

    public Keys saveKeys(Keys keys) {
        return keysRepository.save(keys);
    }

    public void deleteKeys(Long id) {
        keysRepository.deleteById(id);
    }

    public List<Keys> findByRole(String role) {
        return keysRepository.findByRole(role);
    }

    public List<Keys> findByTablenameEquals(String tablename) {
        return keysRepository.findByTablenameEquals(tablename);
    }

    public boolean existsById(Long id) {
        return keysRepository.existsById(id);
    }

    public Keys updateKeys(Long id, Keys keysDetails) {
        Keys keys = keysRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Keys not found with id: " + id));

        keys.setLogin(keysDetails.getLogin());
        keys.setPassword(keysDetails.getPassword());
        keys.setRole(keysDetails.getRole());
        keys.setTablename(keysDetails.getTablename());
        keys.setFields(keysDetails.getFields());

        return keysRepository.save(keys);
    }
}