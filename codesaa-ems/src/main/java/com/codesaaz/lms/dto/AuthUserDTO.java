package com.codesaaz.lms.dto;

import com.codesaaz.lms.util.enums.EmployeeRole;
import lombok.Data;

@Data
public class AuthUserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String role;
    private String phoneNumber;
    private EmployeeRole status;
}
