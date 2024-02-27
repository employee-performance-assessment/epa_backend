package ru.epa.epabackend.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeDtoFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoNew;
import ru.epa.epabackend.service.EmployeeService;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/employees")
public class EmployeeControllerAdmin {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EmployeeDtoFull addEmployee(@Valid @RequestBody EmployeeDtoNew employeeDtoNew) {
        log.info("POST / employees / {} ", employeeDtoNew.getFullName());
        return employeeService.addEmployee(employeeDtoNew);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long employeeId) {
        log.info("DELETE / employees / {}", employeeId);
        employeeService.deleteEmployee(employeeId);
    }
}