package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.CategoryRequestDTO;
import com.ceylon_adds.system_api.dto.response.CategoryResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateCategoryDTO;

import java.util.UUID;

public interface CategoryService {

    void create(CategoryRequestDTO categoryRequestDTO);

    void update(UUID categoryId, CategoryRequestDTO categoryRequestDTO);

    void delete(UUID categoryId);

    void changeStatus(UUID categoryId, boolean status);

    CategoryResponseDTO findById(UUID categoryId);

    PaginateCategoryDTO search(String searchText, int page, int pageSize);

}
