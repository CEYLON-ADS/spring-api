package com.ceylon_adds.system_api.api;

import com.ceylon_adds.system_api.util.StandardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "Test", description = "Health check or test endpoints")
public class TestController {

    @Operation(summary = "Health check", description = "Simple API to test server availability")
    @GetMapping
    public ResponseEntity<StandardResponseDTO> test() {
        return ResponseEntity.ok().body(
                StandardResponseDTO.builder()
                        .code(200)
                        .message("Ok")
                        .data(null)
                        .build()
        );
    }
}
