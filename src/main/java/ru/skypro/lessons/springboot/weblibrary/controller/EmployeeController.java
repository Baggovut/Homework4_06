package ru.skypro.lessons.springboot.weblibrary.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.model.projections.EmployeeFullInfo;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
    @GetMapping("/salary/sum")
    public double getSalarySum() {
        return employeeService.getSalarySum();
    }
    @GetMapping("/salary/min")
    public EmployeeDTO getEmployeeWithMinSalary() {
        return employeeService.getEmployeeWithMinSalary();
    }
    @GetMapping("/high-salary")
    public List<EmployeeDTO> getEmployeesWithHighSalary() {
        return employeeService.getEmployeesWithHighSalary();
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable(name = "id") Integer id){
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/salaryHigherThan")
    public List<EmployeeDTO> getEmployeesWithSalaryHigherThan(@RequestParam("salary") Integer salary){
        return employeeService.getEmployeesWithSalaryHigherThan(salary);
    }
    @GetMapping("/withHighestSalary")
    public List<EmployeeDTO> getEmployeeWithMaxSalary() {
        return employeeService.getEmployeeWithMaxSalary();
    }

    @GetMapping
    public List<EmployeeDTO> getEmployeesWithPosition(@RequestParam(name="position",required = false) Integer positionId){
        return employeeService.getEmployeesWithPosition(positionId);
    }

    @GetMapping("{id}/fullInfo")
    public List<EmployeeFullInfo> getEmployeeFullInfoById(@PathVariable(name = "id") Integer id){
        return employeeService.getEmployeeFullInfoById(id);
    }

    @GetMapping("/page")
    public List<EmployeeDTO> getEmployeesByPage(@RequestParam(name="page",required = false) Integer pageIndex){
        return employeeService.getEmployeesByPage(pageIndex);
    }
}
