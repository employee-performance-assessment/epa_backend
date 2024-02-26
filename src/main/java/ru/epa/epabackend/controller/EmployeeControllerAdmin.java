package ru.epa.epabackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.EmployeeDto;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.util.Create;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("admin/employees")
public class EmployeeControllerAdmin {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EmployeeDto addEmployee(@Validated(Create.class) @RequestBody EmployeeDto employeeDto) {
        log.info("POST / employees / {} ", employeeDto.getFullName());
        return employeeService.addEmployee(employeeDto);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long employeeId) {
        log.info("DELETE / employees / {}", employeeId);
        employeeService.deleteEmployee(employeeId);
    }


}
