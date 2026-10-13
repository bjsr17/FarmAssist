package com.farmassist.service;

import com.farmassist.dto.ProviderProfileDto;
import com.farmassist.entity.ProviderProfile;
import com.farmassist.entity.User;
import com.farmassist.repository.ProviderProfileRepository;
import com.farmassist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    private final ProviderProfileRepository providerProfileRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public ProviderService(ProviderProfileRepository providerProfileRepository,
                           UserRepository userRepository,
                           AuthService authService) {
        this.providerProfileRepository = providerProfileRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public ProviderProfileDto getProfileByUserId(Long userId) {
        ProviderProfile profile = providerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found for user ID: " + userId));
        return authService.mapProviderProfile(profile);
    }

    @Transactional
    public ProviderProfileDto updateProfile(Long userId, ProviderProfileDto dto) {
        ProviderProfile profile = providerProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    ProviderProfile newProfile = new ProviderProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        if (dto.getAgencyName() != null) profile.setAgencyName(dto.getAgencyName());
        if (dto.getContactPerson() != null) profile.setContactPerson(dto.getContactPerson());
        if (dto.getContactPhone() != null) profile.setContactPhone(dto.getContactPhone());
        if (dto.getServiceCategories() != null) profile.setServiceCategories(dto.getServiceCategories());
        if (dto.getCoverageDistricts() != null) profile.setCoverageDistricts(dto.getCoverageDistricts());
        if (dto.getExperienceYears() != null) profile.setExperienceYears(dto.getExperienceYears());
        if (dto.getAvailabilityStatus() != null) profile.setAvailabilityStatus(dto.getAvailabilityStatus());
        if (dto.getGstOrLicenseNumber() != null) profile.setGstOrLicenseNumber(dto.getGstOrLicenseNumber());
        if (dto.getEquipmentInventorySummary() != null) profile.setEquipmentInventorySummary(dto.getEquipmentInventorySummary());

        User user = profile.getUser();
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getProviderName() != null) user.setName(dto.getProviderName());
        userRepository.save(user);

        ProviderProfile saved = providerProfileRepository.save(profile);
        return authService.mapProviderProfile(saved);
    }

    @Transactional
    public ProviderProfileDto toggleAvailability(Long userId, String status) {
        ProviderProfile profile = providerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        profile.setAvailabilityStatus(status);
        return authService.mapProviderProfile(providerProfileRepository.save(profile));
    }

    @Transactional
    public ProviderProfileDto verifyProvider(Long providerProfileId, boolean verified) {
        ProviderProfile profile = providerProfileRepository.findById(providerProfileId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found"));
        profile.setIsVerified(verified);
        return authService.mapProviderProfile(providerProfileRepository.save(profile));
    }

    public List<ProviderProfileDto> getAllProviders() {
        return providerProfileRepository.findAll().stream()
                .map(authService::mapProviderProfile)
                .collect(Collectors.toList());
    }

    public List<ProviderProfileDto> getAvailableProviders() {
        return providerProfileRepository.findByAvailabilityStatus("AVAILABLE").stream()
                .map(authService::mapProviderProfile)
                .collect(Collectors.toList());
    }
}
