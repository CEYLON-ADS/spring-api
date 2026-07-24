package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.dto.request.ApplicationUserLoginRequestDTO;
import com.ceylon_adds.system_api.dto.request.ApplicationUserOTPRequestDTO;
import com.ceylon_adds.system_api.dto.request.UsernamePasswordLoginRequestDTO;
import com.ceylon_adds.system_api.dto.request.UsernamePasswordRegisterRequestDTO;
import com.ceylon_adds.system_api.service.AuthService;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Login and OTP verification endpoints")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Send OTP", description = "Send OTP to the user's mobile number via WhatsApp")
    @PostMapping("/login")
    public ResponseEntity<StandardResponseDTO> login(@RequestBody ApplicationUserLoginRequestDTO dto) {
        authService.sendOTP(dto);
        return ResponseEntity.ok(StandardResponseDTO.builder()
                .code(200)
                .message("OTP send successfully")
                .data(null)
                .build());
    }

    @Operation(summary = "Verify OTP", description = "Verify OTP sent to the user and return JWT token")
    @PostMapping("/verify-otp")
    public ResponseEntity<StandardResponseDTO> verifyOTP(@RequestBody ApplicationUserOTPRequestDTO dto) {
        return ResponseEntity.ok(StandardResponseDTO.builder()
                .code(200)
                .message("Login successfully")
                .data(authService.verifyOTP(dto))
                .build());
    }

    @Operation(summary = "Login with username and password", description = "Authenticate using username and password, return JWT token")
    @PostMapping("/login-password")
    public ResponseEntity<StandardResponseDTO> loginWithPassword(@RequestBody UsernamePasswordLoginRequestDTO dto) {
        return ResponseEntity.ok(StandardResponseDTO.builder()
                .code(200)
                .message("Login successfully")
                .data(authService.loginWithPassword(dto))
                .build());
    }

    @Operation(summary = "Register with username, password and mobile number", description = "Register a new user account")
    @PostMapping("/register-password")
    public ResponseEntity<StandardResponseDTO> registerWithPassword(@RequestBody UsernamePasswordRegisterRequestDTO dto) {
        authService.registerWithPassword(dto);
        return ResponseEntity.ok(StandardResponseDTO.builder()
                .code(200)
                .message("Registration successful")
                .data(null)
                .build());
    }
}
