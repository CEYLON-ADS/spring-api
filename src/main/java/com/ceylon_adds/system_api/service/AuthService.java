package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.ApplicationUserLoginRequestDTO;
import com.ceylon_adds.system_api.dto.request.ApplicationUserOTPRequestDTO;
import com.ceylon_adds.system_api.dto.response.SuccessFullLoginResponseDTO;

public interface AuthService {

    void sendOTP(ApplicationUserLoginRequestDTO dto);

    SuccessFullLoginResponseDTO verifyOTP(ApplicationUserOTPRequestDTO dto);

    SuccessFullLoginResponseDTO loginWithPassword(com.ceylon_adds.system_api.dto.request.UsernamePasswordLoginRequestDTO dto);

    void registerWithPassword(com.ceylon_adds.system_api.dto.request.UsernamePasswordRegisterRequestDTO dto);


}
