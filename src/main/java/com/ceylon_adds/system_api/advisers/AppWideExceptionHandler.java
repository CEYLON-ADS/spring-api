package com.ceylon_adds.system_api.advisers;

import com.ceylon_adds.system_api.exception.*;
import com.ceylon_adds.system_api.util.StandardResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(OTPVerificationFailedException.class)
    public ResponseEntity<StandardResponseDTO> handleOTPVerificationFailedException(OTPVerificationFailedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                StandardResponseDTO.builder().code(400).message(exception.getMessage()).data(exception).build()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardResponseDTO> handleBadRequestException(BadRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                StandardResponseDTO.builder().code(400).message(exception.getMessage()).data(exception).build()
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<StandardResponseDTO> handleInternalServerErrorException(InternalServerErrorException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                StandardResponseDTO.builder().code(400).message(exception.getMessage()).data(exception).build()
        );
    }

    @ExceptionHandler(SMSGatewayException.class)
    public ResponseEntity<StandardResponseDTO> handleSMSGatewayException(SMSGatewayException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                StandardResponseDTO.builder().code(502).message(exception.getMessage()).data(exception).build()
        );
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<StandardResponseDTO> handleDuplicateEntryException(DuplicateEntryException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                StandardResponseDTO.builder().code(409).message(exception.getMessage()).data(exception).build()
        );
    }
}
