package com.codesaaz.lms.service;

import com.codesaaz.lms.exceptions.DataConflictException;
import com.codesaaz.lms.exceptions.DataNotFoundException;
import com.codesaaz.lms.repository.EmployeeRepository;
import com.codesaaz.lms.repository.LeaveRepository;
import com.codesaaz.lms.security.ExtractAuthUser;
import com.codesaaz.lms.util.ExceptionConstants;
import com.codesaaz.lms.dto.EmployeeDTO;
import com.codesaaz.lms.entity.Employee;
import com.codesaaz.lms.mapper.EmployeeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;

    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(final EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, LeaveRepository leaveRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.leaveRepository = leaveRepository;
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {

        return employeeRepository.findAll(pageable)
                .map(employee -> EmployeeMapper.mapToDTOWithSupervisor(employee));
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_RECORD_NOT_FOUND));
        return EmployeeMapper.mapToDTOWithSupervisor(employee);
    }

    @Override
    public EmployeeDTO retrieveAuthenticatedEmployee() {

        long authenticatedEmployeeId = ExtractAuthUser.getCurrentUser().getEmployeeId();
        Employee employee = employeeRepository.findById(authenticatedEmployeeId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_RECORD_NOT_FOUND));
        return EmployeeMapper.mapToDTOWithSupervisor(employee);
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {

        //  EmployeeSupervisor id is sent but id doesn't exist in database
        if (employeeDTO.getSupervisor() != null &&
                employeeDTO.getSupervisor().getEmployeeId() != null &&
                !employeeRepository.findById(employeeDTO.getSupervisor().getEmployeeId()).isPresent()) {
            throw new DataNotFoundException(ExceptionConstants.EMPLOYEE_SUPERVISOR_MISMATCH);
        }
        if (employeeDTO.getUsername() == null || employeeRepository.findByUsername(employeeDTO.getUsername()) != null) {
            throw new DataNotFoundException(ExceptionConstants.EMPLOYEE_USERNAME_NOT_VALID);
        }
        employeeDTO.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        employeeDTO.setRole("ROLE_USER");
        employeeDTO.setPhoneNumber(employeeDTO.getPhoneNumber());
        employeeDTO.setStatus(employeeDTO.getStatus());
        employeeDTO.setTotalLeave(employeeDTO.getTotalLeave());
        employeeDTO.setLeaveConsumed(0);
        employeeDTO.setLeaveRemaining(0);
        Employee employee = employeeRepository.save(EmployeeMapper.mapToEntityWithSupervisor(employeeDTO));
        return EmployeeMapper.mapToDto(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {

        Employee returnedEmployee = employeeRepository.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_RECORD_NOT_FOUND));

        // Employee cannot be their own Supervisor and EmployeeSupervisor must be present in database
        if ((employeeDTO.getSupervisor() != null &&
                employeeDTO.getSupervisor().getEmployeeId() != null) &&
                (returnedEmployee.getEmployeeId() == employeeDTO.getSupervisor().getEmployeeId()
                        || !employeeRepository.findById(employeeDTO.getSupervisor().getEmployeeId()).isPresent())) {
            throw new DataConflictException(ExceptionConstants.EMPLOYEE_SUPERVISOR_MISMATCH);
        }
        returnedEmployee.setFirstName(employeeDTO.getFirstName());
        returnedEmployee.setMiddleName(employeeDTO.getMiddleName());
        returnedEmployee.setLastName(employeeDTO.getLastName());
        returnedEmployee.setEmail(employeeDTO.getEmail());
        returnedEmployee.setPhoneNumber(employeeDTO.getPhoneNumber());
        if (employeeDTO.getSupervisor() != null) {
            returnedEmployee.setSupervisor(EmployeeMapper.mapToEntity(employeeDTO.getSupervisor()));
        } else {
            returnedEmployee.setSupervisor(null);
        }
        return EmployeeMapper.mapToDto(employeeRepository.save(returnedEmployee));
    }

    @Override
    public EmployeeDTO updatePassword(String oldPassword, String newPassword) {

        long authenticatedEmployeeId = ExtractAuthUser.getCurrentUser().getEmployeeId();
        Employee employee = employeeRepository.findById(authenticatedEmployeeId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_RECORD_NOT_FOUND));
        if (!passwordEncoder.matches(oldPassword, employee.getPassword())) {
            throw new DataNotFoundException(ExceptionConstants.OLD_PASSWORD_DOESNT_MATCH);
        }
        employee.setPassword(passwordEncoder.encode(newPassword));
        return EmployeeMapper.mapToDto(employeeRepository.save(employee));
    }

    @Override
    public List<EmployeeDTO> getAllEmployeeUnderSupervision(Long id) {

        Employee returnedEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_RECORD_NOT_FOUND));

        return employeeRepository.findAllBySupervisor(returnedEmployee)
                .stream()
                .map(employee -> EmployeeMapper.mapToDto(employee))
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeDTO> getAllEmployeesByName(Pageable pageable, String fullName) {

        return employeeRepository.findByFirstNameContainingOrMiddleNameContainingOrLastNameContaining(pageable, fullName, fullName, fullName)
                .map(employee -> EmployeeMapper.mapToDTOWithSupervisor(employee));
    }
}
