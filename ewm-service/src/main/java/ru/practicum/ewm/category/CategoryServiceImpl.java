package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.model.dto.CategoryInDto;
import ru.practicum.ewm.exception.InvalidDataException;
import ru.practicum.ewm.exception.UnknownCategoryException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto save(CategoryInDto categoryInDto) {
        Category category = categoryMapper.mapToCategory(categoryInDto);
        return categoryMapper.mapToCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new UnknownCategoryException(
                String.format("Category with id %d does not exist", categoryId)));
        category.setName(categoryDto.getName());
        return categoryMapper.mapToCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto findById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new UnknownCategoryException(
                String.format("Category with id %d does not exist", categoryId)));
        return categoryMapper.mapToCategoryDto(category);
    }

    @Override
    public void remove(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new InvalidDataException(String.format("Category with id %d does not exist", categoryId));
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryDto> findCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).getContent().stream().map(categoryMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }
}
