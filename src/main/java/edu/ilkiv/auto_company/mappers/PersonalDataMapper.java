package edu.ilkiv.auto_company.mappers;


import edu.ilkiv.auto_company.dto.PersonalDataDTO;
import edu.ilkiv.auto_company.model.PersonalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonalDataMapper implements EntityMapper<PersonalDataDTO, PersonalData> {

    private final PersonnelCredentialsMapper personnelCredentialsMapper;

    @Autowired
    public PersonalDataMapper(PersonnelCredentialsMapper personnelCredentialsMapper) {
        this.personnelCredentialsMapper = personnelCredentialsMapper;
    }

    @Override
    public PersonalData toEntity(PersonalDataDTO dto) {
        if (dto == null) {
            return null;
        }

        PersonalData personalData = new PersonalData();
        personalData.setTabelNumber(dto.getTabelNumber());
        personalData.setFullName(dto.getFullName());
        personalData.setDateOfBirth(dto.getDateOfBirth());
        personalData.setSex(dto.getSex());
        personalData.setHomeAddress(dto.getHomeAddress());
        personalData.setHomePhone(dto.getHomePhone());
        personalData.setPhoneNumber(dto.getPhoneNumber());

        // Зв'язані сутності не встановлюємо, щоб уникнути циклічної залежності

        return personalData;
    }

    @Override
    public PersonalDataDTO toDto(PersonalData entity) {
        if (entity == null) {
            return null;
        }

        PersonalDataDTO dto = new PersonalDataDTO();
        dto.setTabelNumber(entity.getTabelNumber());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setSex(entity.getSex());
        dto.setHomeAddress(entity.getHomeAddress());
        dto.setHomePhone(entity.getHomePhone());
        dto.setPhoneNumber(entity.getPhoneNumber());

        // Додаємо credentials якщо вони є
        if (entity.getPersonnelCredentials() != null) {
            dto.setPersonnelCredentials(personnelCredentialsMapper.toDto(entity.getPersonnelCredentials()));
        }

        return dto;
    }
}