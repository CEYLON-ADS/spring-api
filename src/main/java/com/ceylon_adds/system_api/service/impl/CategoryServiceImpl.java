package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.CategoryRequestDTO;
import com.ceylon_adds.system_api.dto.response.CategoryResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateCategoryDTO;
import com.ceylon_adds.system_api.entity.Category;
import com.ceylon_adds.system_api.repository.CategoryRepository;
import com.ceylon_adds.system_api.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void create(CategoryRequestDTO categoryRequestDTO) {
        Category category = Category.builder()
                .categoryName(categoryRequestDTO.getCategoryName())
                .activeStatus(true)
                .build();

        categoryRepository.save(category);
    }

    @Override
    public void update(UUID categoryId, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        category.setCategoryName(categoryRequestDTO.getCategoryName());

        categoryRepository.save(category);
    }

    @Override
    public void delete(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Category not found with id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public void changeStatus(UUID categoryId, boolean status) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        category.setActiveStatus(status);
        categoryRepository.save(category);
    }

    @Override
    public CategoryResponseDTO findById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        return mapToResponseDTO(category);
    }

    @Override
    public PaginateCategoryDTO search(String searchText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("categoryName").ascending());

        Page<Category> categoryPage;
        if (searchText == null || searchText.trim().isEmpty()) {
            categoryPage = categoryRepository.findAll(pageable);
        } else {
            categoryPage = categoryRepository.searchCategories(searchText, pageable);
        }

        return PaginateCategoryDTO.builder()
                .count(categoryPage.getTotalElements())
                .dataList(categoryPage.getContent().stream()
                        .map(this::mapToResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .propertyId(category.getPropertyId())
                .categoryName(category.getCategoryName())
                .activeStatus(category.getActiveStatus())
                .build();
    }
}
