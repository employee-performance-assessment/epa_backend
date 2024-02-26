package ru.epa.epabackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.EmployeeDto;
import ru.epa.epabackend.dto.EmployeeDtoShort;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.util.Update;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/private/employees")
public class EmployeeControllerPrivate {

    private final EmployeeService employeeService;

    @PatchMapping("/{employeeId}")
    public EmployeeDto updateEmployee(@PathVariable Long employeeId,
                                      @Validated(Update.class) @RequestBody EmployeeDto employeeDto) {
        log.info("PATCH / employees / {}", employeeId);
        return employeeService.updateEmployee(employeeId, employeeDto);
    }

    @GetMapping
    public List<EmployeeDtoShort> getAllUsers() {
        log.info("GET / employees");
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{employeeId}")
    public EmployeeDto getUserById(@PathVariable Long employeeId) {
        log.info("GET / employees / {}", employeeId);
        return employeeService.getEmployeeById(employeeId);
    }
}
