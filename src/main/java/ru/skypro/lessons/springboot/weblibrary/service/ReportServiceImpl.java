package ru.skypro.lessons.springboot.weblibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.dto.PositionDTO;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IdNotFoundException;
import ru.skypro.lessons.springboot.weblibrary.model.Report;
import ru.skypro.lessons.springboot.weblibrary.pojo.PojoReport;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.weblibrary.repository.PositionRepository;
import ru.skypro.lessons.springboot.weblibrary.repository.ReportRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final ReportRepository reportRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    @Override
    public long createReport() {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeRepository.findAll().forEach(e->employeeDTOList.add(EmployeeDTO.fromEmployee(e)));

        List<PositionDTO> positionDTOList = new ArrayList<>();
        positionRepository.findAll().forEach(p->positionDTOList.add(PositionDTO.fromPosition(p)));

        List<PojoReport> pojoReportList = new ArrayList<>();

        for(int positionNumber = 0; positionNumber < positionDTOList.size();positionNumber++){
            final int finalPositionNumber = positionNumber;
            List<EmployeeDTO> filteredEmployeeDTOList = employeeDTOList
                    .stream()
                    .filter(
                            e-> Objects.equals(e.getPosition().getId(), positionDTOList.get(finalPositionNumber).getId())
                    ).toList();

            int employeesCounter = filteredEmployeeDTOList.size();
            double minSalary = filteredEmployeeDTOList.stream().min(Comparator.comparing(EmployeeDTO::getSalary)).orElseThrow().getSalary();
            double maxSalary = filteredEmployeeDTOList.stream().max(Comparator.comparing(EmployeeDTO::getSalary)).orElseThrow().getSalary();
            double averageSalary = filteredEmployeeDTOList.stream().mapToDouble(EmployeeDTO::getSalary).sum()/employeesCounter;

            pojoReportList.add(
                    new PojoReport(positionDTOList.get(finalPositionNumber).getId(),
                            employeesCounter,
                            positionDTOList.get(finalPositionNumber).getPositionName(),
                            minSalary,
                            maxSalary,
                            averageSalary
                    )
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonReport;
        try {
            jsonReport = objectMapper.writeValueAsString(pojoReportList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Report report = new Report();
        report.setReportJson(jsonReport);
        reportRepository.save(report);

        return report.getId();
    }

    @Override
    public ResponseEntity<Resource> downloadReport(Integer id) {
        if(id < 1){
            throw new IdNotFoundException("id не может быть меньше 1");
        }

        String fileName = "report_"+id+".json";

        String json = reportRepository.findById(id).orElseThrow(
                ()-> new IdNotFoundException("Отчёт с id="+id+" не найден")
        ).getReportJson();

        Resource resource = new ByteArrayResource(json.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+fileName+"\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE )
                .body(resource);
    }
}
