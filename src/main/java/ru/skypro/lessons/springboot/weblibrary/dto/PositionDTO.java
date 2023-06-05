package ru.skypro.lessons.springboot.weblibrary.dto;

import lombok.Data;
import ru.skypro.lessons.springboot.weblibrary.model.Employee;
import ru.skypro.lessons.springboot.weblibrary.model.Position;

import java.util.List;

@Data
public class PositionDTO {
    private Integer id;
    private String positionName;
    List<Employee> employeeList;

    public static PositionDTO fromPosition(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setPositionName(position.getPositionName());
        positionDTO.setEmployeeList(position.getEmployeeList());
        return positionDTO;
    }

    public Position toPosition() {
        Position position = new Position();
        position.setId(this.getId());
        position.setPositionName(this.getPositionName());
        position.setEmployeeList(this.getEmployeeList());
        return position;
    }
}
