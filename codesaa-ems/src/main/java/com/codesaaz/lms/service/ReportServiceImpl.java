package com.codesaaz.lms.service;

import com.codesaaz.lms.repository.LeaveRepository;
import com.codesaaz.lms.dto.LeaveReportDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final LeaveRepository leaveRepository;

    public ReportServiceImpl(final LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public List<LeaveReportDTO> retrieveLeaveReports() {
        return leaveRepository.generateLeaveReport();
    }
}
