package ru.skypro.lessons.springboot.weblibrary.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PojoReport {
    private int id, employeeCounter;
    private String positionName;
    private double minSalary, maxSalary, averageSalary;
}
