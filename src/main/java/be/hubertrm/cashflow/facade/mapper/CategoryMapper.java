package be.hubertrm.cashflow.facade.mapper;

import be.hubertrm.cashflow.domain.model.Category;
import be.hubertrm.cashflow.facade.dto.CategoryDto;
import org.mapstruct.Mapper;

/**
 * The Interface CategoryMapper provides the methods for mapping Category to CategoryDto and back.
 */
@Mapper
public interface CategoryMapper extends GenericMapper<Category, CategoryDto> {
}
