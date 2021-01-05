package com.codesaaz.lms.security;

import com.codesaaz.lms.entity.Employee;
import com.codesaaz.lms.util.enums.EmployeeRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ExtractAuthUser {

    public static Employee getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Employee) authentication.getPrincipal();
    }

    public static boolean isOwner(Long userId) {
        Employee employee = getCurrentUser();
        if (employee.getEmployeeId() == userId) {
            return true;
        }
        return false;
    }

    public static boolean isAdmin() {
        Employee userEntity = getCurrentUser();
        boolean isAdmin = userEntity.getRole().equals(String.valueOf(EmployeeRole.ROLE_ADMIN));
        return isAdmin;
    }

    public static Long resolveUserId(Long userId) {
        if (userId == null) {
            userId = ExtractAuthUser.getCurrentUser().getEmployeeId();
        }
        return userId;
    }

    public static boolean hasAdminAccessOrIsOwner(Long resourceOwnerId) {
        if (resourceOwnerId == null) {
            return false;
        }
        if (isAdmin() || isOwner(resourceOwnerId)) {
            return true;
        }
        return false;
    }

}
