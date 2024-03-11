package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.employee.EmployeeCreateUpdateFindByIdResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeCreateUpdateRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeFindAllResponseDto;
import ru.epa.epabackend.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeCreateUpdateFindByIdResponseDto mapToFullDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Employee mapToEntity(EmployeeCreateUpdateRequestDto employeeRtoRequest);

    EmployeeFindAllResponseDto mapToShortDto(Employee employee);
}
