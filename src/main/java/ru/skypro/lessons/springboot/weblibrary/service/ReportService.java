package ru.skypro.lessons.springboot.weblibrary.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    long createReport();
    ResponseEntity<Resource> downloadReport(Integer id);
}
