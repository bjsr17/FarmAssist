package com.farmassist.service;

import com.farmassist.config.JwtTokenProvider;
import com.farmassist.dto.*;
import com.farmassist.entity.*;
import com.farmassist.repository.FarmerProfileRepository;
import com.farmassist.repository.ProviderProfileRepository;
import com.farmassist.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       FarmerProfileRepository farmerProfileRepository,
                       ProviderProfileRepository providerProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered: " + req.getEmail());
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setRole(req.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setAddress(req.getAddress());
        user.setDistrict(req.getDistrict());
        user.setState(req.getState());
        user.setPincode(req.getPincode());

        User savedUser = userRepository.save(user);
        Object profileDto = null;

        if (req.getRole() == Role.ROLE_FARMER) {
            FarmerProfile profile = new FarmerProfile();
            profile.setUser(savedUser);
            profile.setFarmLocation(req.getFarmLocation() != null ? req.getFarmLocation() : req.getAddress());
            profile.setVillage(req.getVillage());
            profile.setDistrict(req.getDistrict());
            profile.setState(req.getState());
            profile.setPincode(req.getPincode());
            profile.setLandAreaAcres(req.getLandAreaAcres() != null ? req.getLandAreaAcres() : 5.0);
            profile.setPrimaryCrops(req.getPrimaryCrops() != null ? req.getPrimaryCrops() : "Wheat, Rice, Mustard");
            profile.setSoilType(req.getSoilType() != null ? req.getSoilType() : "Alluvial Soil");
            profile.setIrrigationSource(req.getIrrigationSource() != null ? req.getIrrigationSource() : "Tube Well / Canal");
            FarmerProfile savedProfile = farmerProfileRepository.save(profile);
            profileDto = mapFarmerProfile(savedProfile);
        } else if (req.getRole() == Role.ROLE_PROVIDER) {
            ProviderProfile profile = new ProviderProfile();
            profile.setUser(savedUser);
            profile.setAgencyName(req.getAgencyName() != null ? req.getAgencyName() : req.getName() + " Agri Services");
            profile.setContactPerson(req.getContactPerson() != null ? req.getContactPerson() : req.getName());
            profile.setContactPhone(req.getPhone());
            profile.setServiceCategories(req.getServiceCategories() != null ? req.getServiceCategories() : "MACHINERY,IRRIGATION,TRANSPORT");
            profile.setCoverageDistricts(req.getCoverageDistricts() != null ? req.getCoverageDistricts() : req.getDistrict());
            profile.setExperienceYears(req.getExperienceYears() != null ? req.getExperienceYears() : 3);
            profile.setIsVerified(true);
            profile.setAvailabilityStatus("AVAILABLE");
            ProviderProfile savedProfile = providerProfileRepository.save(profile);
            profileDto = mapProviderProfile(savedProfile);
        }

        String token = tokenProvider.generateToken(savedUser.getEmail(), savedUser.getRole().name(), savedUser.getId());

        return new AuthResponse(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
                savedUser.getRole(), savedUser.getStatus(), savedUser.getPhone(), profileDto);
    }

    public AuthResponse login(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase().trim(), req.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(req.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        Object profileDto = null;
        if (user.getRole() == Role.ROLE_FARMER) {
            profileDto = farmerProfileRepository.findByUserId(user.getId())
                    .map(this::mapFarmerProfile)
                    .orElse(null);
        } else if (user.getRole() == Role.ROLE_PROVIDER) {
            profileDto = providerProfileRepository.findByUserId(user.getId())
                    .map(this::mapProviderProfile)
                    .orElse(null);
        }

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getRole(), user.getStatus(), user.getPhone(), profileDto);
    }

    public User getCurrentAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("No authenticated user found in session");
        }
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public UserDto mapUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setAddress(user.getAddress());
        dto.setDistrict(user.getDistrict());
        dto.setState(user.getState());
        dto.setPincode(user.getPincode());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public FarmerProfileDto mapFarmerProfile(FarmerProfile profile) {
        FarmerProfileDto dto = new FarmerProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setFarmerName(profile.getUser().getName());
        dto.setEmail(profile.getUser().getEmail());
        dto.setPhone(profile.getUser().getPhone());
        dto.setFarmLocation(profile.getFarmLocation());
        dto.setVillage(profile.getVillage());
        dto.setTalukOrBlock(profile.getTalukOrBlock());
        dto.setDistrict(profile.getDistrict());
        dto.setState(profile.getState());
        dto.setPincode(profile.getPincode());
        dto.setLandAreaAcres(profile.getLandAreaAcres());
        dto.setPrimaryCrops(profile.getPrimaryCrops());
        dto.setSoilType(profile.getSoilType());
        dto.setIrrigationSource(profile.getIrrigationSource());
        dto.setKisanCardNumber(profile.getKisanCardNumber());
        dto.setAdditionalDetails(profile.getAdditionalDetails());
        return dto;
    }

    public ProviderProfileDto mapProviderProfile(ProviderProfile profile) {
        ProviderProfileDto dto = new ProviderProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setProviderName(profile.getUser().getName());
        dto.setEmail(profile.getUser().getEmail());
        dto.setPhone(profile.getUser().getPhone());
        dto.setAgencyName(profile.getAgencyName());
        dto.setContactPerson(profile.getContactPerson());
        dto.setContactPhone(profile.getContactPhone());
        dto.setServiceCategories(profile.getServiceCategories());
        dto.setCoverageDistricts(profile.getCoverageDistricts());
        dto.setExperienceYears(profile.getExperienceYears());
        dto.setRatingAvg(profile.getRatingAvg());
        dto.setRatingCount(profile.getRatingCount());
        dto.setIsVerified(profile.getIsVerified());
        dto.setAvailabilityStatus(profile.getAvailabilityStatus());
        dto.setGstOrLicenseNumber(profile.getGstOrLicenseNumber());
        dto.setEquipmentInventorySummary(profile.getEquipmentInventorySummary());
        return dto;
    }
}
