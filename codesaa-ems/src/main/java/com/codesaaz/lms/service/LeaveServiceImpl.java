package com.codesaaz.lms.service;

import com.codesaaz.lms.exceptions.DataConflictException;
import com.codesaaz.lms.exceptions.DataNotFoundException;
import com.codesaaz.lms.exceptions.UnauthorizedRequest;
import com.codesaaz.lms.repository.EmployeeRepository;
import com.codesaaz.lms.repository.LeaveRepository;
import com.codesaaz.lms.mapper.StatusMapper;
import com.codesaaz.lms.security.ExtractAuthUser;
import com.codesaaz.lms.util.DateUtil;
import com.codesaaz.lms.util.ExceptionConstants;
import com.codesaaz.lms.dto.EmployeeDTO;
import com.codesaaz.lms.dto.LeaveDTO;
import com.codesaaz.lms.entity.Employee;
import com.codesaaz.lms.entity.Leave;
import com.codesaaz.lms.mapper.LeaveMapper;
import com.codesaaz.lms.util.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository employeeLeaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveServiceImpl(final LeaveRepository employeeLeaveRepository, final EmployeeRepository employeeRepository) {
        this.employeeLeaveRepository = employeeLeaveRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Page<LeaveDTO> getAllEmployeeLeaves(Pageable pageable) {

        return employeeLeaveRepository.findAll(pageable)
                .map(employeeLeave -> LeaveMapper.mapToDto(employeeLeave));
    }

    @Override
    public LeaveDTO getEmployeeLeaveById(Long id) {

        Leave employeeLeave = employeeLeaveRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_RECORD_NOT_FOUND));
        return LeaveMapper.mapToDto(employeeLeave);
    }

    @Override
    public Page<Leave> getEmployeeLeaveByEmployeeId(Pageable pageable, Long id) {
        Page<Leave> employeeLeave = employeeLeaveRepository.findLeaveByEmployeeEmployeeId(id, pageable);
        return employeeLeave;
    }

    @Override
    public LeaveDTO createEmployeeLeave(LeaveDTO leaveDTO) {

        if (leaveDTO.getLeaveTypeDTO() == null) {
            throw new DataNotFoundException(ExceptionConstants.LEAVE_TYPE_RECORD_NOT_FOUND);
        }
        // Setting current employee id on current newly created Leave Request
        long employeeId = ExtractAuthUser.getCurrentUser().getEmployeeId();
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee.getTotalLeave() == employee.getLeaveConsumed()) {
            throw new DataConflictException(ExceptionConstants.EMPLOYEE_LEAVE_LIMIT_EXCEED);
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employeeId);

        leaveDTO.setEmployeeDTO(employeeDTO);
        leaveDTO.setStatus(leaveDTO.getStatus());
        Leave employeeLeave = employeeLeaveRepository.save(LeaveMapper.mapToEntity(leaveDTO));
        return LeaveMapper.mapToDto(employeeLeave);
    }

    @Override
    public LeaveDTO updateEmployeeLeave(LeaveDTO leaveDTO) {

        Leave returnedEmployeeLeave = employeeLeaveRepository.findById(leaveDTO.getLeaveId())
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_RECORD_NOT_FOUND));

        // Leave status must be in pending
        if (returnedEmployeeLeave.getReviewedBy() != null) {
            throw new DataConflictException(ExceptionConstants.EMPLOYEE_LEAVE_ACTION_ALREADY_TAKEN);
        }
        returnedEmployeeLeave.setFromDate(leaveDTO.getFromDate());
        returnedEmployeeLeave.setToDate(leaveDTO.getToDate());
        returnedEmployeeLeave.setLeaveReason(leaveDTO.getLeaveReason());
        return LeaveMapper.mapToDto(employeeLeaveRepository.save(returnedEmployeeLeave));
    }

    @Override
    public LeaveDTO approveEmployeeLeave(LeaveDTO leaveDTO) {

        Leave returnedEmployeeLeave = employeeLeaveRepository.findById(leaveDTO.getLeaveId())
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_RECORD_NOT_FOUND));
        if (returnedEmployeeLeave.getStatus().equals(LeaveStatus.APPROVED)) {
            throw new DataConflictException(ExceptionConstants.EMPLOYEE_LEAVE_ALREADY_APPROVED);
        }
        if (returnedEmployeeLeave.getStatus().equals(LeaveStatus.DENIED)) {
            throw new DataConflictException(ExceptionConstants.EMPLOYEE_LEAVE_ALREADY_DENIED);
        }
        Employee employeeSupervisor = returnedEmployeeLeave.getEmployee().getSupervisor();
        long approverId = ExtractAuthUser.getCurrentUser().getEmployeeId();
        String approverRole = ExtractAuthUser.getCurrentUser().getRole();

        // Setting Approver Id extracting from ExtractUserAuthentication currentUser id
        Employee approverEmployee = new Employee();
        approverEmployee.setEmployeeId(approverId);

        // Employee cant approve their own request
        if (!approverRole.equals("ROLE_ADMIN") || approverId == returnedEmployeeLeave.getEmployee().getEmployeeId()) {

            if (approverId == returnedEmployeeLeave.getEmployee().getEmployeeId() ||
                    employeeSupervisor == null || employeeSupervisor.getEmployeeId() == null ||
                    approverId != employeeSupervisor.getEmployeeId()) {
                throw new UnauthorizedRequest(ExceptionConstants.YOU_CANT_REVIEW_THIS_REQUEST);
            }
//            throw new UnauthorizedRequest(ExceptionConstants.YOU_CANT_REVIEW_THIS_REQUEST);
        }
        updateLeaveinDb(returnedEmployeeLeave);
        returnedEmployeeLeave.setStatus(StatusMapper.mapLeaveStatus(String.valueOf(leaveDTO.getStatus())));
        returnedEmployeeLeave.setReviewedBy(approverEmployee);
        returnedEmployeeLeave.setDeniedReason(leaveDTO.getDeniedReason());
        return LeaveMapper.mapToDto(employeeLeaveRepository.save(returnedEmployeeLeave));
    }

    private void updateLeaveinDb(Leave leave) {
        try {
            Employee employee = leave.getEmployee();
            int total = employee.getTotalLeave();
            int dateFrom = leave.getFromDate().getDayOfYear();
            int dateTo = leave.getToDate().getDayOfYear();
            int numberOfLeaves = dateTo - dateFrom;
            if (numberOfLeaves > 0) {
                employee.setLeaveConsumed(employee.getLeaveConsumed() + numberOfLeaves);
            } else {
                employee.setLeaveConsumed(employee.getLeaveConsumed() + 1);
            }
            if (employee.getLeaveRemaining() == 0)
                employee.setLeaveRemaining(total - numberOfLeaves);
            else
                employee.setLeaveRemaining(employee.getLeaveRemaining() - numberOfLeaves);
            employeeRepository.save(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public LeaveDTO ChangeEmployeeLeaveStatus(Long id, String status) {

        Leave returnedEmployeeLeave = employeeLeaveRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_RECORD_NOT_FOUND));

        // Leave status must be in pending
        if (returnedEmployeeLeave.getReviewedBy() != null) {
            throw new DataConflictException(ExceptionConstants.EMPLOYEE_LEAVE_ACTION_ALREADY_TAKEN);
        }
        returnedEmployeeLeave.setStatus(StatusMapper.mapLeaveStatus(status));
        return LeaveMapper.mapToDto(employeeLeaveRepository.save(returnedEmployeeLeave));
    }

    @Override
    public List<LeaveDTO> retrieveEmployeeLeaveByDate(String date1, String date2) {
        return employeeLeaveRepository.findLeaveByDate(DateUtil.convertToDate(date1), DateUtil.convertToDate(date2))
                .stream()
                .map(leave -> LeaveMapper.mapToDto(leave))
                .collect(Collectors.toList());
    }
}
