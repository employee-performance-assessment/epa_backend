package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseShort;
import ru.epa.epabackend.dto.employee.EmployeeRtoRequest;
import ru.epa.epabackend.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    default String setFullName(Employee employee) {
        StringBuilder fullName = new StringBuilder();
        fullName.append(employee.getLastName())
                .append(" ")
                .append(employee.getFirstName());
        return fullName.toString();
    }

    @Mapping(target = "fullName", expression = "java(setFullName(employee))")
    EmployeeDtoResponseFull  mapToFullDto(Employee employee);

    default String setLastName(EmployeeRtoRequest employeeRtoRequest) {
        String[] fullName = employeeRtoRequest.getFullName().split(" ");
        return fullName[0];
    }

    default String setFirstName(EmployeeRtoRequest employeeRtoRequest) {
        String[] fullName = employeeRtoRequest.getFullName().split(" ");
        return fullName[1];
    }

    default String setPatronymic(EmployeeRtoRequest employeeRtoRequest) {
        String[] fullName = employeeRtoRequest.getFullName().split(" ");
        return fullName[2];
    }

    @Mapping(target = "lastName", expression = "java(setLastName(employeeRtoRequest))")
    @Mapping(target = "firstName", expression = "java(setFirstName(employeeRtoRequest))")
    @Mapping(target = "patronymic", expression = "java(setPatronymic(employeeRtoRequest))")
    Employee mapToEntity(EmployeeRtoRequest employeeRtoRequest);

    @Mapping(target = "fullName", expression = "java(setFullName(employee))")
    EmployeeDtoResponseShort mapToShortDto(Employee employee);

}