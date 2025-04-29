package edu.ilkiv.auto_company.mappers;

import edu.ilkiv.auto_company.dto.KeysDTO;
import edu.ilkiv.auto_company.model.Keys;
import org.springframework.stereotype.Component;

@Component
public class KeysMapper implements EntityMapper<KeysDTO, Keys> {

    @Override
    public Keys toEntity(KeysDTO dto) {
        if (dto == null) {
            return null;
        }

        Keys keys = new Keys();
        keys.setIdKeys(dto.getIdKeys());
        keys.setLogin(dto.getLogin());
        keys.setPassword(dto.getPassword()); // В реальному проекті тут мав би бути механізм хешування паролів
        keys.setRole(dto.getRole());
        keys.setTablename(dto.getTablename());
        keys.setFields(dto.getFields());

        return keys;
    }

    @Override
    public KeysDTO toDto(Keys entity) {
        if (entity == null) {
            return null;
        }

        KeysDTO dto = new KeysDTO();
        dto.setIdKeys(entity.getIdKeys());
        dto.setLogin(entity.getLogin());
        // Пароль не передаємо в DTO з міркувань безпеки
        dto.setPassword("");
        dto.setRole(entity.getRole());
        dto.setTablename(entity.getTablename());
        dto.setFields(entity.getFields());

        return dto;
    }
}