package com.ceylon_adds.system_api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.ceylon_adds.system_api.dto.request.UsernamePasswordLoginRequestDTO;
import com.ceylon_adds.system_api.dto.request.UsernamePasswordRegisterRequestDTO;

import com.ceylon_adds.system_api.dto.request.ApplicationUserLoginRequestDTO;
import com.ceylon_adds.system_api.dto.request.ApplicationUserOTPRequestDTO;
import com.ceylon_adds.system_api.dto.response.SuccessFullLoginResponseDTO;
import com.ceylon_adds.system_api.entity.ApplicationUser;
import com.ceylon_adds.system_api.entity.ApplicationUserRole;
import com.ceylon_adds.system_api.entity.OTP;
import com.ceylon_adds.system_api.entity.enums.UserRole;
import com.ceylon_adds.system_api.exception.BadRequestException;
import com.ceylon_adds.system_api.exception.InternalServerErrorException;
import com.ceylon_adds.system_api.exception.OTPVerificationFailedException;
import com.ceylon_adds.system_api.exception.SMSGatewayException;
import com.ceylon_adds.system_api.repository.ApplicationUserRepository;
import com.ceylon_adds.system_api.repository.ApplicationUserRoleRepository;
import com.ceylon_adds.system_api.repository.OTPRepository;
import com.ceylon_adds.system_api.service.AuthService;
import com.ceylon_adds.system_api.util.IdGenerator;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ApplicationUserRepository userRepository;

    private final ApplicationUserRoleRepository roleRepository;

    private final OTPRepository otpRepository;

    private final IdGenerator idGenerator;

    private final SecretKey secretKey;

    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${twilio.account-sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Value("${twilio.whatsapp-number}")
    private String twilioWhatsAppNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    @Transactional
    @Override
    public void sendOTP(ApplicationUserLoginRequestDTO dto) {

        if (dto == null) throw new BadRequestException("Request cannot be null");

        // Validate mobile number
        if (dto.getMobileNumber() == null || dto.getMobileNumber().trim().isEmpty()) {
            throw new BadRequestException("Mobile number cannot be empty");
        }

        // Check if user exists
        ApplicationUser user = userRepository.findByMobileNumber(reformatMobileNumber(dto.getMobileNumber().trim(),dto.getCountryCode().trim()))
                .orElseGet(() -> {
                    // Create new user if doesn't exist
                    ApplicationUser newUser = ApplicationUser.builder()
                            .mobileNumber(reformatMobileNumber(dto.getMobileNumber().trim(),dto.getCountryCode().trim()))
                            .accountId(idGenerator.generateUserAccountId(userRepository))
                            .activeState(true)
                            .roles(new HashSet<>())
                            .build();
                    // Assign default role
                    ApplicationUserRole defaultRole = roleRepository.findByRoleName(UserRole.USER.name())
                            .orElseGet(() -> roleRepository.save(
                                    ApplicationUserRole.builder()
                                            .roleName(UserRole.USER.name())
                                            .build()));
                    newUser.getRoles().add(defaultRole);
                    return userRepository.save(newUser);
                });

        // Generate OTP
        String otpCode = generateOTP();

        // Check for existing OTP
        Optional<OTP> existingOtp = otpRepository.findByApplicationUser(user);
        OTP otp;
        if (existingOtp.isPresent()) {
            // Update existing OTP
            otp = existingOtp.get();
            otp.setOtp(otpCode);
            otp.setAttempts(0);
            otp.setExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));
        } else {
            // Create new OTP
            otp = OTP.builder()
                    .otp(otpCode)
                    .attempts(0)
                    .expiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                    .applicationUser(user)
                    .build();
        }

        // Save OTP (new or updated)
        try {
            otpRepository.save(otp);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to save OTP for user: {}", dto.getMobileNumber(), e);
            throw new InternalServerErrorException("Failed to save OTP due to database constraint violation: " + e.getMessage());
        }

        try {
            System.out.println("Your OTP code is: " + otpCode);
//            Message message = Message.creator(
//                    new PhoneNumber("whatsapp:" + (reformatMobileNumber(dto.getMobileNumber()))),
//                    new PhoneNumber(twilioWhatsAppNumber),
//                    "Your OTP code is: " + otpCode
//            ).create();
//            logger.info("OTP sent via WhatsApp to {}: Message SID {}", dto.getMobileNumber(), message.getSid());

        } catch (Exception e) {
            logger.error("Failed to send OTP via WhatsApp to {}: {}", dto.getMobileNumber(), e.getMessage());
            logger.warn("SMS gateway error bypassed for local development. Use OTP '123456' to verify.");
        }
    }

    @Override
    public SuccessFullLoginResponseDTO verifyOTP(ApplicationUserOTPRequestDTO dto) {
        if (dto == null) throw new BadRequestException("Request cannot be null");

        // Validate mobile number
        if (dto.getMobileNumber() == null || dto.getMobileNumber().trim().isEmpty()) {
            throw new BadRequestException("Mobile number cannot be empty");
        }

        // Validate OTP
        if (dto.getOtp() == null || dto.getOtp().trim().isEmpty()) {
            throw new BadRequestException("OTP cannot be empty");
        }
        ApplicationUser user = userRepository.findByMobileNumber(reformatMobileNumber(dto.getMobileNumber().trim(),dto.getCountryCode().trim()))
                .orElseThrow(() -> new OTPVerificationFailedException("User not found"));

        // Bypass OTP check for local development using master code
        if ("123456".equals(dto.getOtp())) {
            otpRepository.findByApplicationUser(user).ifPresent(otpRepository::delete);
            return SuccessFullLoginResponseDTO.builder().token(generateJWT(user)).build();
        }

        OTP otpEntity = otpRepository.findByApplicationUser(user)
                .orElseThrow(() -> new OTPVerificationFailedException("OTP not found"));

        if (otpEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new OTPVerificationFailedException("OTP expired");
        }

        if (otpEntity.getAttempts() >= 3) {
            throw new OTPVerificationFailedException("Maximum attempts exceeded");
        }

        if (!otpEntity.getOtp().equals(dto.getOtp())) {
            otpEntity.setAttempts(otpEntity.getAttempts() + 1);
            otpRepository.save(otpEntity);
            throw new OTPVerificationFailedException("Invalid OTP");
        }

        // OTP valid, delete it
        otpRepository.delete(otpEntity);

        // Generate JWT
        return SuccessFullLoginResponseDTO.builder().token(generateJWT(user)).userId(user.getPropertyId()).build();


    }

    @Override
    public SuccessFullLoginResponseDTO loginWithPassword(UsernamePasswordLoginRequestDTO dto) {
        if (dto == null) throw new BadRequestException("Request cannot be null");
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password cannot be empty");
        }

        ApplicationUser user = userRepository.findByUsername(dto.getUsername().trim())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        if (!Boolean.TRUE.equals(user.getActiveState())) {
            throw new BadRequestException("Account is deactivated");
        }

        return SuccessFullLoginResponseDTO.builder()
                .token(generateJWT(user))
                .userId(user.getPropertyId())
                .build();
    }

    @Transactional
    @Override
    public void registerWithPassword(UsernamePasswordRegisterRequestDTO dto) {
        if (dto == null) throw new BadRequestException("Request cannot be null");
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password cannot be empty");
        }
        if (dto.getMobileNumber() == null || dto.getMobileNumber().trim().isEmpty()) {
            throw new BadRequestException("Mobile number cannot be empty");
        }

        String formattedMobile = dto.getMobileNumber().trim();
        if (formattedMobile.startsWith("0")) {
            formattedMobile = formattedMobile.replaceFirst("^0", "+94");
        }

        if (userRepository.findByUsername(dto.getUsername().trim()).isPresent()) {
            throw new BadRequestException("Username is already taken");
        }

        if (userRepository.findByMobileNumber(formattedMobile).isPresent()) {
            throw new BadRequestException("Mobile number is already registered");
        }

        // Fetch USER role
        ApplicationUserRole defaultRole = roleRepository.findByRoleName(UserRole.USER.name())
                .orElseGet(() -> roleRepository.save(
                        ApplicationUserRole.builder()
                                .roleName(UserRole.USER.name())
                                .build()));

        ApplicationUser newUser = ApplicationUser.builder()
                .username(dto.getUsername().trim())
                .password(passwordEncoder.encode(dto.getPassword()))
                .mobileNumber(formattedMobile)
                .accountId(idGenerator.generateUserAccountId(userRepository))
                .activeState(true)
                .roles(new HashSet<>(java.util.Set.of(defaultRole)))
                .build();

        userRepository.save(newUser);
    }


    private String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private String generateJWT(ApplicationUser user) {
//        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(user.getMobileNumber())
                .claim("roles", user.getRoles().stream()
                        .map(ApplicationUserRole::getRoleName)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.MILLIS)))
                .signWith(secretKey)
                .compact();
    }

    private String reformatMobileNumber(String mobileNumber, String countryCode) {

        return mobileNumber.startsWith("0") ? mobileNumber.replaceFirst("^0", countryCode) : mobileNumber;

    }
}

