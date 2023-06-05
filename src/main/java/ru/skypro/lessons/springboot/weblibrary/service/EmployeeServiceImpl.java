package ru.skypro.lessons.springboot.weblibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IdNotFoundException;
import ru.skypro.lessons.springboot.weblibrary.model.Employee;
import ru.skypro.lessons.springboot.weblibrary.model.Position;
import ru.skypro.lessons.springboot.weblibrary.model.projections.EmployeeFullInfo;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeePagingRepository;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.weblibrary.repository.PositionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final EmployeePagingRepository employeePagingRepository;


    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        employeeRepository.findAll().forEach(employeeList::add);
        return  employeeList.stream()
                .map(EmployeeDTO::fromEmployee).collect(Collectors.toList());
    }

    @Override
    public double getSalarySum() {
        return getAllEmployees()
                .stream()
                .mapToDouble(EmployeeDTO::getSalary)
                .sum();
    }

    @Override
    public EmployeeDTO getEmployeeWithMinSalary() {
        return getAllEmployees()
                .stream()
                .min(Comparator.comparing(EmployeeDTO::getSalary))
                .orElseThrow();
    }

    @Override
    public List<EmployeeDTO> getEmployeeWithMaxSalary() {
        EmployeeDTO singleEmployeeWithMaxSalary = getAllEmployees()
                .stream()
                .max(Comparator.comparing(EmployeeDTO::getSalary))
                .orElseThrow();
        return getAllEmployees()
                .stream()
                .filter(e -> Objects.equals(e.getSalary(), singleEmployeeWithMaxSalary.getSalary()))
                .toList();
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithHighSalary() {
        double averageSalary = getSalarySum()/getAllEmployees().size();
        return getAllEmployees()
                .stream()
                .filter(e -> e.getSalary() >= averageSalary)
                .toList();
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Position existedPosition = positionRepository.findPositionByPositionName(employeeDTO.getPosition().getPositionName());

        if(existedPosition != null){
            employeeDTO.setPosition(existedPosition);
        }

        employeeRepository.save(employeeDTO.toEmployee());
    }

    @Override
    public void editEmployeeById(Integer id, EmployeeDTO employeeDTO) {
        if(id < 1){
            throw new IdNotFoundException("id не может быть меньше 1");
        }
        EmployeeDTO existedEmployee = getEmployeeById(id);
        if(existedEmployee != null){
            employeeDTO.setId(id);
            addEmployee(employeeDTO);
        }
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {
        if(id < 1){
            throw new IdNotFoundException("id не может быть меньше 1");
        }
        return EmployeeDTO.fromEmployee(
                employeeRepository
                        .findById(id)
                        .orElseThrow(()-> new IdNotFoundException("Сотрудник с id="+id+" не найден"))
        );
    }

    @Override
    public void deleteEmployeeById(Integer id) {
        if(id < 1){
            throw new IdNotFoundException("id не может быть меньше 1");
        }

        EmployeeDTO existedEmployee = getEmployeeById(id);
        if(existedEmployee != null){
            employeeRepository.deleteById(id);
        }
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithSalaryHigherThan(Integer salary) {
        return getAllEmployees()
                .stream()
                .filter(e -> e.getSalary() > salary)
                .toList();
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithPosition(Integer positionId) {
        if(positionId == null){
            return getAllEmployees();
        }
        if(positionId < 1){
            throw new IdNotFoundException("position_id не может быть меньше 1");
        }
        Position existedPosition = positionRepository
                .findById(positionId)
                .orElseThrow(
                        () -> new IdNotFoundException("Должность с position_id="+positionId+" не найдена")
                );

        return existedPosition.getEmployeeList()
                .stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeFullInfo> getEmployeeFullInfoById(Integer id) {
        return employeeRepository.findEmployeeByIdFullInfo(id);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByPage(Integer pageIndex) {
        int unitsPerPage = 10;
        if (pageIndex == null || pageIndex < 0){
            pageIndex = 0;
        }

        Pageable employeeOfConcretePage = PageRequest.of(pageIndex, unitsPerPage);
        Page<Employee> page = employeePagingRepository.findAll(employeeOfConcretePage);

        return page
                .stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public void uploadEmployees(MultipartFile file) {
        String json = null;
        try(InputStream inputStream = file.getInputStream()) {
            byte[] bytes = new byte[inputStream.available()];
            if(inputStream.read(bytes) != -1){
                json = new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<EmployeeDTO> employeeDTOList;

        try {
            employeeDTOList = objectMapper.readValue(
                    json,
                    new TypeReference<>() {
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for (EmployeeDTO e : employeeDTOList){
            addEmployee(e);
        }
    }
}
