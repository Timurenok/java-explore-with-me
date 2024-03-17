package ru.practicum.ewm.category;

import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.model.dto.CategoryInDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryInDto CategoryInDto);

    CategoryDto update(Long categoryId, CategoryDto categoryDto);

    CategoryDto findById(Long categoryId);

    void remove(Long categoryId);

    List<CategoryDto> findCategories(Integer from, Integer size);
}
