package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.model.Keys;
import edu.ilkiv.auto_company.service.KeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
public class KeysController {

    private final KeysService keysService;

    @Autowired
    public KeysController(KeysService keysService) {
        this.keysService = keysService;
    }

    // GET all keys
    @GetMapping
    public ResponseEntity<List<Keys>> getAllKeys() {
        List<Keys> keys = keysService.getAllKeys();
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }

    // GET keys by id
    @GetMapping("/{id}")
    public ResponseEntity<Keys> getKeysById(@PathVariable Long id) {
        return keysService.getKeysById(id)
                .map(keys -> new ResponseEntity<>(keys, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET keys by login
    @GetMapping("/login/{login}")
    public ResponseEntity<Keys> getKeysByLogin(@PathVariable String login) {
        return keysService.findByLogin(login)
                .map(keys -> new ResponseEntity<>(keys, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new keys
    @PostMapping
    public ResponseEntity<Keys> createKeys(@RequestBody Keys keys) {
        Keys savedKeys = keysService.saveKeys(keys);
        return new ResponseEntity<>(savedKeys, HttpStatus.CREATED);
    }

    // PUT update keys
    @PutMapping("/{id}")
    public ResponseEntity<Keys> updateKeys(@PathVariable Long id, @RequestBody Keys keys) {
        if (!keysService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        keys.setIdKeys(id); // Ensure the ID matches
        Keys updatedKeys = keysService.updateKeys(id, keys);
        return new ResponseEntity<>(updatedKeys, HttpStatus.OK);
    }

    // DELETE keys
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeys(@PathVariable Long id) {
        if (!keysService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        keysService.deleteKeys(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET keys by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Keys>> getKeysByRole(@PathVariable String role) {
        List<Keys> keys = keysService.findByRole(role);
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }

    // GET keys by tablename
    @GetMapping("/tablename/{tablename}")
    public ResponseEntity<List<Keys>> getKeysByTablename(@PathVariable String tablename) {
        List<Keys> keys = keysService.findByTablenameEquals(tablename);
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }
}