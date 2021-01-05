package com.codesaaz.lms.controller;

import com.codesaaz.lms.dto.AuthResponseDTO;
import com.codesaaz.lms.dto.GenericResponseDTO;
import com.codesaaz.lms.dto.LoginRequestDTO;
import com.codesaaz.lms.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequest) {
        log.info("Authentication API: login requested by user: ", loginRequest.getUsername());
        GenericResponseDTO<AuthResponseDTO> genericResponse = authenticationService.loginUser(loginRequest);
        return new ResponseEntity<>(genericResponse, genericResponse.getHttpStatus());
    }

    @GetMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("userEmail") String userEmail) {
        log.info("Authentication API: processing forgot password for email: ", userEmail);
        GenericResponseDTO<?> genericResponse = authenticationService.forgotPassword(userEmail);
        return new ResponseEntity<>(genericResponse, genericResponse.getHttpStatus());
    }
}
