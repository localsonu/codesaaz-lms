package com.codesaaz.lms.service;

import com.codesaaz.lms.dto.*;
import com.codesaaz.lms.entity.Employee;
import com.codesaaz.lms.exceptions.AppExceptionConstants;
import com.codesaaz.lms.mapper.AuthUserMapper;
import com.codesaaz.lms.security.JwtTokenProvider;
import com.codesaaz.lms.util.MessageCodeUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public GenericResponseDTO<AuthResponseDTO> loginUser(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            Employee userEntity = (Employee) authentication.getPrincipal();
            AuthUserDTO authUserDTO = AuthUserMapper.mapToAuthUserDTO(userEntity);
            String token = jwtTokenProvider.createToken(authUserDTO);
            AuthResponseDTO authenticationDTO = new AuthResponseDTO(token, AuthUserMapper.mapToAuthUserDTO(userEntity));
            return ResponseBuilder.buildSuccessResponse(authenticationDTO);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(AppExceptionConstants.BAD_LOGIN_CREDENTIALS);
        }
    }

    @Override
    public GenericResponseDTO<?> forgotPassword(String userEmail) {
        return ResponseBuilder.buildFailureResponse(MessageCodeUtil.IMPLEMENTATION_NOT_AVAILABLE);
    }
}
