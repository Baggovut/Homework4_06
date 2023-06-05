package ru.skypro.lessons.springboot.weblibrary.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeService;

@RestController
@RequestMapping("/admin/employees")
@AllArgsConstructor
public class EmployeeAdminController {
    private final EmployeeService employeeService;

    @PostMapping("/")
    public void addEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeService.addEmployee(employeeDTO);
    }

    @PutMapping("/{id}")
    public void editEmployeeById(@PathVariable(name = "id") Integer id,@RequestBody EmployeeDTO employeeDTO){
        employeeService.editEmployeeById(id,employeeDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable(name = "id") Integer id){
        employeeService.deleteEmployeeById(id);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadEmployees(@RequestParam("file") MultipartFile file){
        employeeService.uploadEmployees(file);
    }
}
