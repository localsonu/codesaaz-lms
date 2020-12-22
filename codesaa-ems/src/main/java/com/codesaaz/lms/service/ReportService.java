package com.codesaaz.lms.service;

import com.codesaaz.lms.dto.LeaveReportDTO;

import java.util.List;

public interface ReportService {

    List<LeaveReportDTO> retrieveLeaveReports();
}
