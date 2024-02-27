package ru.epa.epabackend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseFull;
import ru.epa.epabackend.dto.employee.EmployeeRtoRequest;
import ru.epa.epabackend.service.EmployeeService;

import static ru.epa.epabackend.util.ValidationGroups.Create;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/employees")
public class EmployeeControllerAdmin {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EmployeeDtoResponseFull addEmployee(@Validated(Create.class) @RequestBody EmployeeRtoRequest
                                                           employeeRtoRequest) {
        log.info("POST / employees / {} ", employeeRtoRequest.getFullName());
        return employeeService.addEmployee(employeeRtoRequest);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long employeeId) {
        log.info("DELETE / employees / {}", employeeId);
        employeeService.deleteEmployee(employeeId);
    }
}