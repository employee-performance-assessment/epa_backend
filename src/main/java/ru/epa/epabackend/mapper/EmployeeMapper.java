package ru.epa.epabackend.mapper;

import lombok.experimental.UtilityClass;
import ru.epa.epabackend.dto.employee.EmployeeDtoFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoNew;
import ru.epa.epabackend.dto.employee.EmployeeDtoShort;
import ru.epa.epabackend.exception.exceptions.WrongFullNameException;
import ru.epa.epabackend.model.Employee;

@UtilityClass
public class EmployeeMapper {

    public EmployeeDtoFull toEmployeeDtoFull(Employee employee) {
        String fullName = employee.getLastName() + " " + employee.getFirstName() + " " + employee.getPatronymic();
        return EmployeeDtoFull.builder()
                .id(employee.getId())
                .fullName(fullName)
                .nickName(employee.getNickName())
                .city(employee.getCity())
                .login(employee.getLogin())
                .password(employee.getPassword())
                .birthday(employee.getBirthday())
                .role(employee.getRole())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .build();
    }

    public Employee toEmployee(EmployeeDtoNew employeeDtoNew) {
        String[] fullName = employeeDtoNew.getFullName().split(" ");
        if (fullName.length != 3) {
            throw new WrongFullNameException("Поле ФИО должно состоять из трёх слов!");
        }
        return Employee.builder()
                .lastName(fullName[0])
                .firstName(fullName[1])
                .patronymic(fullName[2])
                .nickName(employeeDtoNew.getNickName())
                .city(employeeDtoNew.getCity())
                .login(employeeDtoNew.getLogin())
                .password(employeeDtoNew.getPassword())
                .birthday(employeeDtoNew.getBirthday())
                .role(employeeDtoNew.getRole())
                .position(employeeDtoNew.getPosition())
                .department(employeeDtoNew.getDepartment())
                .build();
    }

    public EmployeeDtoShort toEmployeeDtoShort(Employee employee) {
        return EmployeeDtoShort.builder()
                .fullName(employee.getLastName() + " " + employee.getFirstName())
                .position(employee.getPosition())
                .build();
    }
}
