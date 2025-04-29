package edu.ilkiv.auto_company.mappers;


import edu.ilkiv.auto_company.dto.PhysicalExaminationDTO;
import edu.ilkiv.auto_company.model.PhysicalExamination;
import org.springframework.stereotype.Component;

@Component
public class PhysicalExaminationMapper implements EntityMapper<PhysicalExaminationDTO, PhysicalExamination> {

    @Override
    public PhysicalExamination toEntity(PhysicalExaminationDTO dto) {
        if (dto == null) {
            return null;
        }

        PhysicalExamination examination = new PhysicalExamination();
        examination.setIdPhysicalExamination(dto.getIdPhysicalExamination());
        examination.setDate(dto.getDate());
        examination.setExaminationResult(dto.getExaminationResult());

        // Зв'язок з PersonalData повинен встановлюватись в сервісному шарі

        return examination;
    }

    @Override
    public PhysicalExaminationDTO toDto(PhysicalExamination entity) {
        if (entity == null) {
            return null;
        }

        PhysicalExaminationDTO dto = new PhysicalExaminationDTO();
        dto.setIdPhysicalExamination(entity.getIdPhysicalExamination());
        dto.setDate(entity.getDate());
        dto.setExaminationResult(entity.getExaminationResult());

        if (entity.getPersonnel() != null) {
            dto.setPersonnelTabelNumber(entity.getPersonnel().getTabelNumber());
        }

        return dto;
    }
}
