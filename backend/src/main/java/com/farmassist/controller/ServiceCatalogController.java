package com.farmassist.controller;

import com.farmassist.dto.ApiResponse;
import com.farmassist.dto.ServiceCategoryDto;
import com.farmassist.dto.ServiceItemDto;
import com.farmassist.service.ServiceCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class ServiceCatalogController {

    private final ServiceCatalogService catalogService;

    public ServiceCatalogController(ServiceCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<ServiceCategoryDto>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getAllActiveCategoriesWithItems()));
    }

    @GetMapping("/categories/all")
    public ResponseEntity<ApiResponse<List<ServiceCategoryDto>>> getAllCategoriesAdmin() {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getAllCategoriesAdmin()));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<ServiceCategoryDto>> getCategoryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(catalogService.getCategoryById(id)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ServiceCategoryDto>> createCategory(@Valid @RequestBody ServiceCategoryDto dto) {
        try {
            ServiceCategoryDto created = catalogService.createCategory(dto);
            return ResponseEntity.ok(ApiResponse.success("Category created successfully", created));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ServiceCategoryDto>> updateCategory(@PathVariable Long id, @RequestBody ServiceCategoryDto dto) {
        try {
            ServiceCategoryDto updated = catalogService.updateCategory(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<List<ServiceItemDto>>> getAllItems() {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getAllAvailableItems()));
    }

    @GetMapping("/categories/{categoryId}/items")
    public ResponseEntity<ApiResponse<List<ServiceItemDto>>> getItemsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getItemsByCategory(categoryId)));
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ApiResponse<ServiceItemDto>> getItemById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(catalogService.getItemById(id)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/items")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ServiceItemDto>> createItem(@Valid @RequestBody ServiceItemDto dto) {
        try {
            ServiceItemDto created = catalogService.createItem(dto);
            return ResponseEntity.ok(ApiResponse.success("Service item created successfully", created));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/items/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ServiceItemDto>> updateItem(@PathVariable Long id, @RequestBody ServiceItemDto dto) {
        try {
            ServiceItemDto updated = catalogService.updateItem(id, dto);
            return ResponseEntity.ok(ApiResponse.success("Service item updated successfully", updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        try {
            catalogService.deleteItem(id);
            return ResponseEntity.ok(ApiResponse.success("Item deleted successfully", null));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
