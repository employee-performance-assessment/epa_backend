package ru.epa.epabackend.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseShort;
import ru.epa.epabackend.dto.employee.EmployeeRtoRequest;
import ru.epa.epabackend.service.EmployeeService;

import java.util.List;

import static ru.epa.epabackend.util.ValidationGroups.Update;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/employees")
public class EmployeeControllerUser {

    private final EmployeeService employeeService;

    @PatchMapping("/{employeeId}")
    public EmployeeDtoResponseFull updateEmployee(@PathVariable Long employeeId,
                                                  @Validated(Update.class) @RequestBody EmployeeRtoRequest
                                                          employeeRtoRequest) {
        log.info("PATCH / employees / {}", employeeId);
        return employeeService.updateEmployee(employeeId, employeeRtoRequest);
    }

    @GetMapping
    public List<EmployeeDtoResponseShort> getAllEmployees() {
        log.info("GET / employees");
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{employeeId}")
    public EmployeeDtoResponseFull getEmployeeById(@PathVariable Long employeeId) {
        log.info("GET / employees / {}", employeeId);
        return employeeService.getEmployeeById(employeeId);
    }
}
