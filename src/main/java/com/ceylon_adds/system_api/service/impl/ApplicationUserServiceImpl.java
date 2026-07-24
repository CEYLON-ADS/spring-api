package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.ApplicationUserRequestDTO;
import com.ceylon_adds.system_api.dto.response.ApplicationUserResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateApplicationUserDTO;
import com.ceylon_adds.system_api.entity.ApplicationUser;
import com.ceylon_adds.system_api.entity.ApplicationUserRole;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.exception.DuplicateEntryException;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.ApplicationUserRepository;
import com.ceylon_adds.system_api.repository.ApplicationUserRoleRepository;
import com.ceylon_adds.system_api.service.ApplicationUserService;
import com.ceylon_adds.system_api.util.FileDataHandler;
import com.ceylon_adds.system_api.util.IdGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.FileHandler;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserRoleRepository applicationUserRoleRepository;

    private final FileDataHandler fileDataHandler;

    private final IdGenerator idGenerator;

    private final PasswordEncoder passwordEncoder;

    @Value("${system.host.accessMobile}")
    private String accessMobile;


    @Override
    @Transactional
    public void update(UUID userId, ApplicationUserRequestDTO dto) {

        if (dto == null) throw new BadRequestException("Request cannot be null");

        if (dto.getMobileNumber() == null || dto.getMobileNumber().trim().isEmpty()) {
            throw new BadRequestException("Mobile number cannot be empty");
        }

        ApplicationUser existingUser = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        List<ApplicationUser> list = applicationUserRepository.findAll();

        for (ApplicationUser applicationUser : list) {
            if (!existingUser.getMobileNumber().equals(dto.getMobileNumber()) && dto.getMobileNumber().equals(applicationUser.getMobileNumber()))
                throw new DuplicateEntryException("Already have an user from this mobile number");

        }

        existingUser.setMobileNumber(dto.getMobileNumber());

        // no need to explicitly save() because JPA flushes changes at transaction commit
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        if (!applicationUserRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        applicationUserRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void changeActiveStatus(UUID userId, boolean active) {
        ApplicationUser user = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        user.setActiveState(active);
        // changes auto-flushed at commit
    }

    @Override
    @Transactional
    public void initializeSystemHost() {
        ApplicationUserRole userRole = applicationUserRoleRepository.findByRoleName("USER")
                .orElseThrow(() -> new EntryNotFoundException("USER role not found"));
        ApplicationUserRole hostRole = applicationUserRoleRepository.findByRoleName("HOST")
                .orElseThrow(() -> new EntryNotFoundException("HOST role not found"));
        ApplicationUserRole adminRole = applicationUserRoleRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new EntryNotFoundException("ADMIN role not found"));

        // 1. Host user
        if (applicationUserRepository.findByMobileNumber(accessMobile).isEmpty() && 
            applicationUserRepository.findByUsername("host").isEmpty()) {
            Set<ApplicationUserRole> hostRoles = new HashSet<>(Set.of(userRole, hostRole));
            applicationUserRepository.save(
                    ApplicationUser.builder()
                            .mobileNumber(accessMobile)
                            .username("host")
                            .password(passwordEncoder.encode("host123"))
                            .activeState(true)
                            .accountId(idGenerator.generateUserAccountId(applicationUserRepository))
                            .roles(hostRoles)
                            .build()
            );
        }

        // 2. Admin user
        if (applicationUserRepository.findByMobileNumber("+94111111111").isEmpty() && 
            applicationUserRepository.findByUsername("admin").isEmpty()) {
            Set<ApplicationUserRole> adminRoles = new HashSet<>(Set.of(userRole, hostRole, adminRole));
            applicationUserRepository.save(
                    ApplicationUser.builder()
                            .mobileNumber("+94111111111")
                            .username("admin")
                            .password(passwordEncoder.encode("admin123"))
                            .activeState(true)
                            .accountId(idGenerator.generateUserAccountId(applicationUserRepository))
                            .roles(adminRoles)
                            .build()
            );
        }

        // 3. Regular user
        if (applicationUserRepository.findByMobileNumber("+94222222222").isEmpty() && 
            applicationUserRepository.findByUsername("user").isEmpty()) {
            Set<ApplicationUserRole> regularRoles = new HashSet<>(Set.of(userRole));
            applicationUserRepository.save(
                    ApplicationUser.builder()
                            .mobileNumber("+94222222222")
                            .username("user")
                            .password(passwordEncoder.encode("user123"))
                            .activeState(true)
                            .accountId(idGenerator.generateUserAccountId(applicationUserRepository))
                            .roles(regularRoles)
                            .build()
            );
        }

        // 4. Dummy user
        if (applicationUserRepository.findByMobileNumber("+94777777777").isEmpty() && 
            applicationUserRepository.findByUsername("dummy").isEmpty()) {
            Set<ApplicationUserRole> regularRoles = new HashSet<>(Set.of(userRole));
            applicationUserRepository.save(
                    ApplicationUser.builder()
                            .mobileNumber("+94777777777")
                            .username("dummy")
                            .password(passwordEncoder.encode("dummy123"))
                            .activeState(true)
                            .accountId(idGenerator.generateUserAccountId(applicationUserRepository))
                            .roles(regularRoles)
                            .build()
            );
        }

        // 5. Dummy Admin user
        if (applicationUserRepository.findByMobileNumber("+94888888888").isEmpty() && 
            applicationUserRepository.findByUsername("dummyadmin").isEmpty()) {
            Set<ApplicationUserRole> adminRoles = new HashSet<>(Set.of(userRole, hostRole, adminRole));
            applicationUserRepository.save(
                    ApplicationUser.builder()
                            .mobileNumber("+94888888888")
                            .username("dummyadmin")
                            .password(passwordEncoder.encode("admin123"))
                            .activeState(true)
                            .accountId(idGenerator.generateUserAccountId(applicationUserRepository))
                            .roles(adminRoles)
                            .build()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationUserResponseDTO getById(UUID userId) {
        ApplicationUser user = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        return mapToResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginateApplicationUserDTO search(String searchText, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ApplicationUser> resultPage;

        if (searchText == null || searchText.isBlank()) {
            resultPage = applicationUserRepository.findAll(pageable);
        } else {
            if(searchText.startsWith("0")) searchText = searchText.replaceFirst("^0", "+94");
            resultPage = applicationUserRepository
                    .searchUsers(searchText, pageable);
        }

        return PaginateApplicationUserDTO.builder()
                .count(resultPage.getTotalElements())
                .dataList(resultPage.getContent().stream()
                        .map(this::mapToResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public PaginateApplicationUserDTO getActiveUsers(String searchText, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ApplicationUser> resultPage;

        if (searchText == null || searchText.isBlank()) {
            resultPage = applicationUserRepository.findAllByActiveState(true,pageable);
        } else {
            if(searchText.startsWith("0")) searchText = searchText.replaceFirst("^0", "+94");
            System.out.println(searchText);
            resultPage = applicationUserRepository
                    .searchActiveUsers(searchText,  pageable);
        }

        return PaginateApplicationUserDTO.builder()
                .count(resultPage.getTotalElements())
                .dataList(resultPage.getContent().stream()
                        .map(this::mapToResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public PaginateApplicationUserDTO getBlackListedUsers(String searchText, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ApplicationUser> resultPage;

        if (searchText == null || searchText.isBlank()) {
            resultPage = applicationUserRepository.findAllByActiveState(false,pageable);
        } else {
            if(searchText.startsWith("0")) searchText = searchText.replaceFirst("^0", "+94");
            System.out.println(searchText);
            resultPage = applicationUserRepository
                    .searchActiveUsers(searchText,  pageable);
        }

        return PaginateApplicationUserDTO.builder()
                .count(resultPage.getTotalElements())
                .dataList(resultPage.getContent().stream()
                        .map(this::mapToResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private ApplicationUserResponseDTO mapToResponseDTO(ApplicationUser user) {
        return ApplicationUserResponseDTO.builder()
                .propertyId(user.getPropertyId())
                .mobileNumber(user.getMobileNumber())
                .activeState(user.getActiveState())
                .accountId(user.getAccountId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .avatarUrl(user.getAvatar() != null ? fileDataHandler.byteArrayToString(user.getAvatar().getResourceUrl()) : null)
                .build();
    }
}
