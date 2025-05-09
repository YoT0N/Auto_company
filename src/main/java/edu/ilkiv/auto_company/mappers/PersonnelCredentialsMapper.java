package edu.ilkiv.auto_company.mappers;


import edu.ilkiv.auto_company.dto.PersonnelCredentialsDTO;
import edu.ilkiv.auto_company.model.PersonnelCredentials;
import org.springframework.stereotype.Component;

@Component
public class PersonnelCredentialsMapper implements EntityMapper<PersonnelCredentialsDTO, PersonnelCredentials> {

    @Override
    public PersonnelCredentials toEntity(PersonnelCredentialsDTO dto) {
        if (dto == null) {
            return null;
        }

        PersonnelCredentials credentials = new PersonnelCredentials();
        credentials.setTabelNumber(dto.getTabelNumber());
        credentials.setPosition(dto.getPosition());
        credentials.setDateOfEmployment(dto.getDateOfEmployment());

        // PersonalData встановлюється в сервісі
        return credentials;
    }

    @Override
    public PersonnelCredentialsDTO toDto(PersonnelCredentials entity) {
        if (entity == null) {
            return null;
        }

        PersonnelCredentialsDTO dto = new PersonnelCredentialsDTO();
        dto.setTabelNumber(entity.getTabelNumber());
        dto.setPosition(entity.getPosition());
        dto.setDateOfEmployment(entity.getDateOfEmployment());

        return dto;
    }
}
