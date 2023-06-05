package ru.skypro.lessons.springboot.weblibrary.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.weblibrary.service.ReportService;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable(name = "id")Integer id){
        return reportService.downloadReport(id);
    }
}
