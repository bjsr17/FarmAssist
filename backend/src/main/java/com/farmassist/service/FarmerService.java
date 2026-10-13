package com.farmassist.service;

import com.farmassist.dto.FarmerProfileDto;
import com.farmassist.entity.FarmerProfile;
import com.farmassist.entity.Role;
import com.farmassist.entity.User;
import com.farmassist.repository.FarmerProfileRepository;
import com.farmassist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmerService {

    private final FarmerProfileRepository farmerProfileRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public FarmerService(FarmerProfileRepository farmerProfileRepository,
                         UserRepository userRepository,
                         AuthService authService) {
        this.farmerProfileRepository = farmerProfileRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public FarmerProfileDto getProfileByUserId(Long userId) {
        FarmerProfile profile = farmerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Farmer profile not found for user ID: " + userId));
        return authService.mapFarmerProfile(profile);
    }

    @Transactional
    public FarmerProfileDto updateProfile(Long userId, FarmerProfileDto dto) {
        FarmerProfile profile = farmerProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    FarmerProfile newProfile = new FarmerProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        if (dto.getFarmLocation() != null) profile.setFarmLocation(dto.getFarmLocation());
        if (dto.getVillage() != null) profile.setVillage(dto.getVillage());
        if (dto.getTalukOrBlock() != null) profile.setTalukOrBlock(dto.getTalukOrBlock());
        if (dto.getDistrict() != null) profile.setDistrict(dto.getDistrict());
        if (dto.getState() != null) profile.setState(dto.getState());
        if (dto.getPincode() != null) profile.setPincode(dto.getPincode());
        if (dto.getLandAreaAcres() != null) profile.setLandAreaAcres(dto.getLandAreaAcres());
        if (dto.getPrimaryCrops() != null) profile.setPrimaryCrops(dto.getPrimaryCrops());
        if (dto.getSoilType() != null) profile.setSoilType(dto.getSoilType());
        if (dto.getIrrigationSource() != null) profile.setIrrigationSource(dto.getIrrigationSource());
        if (dto.getKisanCardNumber() != null) profile.setKisanCardNumber(dto.getKisanCardNumber());
        if (dto.getAdditionalDetails() != null) profile.setAdditionalDetails(dto.getAdditionalDetails());

        // Also update User contact if provided
        User user = profile.getUser();
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getFarmerName() != null) user.setName(dto.getFarmerName());
        userRepository.save(user);

        FarmerProfile saved = farmerProfileRepository.save(profile);
        return authService.mapFarmerProfile(saved);
    }

    public List<FarmerProfileDto> getAllFarmers() {
        return farmerProfileRepository.findAll().stream()
                .map(authService::mapFarmerProfile)
                .collect(Collectors.toList());
    }
}
