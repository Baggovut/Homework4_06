package ru.skypro.lessons.springboot.weblibrary.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.lessons.springboot.weblibrary.service.ReportService;

@RestController
@RequestMapping("/admin/report")
@AllArgsConstructor
public class ReportAdminController {
    private final ReportService reportService;
    @PostMapping
    public long createReport(){
        return reportService.createReport();
    }
}
