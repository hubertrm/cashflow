package be.hubertrm.cashflow.application.mapper;

import be.hubertrm.cashflow.domain.file.model.RecordEvaluated;
import be.hubertrm.cashflow.application.dto.RecordEvaluatedDto;
import org.mapstruct.Mapper;

/**
 * The abstract class RecordEvaluatedMapper provides the methods for mapping RecordEvaluated to RecordEvaluatedDto and back.
 */
@Mapper(componentModel = "spring")
public interface RecordEvaluatedMapper extends GenericMapper<RecordEvaluated, RecordEvaluatedDto> {
}
