package ru.epa.epabackend.mapper;

import lombok.experimental.UtilityClass;
import ru.epa.epabackend.dto.employee.EmployeeDtoRequest;
import ru.epa.epabackend.dto.employee.EmployeeFullDto;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.model.Employee;

@UtilityClass
public class EmployeeMapper {

    public EmployeeFullDto toEmployeeDtoFull(Employee employee) {
        return EmployeeFullDto.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .nickName(employee.getNickName())
                .city(employee.getCity())
                .email(employee.getEmail())
                .birthday(employee.getBirthday())
                .role(employee.getRole())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .build();
    }

    public Employee toEmployee(EmployeeDtoRequest employeeDtoRequest) {
        return Employee.builder()
                .fullName(employeeDtoRequest.getFullName())
                .nickName(employeeDtoRequest.getNickName())
                .city(employeeDtoRequest.getCity())
                .email(employeeDtoRequest.getEmail())
                .password(employeeDtoRequest.getPassword())
                .birthday(employeeDtoRequest.getBirthday())
                .role(employeeDtoRequest.getRole())
                .position(employeeDtoRequest.getPosition())
                .department(employeeDtoRequest.getDepartment())
                .build();
    }

    public EmployeeShortDto toEmployeeDtoShort(Employee employee) {
        return EmployeeShortDto.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .position(employee.getPosition())
                .build();
    }
}
