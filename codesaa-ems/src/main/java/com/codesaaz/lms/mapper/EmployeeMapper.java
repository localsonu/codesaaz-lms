package com.codesaaz.lms.mapper;

import com.codesaaz.lms.dto.EmployeeDTO;
import com.codesaaz.lms.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDTO mapToDto(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setUsername(employee.getUsername());
//        employeeDTO.setPassword(employee.getPassword());
        employeeDTO.setRole(employee.getRole());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setMiddleName(employee.getMiddleName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setPhoneNumber((employee.getPhoneNumber()));
        employeeDTO.setCreatedAt(employee.getCreatedAt());
        employeeDTO.setStatus(String.valueOf(employee.getStatus()));
        employeeDTO.setTotalLeave(employee.getTotalLeave());
        employeeDTO.setLeaveRemaining(employee.getLeaveRemaining());
        employeeDTO.setLeaveConsumed(employee.getLeaveConsumed());
        return employeeDTO;
    }

    public static Employee mapToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeDTO.getEmployeeId());
        employee.setUsername(employeeDTO.getUsername());
        employee.setPassword(employeeDTO.getPassword());
        employee.setRole(employeeDTO.getRole());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setMiddleName(employeeDTO.getMiddleName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setCreatedAt(employeeDTO.getCreatedAt());
        employee.setStatus(StatusMapper.mapEmployeeStatus(employeeDTO.getStatus()));
        employee.setTotalLeave(employeeDTO.getTotalLeave());
        employee.setLeaveConsumed(employeeDTO.getLeaveConsumed());
        employee.setLeaveRemaining(employeeDTO.getLeaveRemaining());
        return employee;
    }

    public static EmployeeDTO mapToDTOWithSupervisor(Employee employee) {
        EmployeeDTO employeeDTO = mapToDto(employee);
        if (employee.getSupervisor() != null) {
            employeeDTO.setSupervisor(EmployeeMapper.mapToDto(employee.getSupervisor()));
        }
        return employeeDTO;
    }

    public static Employee mapToEntityWithSupervisor(EmployeeDTO employeeDTO) {
        Employee employee = mapToEntity(employeeDTO);
        if (employeeDTO.getSupervisor() != null) {
            employee.setSupervisor(EmployeeMapper.mapToEntity(employeeDTO.getSupervisor()));
        }
        return employee;
    }
}
