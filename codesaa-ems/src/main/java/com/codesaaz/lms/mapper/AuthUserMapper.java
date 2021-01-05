package com.codesaaz.lms.mapper;


import com.codesaaz.lms.dto.AuthUserDTO;
import com.codesaaz.lms.entity.Employee;

public class AuthUserMapper {

    public static AuthUserDTO mapToAuthUserDTO(Employee employee) {
        AuthUserDTO authUser = new AuthUserDTO();
        authUser.setId(employee.getEmployeeId());
        authUser.setFullName(employee.getFirstName());
        authUser.setUsername(employee.getUsername());
        authUser.setRole(employee.getRole());
        authUser.setPhoneNumber(employee.getPhoneNumber());
    //    authUser.setStatus(employee.getStatus());
        return authUser;
    }

    public static Employee mapToAuthUserEntity(AuthUserDTO authUser) {
        Employee userEntity = new Employee();
        userEntity.setEmployeeId(authUser.getId());
        userEntity.setFirstName(authUser.getFullName());
        userEntity.setUsername(authUser.getUsername());
        userEntity.setRole(authUser.getRole());
        userEntity.setPhoneNumber(authUser.getPhoneNumber());
       // userEntity.setStatus(authUser.getStatus().toString());
        return userEntity;
    }
}
