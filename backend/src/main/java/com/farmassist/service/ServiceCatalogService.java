package com.farmassist.service;

import com.farmassist.dto.ServiceCategoryDto;
import com.farmassist.dto.ServiceItemDto;
import com.farmassist.entity.ServiceCategory;
import com.farmassist.entity.ServiceItem;
import com.farmassist.repository.ServiceCategoryRepository;
import com.farmassist.repository.ServiceItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceCatalogService {

    private final ServiceCategoryRepository categoryRepository;
    private final ServiceItemRepository itemRepository;

    public ServiceCatalogService(ServiceCategoryRepository categoryRepository,
                                 ServiceItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    public List<ServiceCategoryDto> getAllActiveCategoriesWithItems() {
        List<ServiceCategory> categories = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        return categories.stream().map(this::mapCategoryWithItems).collect(Collectors.toList());
    }

    public List<ServiceCategoryDto> getAllCategoriesAdmin() {
        List<ServiceCategory> categories = categoryRepository.findAllByOrderByDisplayOrderAsc();
        return categories.stream().map(this::mapCategoryWithItems).collect(Collectors.toList());
    }

    public ServiceCategoryDto getCategoryById(Long id) {
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service Category not found: " + id));
        return mapCategoryWithItems(category);
    }

    @Transactional
    public ServiceCategoryDto createCategory(ServiceCategoryDto dto) {
        ServiceCategory category = new ServiceCategory();
        category.setName(dto.getName());
        category.setCode(dto.getCode().toUpperCase().trim());
        category.setDescription(dto.getDescription());
        category.setIcon(dto.getIcon());
        category.setBannerUrl(dto.getBannerUrl());
        category.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        category.setDisplayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0);

        return mapCategory(categoryRepository.save(category));
    }

    @Transactional
    public ServiceCategoryDto updateCategory(Long id, ServiceCategoryDto dto) {
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service Category not found: " + id));

        if (dto.getName() != null) category.setName(dto.getName());
        if (dto.getCode() != null) category.setCode(dto.getCode().toUpperCase().trim());
        if (dto.getDescription() != null) category.setDescription(dto.getDescription());
        if (dto.getIcon() != null) category.setIcon(dto.getIcon());
        if (dto.getBannerUrl() != null) category.setBannerUrl(dto.getBannerUrl());
        if (dto.getIsActive() != null) category.setIsActive(dto.getIsActive());
        if (dto.getDisplayOrder() != null) category.setDisplayOrder(dto.getDisplayOrder());

        return mapCategoryWithItems(categoryRepository.save(category));
    }

    public List<ServiceItemDto> getAllAvailableItems() {
        return itemRepository.findByIsAvailableTrue().stream()
                .map(this::mapItem)
                .collect(Collectors.toList());
    }

    public List<ServiceItemDto> getItemsByCategory(Long categoryId) {
        return itemRepository.findByCategoryIdAndIsAvailableTrue(categoryId).stream()
                .map(this::mapItem)
                .collect(Collectors.toList());
    }

    public ServiceItemDto getItemById(Long id) {
        ServiceItem item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service Item not found: " + id));
        return mapItem(item);
    }

    @Transactional
    public ServiceItemDto createItem(ServiceItemDto dto) {
        ServiceCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId()));

        ServiceItem item = new ServiceItem();
        item.setCategory(category);
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setBasePrice(dto.getBasePrice());
        item.setUnitType(dto.getUnitType());
        item.setUnitLabel(dto.getUnitLabel());
        item.setImageUrl(dto.getImageUrl());
        item.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);
        item.setEstimatedDurationHours(dto.getEstimatedDurationHours());
        item.setSpecificationJson(dto.getSpecificationJson());

        return mapItem(itemRepository.save(item));
    }

    @Transactional
    public ServiceItemDto updateItem(Long id, ServiceItemDto dto) {
        ServiceItem item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service Item not found: " + id));

        if (dto.getCategoryId() != null && !dto.getCategoryId().equals(item.getCategory().getId())) {
            ServiceCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId()));
            item.setCategory(category);
        }

        if (dto.getName() != null) item.setName(dto.getName());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getBasePrice() != null) item.setBasePrice(dto.getBasePrice());
        if (dto.getUnitType() != null) item.setUnitType(dto.getUnitType());
        if (dto.getUnitLabel() != null) item.setUnitLabel(dto.getUnitLabel());
        if (dto.getImageUrl() != null) item.setImageUrl(dto.getImageUrl());
        if (dto.getIsAvailable() != null) item.setIsAvailable(dto.getIsAvailable());
        if (dto.getEstimatedDurationHours() != null) item.setEstimatedDurationHours(dto.getEstimatedDurationHours());
        if (dto.getSpecificationJson() != null) item.setSpecificationJson(dto.getSpecificationJson());

        return mapItem(itemRepository.save(item));
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public ServiceCategoryDto mapCategory(ServiceCategory cat) {
        ServiceCategoryDto dto = new ServiceCategoryDto();
        dto.setId(cat.getId());
        dto.setName(cat.getName());
        dto.setCode(cat.getCode());
        dto.setDescription(cat.getDescription());
        dto.setIcon(cat.getIcon());
        dto.setBannerUrl(cat.getBannerUrl());
        dto.setIsActive(cat.getIsActive());
        dto.setDisplayOrder(cat.getDisplayOrder());
        return dto;
    }

    public ServiceCategoryDto mapCategoryWithItems(ServiceCategory cat) {
        ServiceCategoryDto dto = mapCategory(cat);
        List<ServiceItem> items = itemRepository.findByCategory(cat);
        dto.setItems(items.stream().map(this::mapItem).collect(Collectors.toList()));
        return dto;
    }

    public ServiceItemDto mapItem(ServiceItem item) {
        ServiceItemDto dto = new ServiceItemDto();
        dto.setId(item.getId());
        dto.setCategoryId(item.getCategory().getId());
        dto.setCategoryName(item.getCategory().getName());
        dto.setCategoryCode(item.getCategory().getCode());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setBasePrice(item.getBasePrice());
        dto.setUnitType(item.getUnitType());
        dto.setUnitLabel(item.getUnitLabel());
        dto.setImageUrl(item.getImageUrl());
        dto.setIsAvailable(item.getIsAvailable());
        dto.setEstimatedDurationHours(item.getEstimatedDurationHours());
        dto.setSpecificationJson(item.getSpecificationJson());
        return dto;
    }
}
