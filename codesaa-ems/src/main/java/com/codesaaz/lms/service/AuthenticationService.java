package com.codesaaz.lms.service;

import com.codesaaz.lms.dto.AuthResponseDTO;
import com.codesaaz.lms.dto.GenericResponseDTO;
import com.codesaaz.lms.dto.LoginRequestDTO;

public interface AuthenticationService {

    GenericResponseDTO<AuthResponseDTO> loginUser(LoginRequestDTO loginRequest);

    GenericResponseDTO<?> forgotPassword(String userEmail);

}
