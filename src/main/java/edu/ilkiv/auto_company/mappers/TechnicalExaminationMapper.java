package edu.ilkiv.auto_company.mappers;


import edu.ilkiv.auto_company.dto.TechnicalExaminationDTO;
import edu.ilkiv.auto_company.model.TechnicalExamination;
import org.springframework.stereotype.Component;

@Component
public class TechnicalExaminationMapper implements EntityMapper<TechnicalExaminationDTO, TechnicalExamination> {

    @Override
    public TechnicalExamination toEntity(TechnicalExaminationDTO dto) {
        if (dto == null) {
            return null;
        }

        TechnicalExamination examination = new TechnicalExamination();
        examination.setIdTechnicalExamination(dto.getIdTechnicalExamination());
        examination.setDate(dto.getDate());
        examination.setExaminationResult(dto.getExaminationResult());
        examination.setSentForRepair(dto.getSentForRepair());
        examination.setRepairPrice(dto.getRepairPrice());

        // Зв'язок з Bus встановлюється в сервісному шарі

        return examination;
    }

    @Override
    public TechnicalExaminationDTO toDto(TechnicalExamination entity) {
        if (entity == null) {
            return null;
        }

        TechnicalExaminationDTO dto = new TechnicalExaminationDTO();
        dto.setIdTechnicalExamination(entity.getIdTechnicalExamination());
        dto.setDate(entity.getDate());
        dto.setExaminationResult(entity.getExaminationResult());
        dto.setSentForRepair(entity.getSentForRepair());
        dto.setRepairPrice(entity.getRepairPrice());

        if (entity.getBus() != null) {
            dto.setBusCountryNumber(entity.getBus().getCountryNumber());
        }

        return dto;
    }
}
