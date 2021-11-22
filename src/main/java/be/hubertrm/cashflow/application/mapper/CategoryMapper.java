package be.hubertrm.cashflow.application.mapper;

import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.application.dto.CategoryDto;
import org.mapstruct.Mapper;

/**
 * The Interface CategoryMapper provides the methods for mapping Category to CategoryDto and back.
 */
@Mapper
public interface CategoryMapper extends GenericMapper<Category, CategoryDto> {
}
