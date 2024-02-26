package ru.epa.epabackend.mapper;

import lombok.experimental.UtilityClass;
import ru.epa.epabackend.dto.EmployeeDto;
import ru.epa.epabackend.dto.EmployeeDtoShort;
import ru.epa.epabackend.exception.WrongFullNameException;
import ru.epa.epabackend.model.Employee;

@UtilityClass
public class EmployeeMapper {

    public EmployeeDto toEmployeeDto(Employee employee) {
        String fullName = employee.getLastName() + " " + employee.getFirstName() + " " + employee.getPatronymic();
        return EmployeeDto.builder()
                .id(employee.getId())
                .fullName(fullName)
                .nik(employee.getNik())
                .city(employee.getCity())
                .login(employee.getLogin())
                .password(employee.getPassword())
                .birthday(employee.getBirthday())
                .role(employee.getRole())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .build();
    }

    public Employee toEmployee(EmployeeDto employeeDto) {
        String[] fullName = employeeDto.getFullName().split(" ");
        if (fullName.length != 3) {
            throw new WrongFullNameException("Поле ФИО должно состоять из трёх слов!");
        }
        return Employee.builder()
                .id(employeeDto.getId())
                .lastName(fullName[0])
                .firstName(fullName[1])
                .patronymic(fullName[2])
                .nik(employeeDto.getNik())
                .city(employeeDto.getCity())
                .login(employeeDto.getLogin())
                .password(employeeDto.getPassword())
                .birthday(employeeDto.getBirthday())
                .role(employeeDto.getRole())
                .position(employeeDto.getPosition())
                .department(employeeDto.getDepartment())
                .build();
    }

    public EmployeeDtoShort toEmployeeDtoShort(Employee employee) {
        return EmployeeDtoShort.builder()
                .fullName(employee.getLastName() + " " + employee.getFirstName())
                .position(employee.getPosition())
                .build();
    }
}
