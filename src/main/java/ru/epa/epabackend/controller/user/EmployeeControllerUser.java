package ru.epa.epabackend.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeDtoFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoShort;
import ru.epa.epabackend.dto.employee.EmployeeDtoUpdate;
import ru.epa.epabackend.service.EmployeeService;

import java.util.List;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/employees")
public class EmployeeControllerUser {

    private final EmployeeService employeeService;

    @PatchMapping("/{employeeId}")
    public EmployeeDtoFull updateEmployee(@PathVariable Long employeeId,
                                          @Valid @RequestBody EmployeeDtoUpdate employeeDtoUpdate) {
        log.info("PATCH / employees / {}", employeeId);
        return employeeService.updateEmployee(employeeId, employeeDtoUpdate);
    }

    @GetMapping
    public List<EmployeeDtoShort> getAllEmployees() {
        log.info("GET / employees");
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{employeeId}")
    public EmployeeDtoFull getEmployeeById(@PathVariable Long employeeId) {
        log.info("GET / employees / {}", employeeId);
        return employeeService.getEmployeeById(employeeId);
    }
}
