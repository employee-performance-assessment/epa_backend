package ru.epa.epabackend.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.epa.epabackend.model.Technology;
import ru.epa.epabackend.util.Role;

import java.util.Set;

@Data
@AllArgsConstructor
public class EmployeeForListDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronimic;
    private Role role;
    private Set<Technology> technologies;
    private String nickName;
}