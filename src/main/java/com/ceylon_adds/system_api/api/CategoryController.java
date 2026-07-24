package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.CategoryRequestDTO;
import com.ceylon_adds.system_api.dto.response.CategoryResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateCategoryDTO;
import com.ceylon_adds.system_api.service.CategoryService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create category", description = "Create a new category")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PostMapping
    public ResponseEntity<StandardResponseDTO> createCategory(@RequestBody CategoryRequestDTO dto) {
        categoryService.create(dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(201)
                        .message("Category created successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Update category", description = "Update category details by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<StandardResponseDTO> updateCategory(
            @PathVariable UUID categoryId,
            @RequestBody CategoryRequestDTO dto) {

        categoryService.update(categoryId, dto);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Category updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Delete category", description = "Delete category by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<StandardResponseDTO> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Category deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Change active status", description = "Activate or deactivate category")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOST')")
    @PatchMapping("/{categoryId}/status")
    public ResponseEntity<StandardResponseDTO> changeCategoryStatus(
            @PathVariable UUID categoryId,
            @RequestParam boolean active) {

        categoryService.changeStatus(categoryId, active);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Category status updated successfully")
                        .data(null)
                        .build()
        );
    }

    @Operation(summary = "Get category by ID", description = "Retrieve category details by ID")
    @GetMapping("/{categoryId}")
    public ResponseEntity<StandardResponseDTO> getCategoryById(@PathVariable UUID categoryId) {
        CategoryResponseDTO response = categoryService.findById(categoryId);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Category retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Search categories", description = "Search categories by name")
    @GetMapping("/search")
    public ResponseEntity<StandardResponseDTO> searchCategories(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginateCategoryDTO response = categoryService.search(searchText, page, size);
        return ResponseEntity.ok(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Categories retrieved successfully")
                        .data(response)
                        .build()
        );
    }
}
