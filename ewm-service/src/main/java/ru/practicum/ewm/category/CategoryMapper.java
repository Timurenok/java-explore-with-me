package ru.practicum.ewm.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.model.dto.CategoryInDto;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @Mapping(target = "id", expression = "java(null)")
    Category mapToCategory(CategoryInDto categoryInDto);

    Category mapToCategoryFromDto(CategoryDto categoryDto);

    CategoryDto mapToCategoryDto(Category category);
}
