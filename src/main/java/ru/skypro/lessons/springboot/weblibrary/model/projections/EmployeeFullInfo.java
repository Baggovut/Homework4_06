package ru.skypro.lessons.springboot.weblibrary.model.projections;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeFullInfo {
    private String name;
    private Double salary;
    private String positionName;
}
