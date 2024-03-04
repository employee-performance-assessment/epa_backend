package ru.epa.epabackend.mapper;

import lombok.experimental.UtilityClass;
import ru.epa.epabackend.dto.employee.EmployeeFullDto;
import ru.epa.epabackend.dto.employee.EmployeeRtoRequest;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.exception.exceptions.WrongFullNameException;
import ru.epa.epabackend.model.Employee;

@UtilityClass
public class EmployeeMapper {

    public EmployeeFullDto toEmployeeDtoFull(Employee employee) {
        String fullName = employee.getLastName() + " " + employee.getFirstName() + " " + employee.getPatronymic();
        return EmployeeFullDto.builder()
                .id(employee.getId())
                .fullName(fullName)
                .nickName(employee.getNickName())
                .city(employee.getCity())
                .email(employee.getEmail())
                .birthday(employee.getBirthday())
                .role(employee.getRole())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .build();
    }

    public Employee toEmployee(EmployeeRtoRequest employeeRtoRequest) {
        String[] fullName = employeeRtoRequest.getFullName().split(" ");
        if (fullName.length != 3) {
            throw new WrongFullNameException("Поле ФИО должно состоять из трёх слов!");
        }
        return Employee.builder()
                .lastName(fullName[0])
                .firstName(fullName[1])
                .patronymic(fullName[2])
                .nickName(employeeRtoRequest.getNickName())
                .city(employeeRtoRequest.getCity())
                .email(employeeRtoRequest.getEmail())
                .password(employeeRtoRequest.getPassword())
                .birthday(employeeRtoRequest.getBirthday())
                .role(employeeRtoRequest.getRole())
                .position(employeeRtoRequest.getPosition())
                .department(employeeRtoRequest.getDepartment())
                .build();
    }

    public EmployeeShortDto toEmployeeDtoShort(Employee employee) {
        return EmployeeShortDto.builder()
                .id(employee.getId())
                .fullName(employee.getLastName() + " " + employee.getFirstName())
                .position(employee.getPosition())
                .build();
    }
}
