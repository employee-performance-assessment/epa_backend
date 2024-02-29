package ru.epa.epabackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.*;
import ru.epa.epabackend.exception.exceptions.WrongFullNameException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.epa.epabackend.util.Role.USER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDtoResponseFull addEmployee(EmployeeRtoRequest employeeRtoRequest) {
        log.info("Создание нового сотрудника {}", employeeRtoRequest.getFullName());
        Employee employee = employeeRepository.save(EmployeeMapper.toEmployee(employeeRtoRequest));
        employee.setRole(USER);
        return EmployeeMapper.toEmployeeDtoFull(employee);
    }

    @Override
    public EmployeeDtoResponseFull updateEmployee(Long employeeId, EmployeeRtoRequest employeeRtoRequest) {
        log.info("Обновление существующего сотрудника {}", employeeRtoRequest.getFullName());
        Employee oldEmployee = getEmployee(employeeId);
        String fullName = employeeRtoRequest.getFullName();
        if (fullName != null && !fullName.isBlank()) {
            String[] full = employeeRtoRequest.getFullName().split(" ");
            if (full.length != 3) {
                throw new WrongFullNameException("Поле ФИО должно состоять из трёх слов!");
            }
            oldEmployee.setLastName(full[0]);
            oldEmployee.setFirstName(full[1]);
            oldEmployee.setPatronymic(full[2]);
        }

        updateEmployeeFields(oldEmployee, employeeRtoRequest);

        Role role = employeeRtoRequest.getRole();
        if (role != null) {
            oldEmployee.setRole(role);
        }
        String position = employeeRtoRequest.getPosition();
        if (position != null && !position.isBlank()) {
            oldEmployee.setPosition(position);
        }
        String department = employeeRtoRequest.getDepartment();
        if (department != null && !department.isBlank()) {
            oldEmployee.setDepartment(department);
        }
        return EmployeeMapper.toEmployeeDtoFull(oldEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        log.info("Удаление сотрудника по идентификатору {}", employeeId);
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        } else {
            throw new EntityNotFoundException(String.format("Объект класса %s не найден", Employee.class));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDtoResponseShort> getAllEmployees() {
        log.info("Получение всех сотрудников");
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toEmployeeDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDtoResponseFull getEmployeeById(Long employeeId) {
        log.info("Получение сотрудника по идентификатору {}", employeeId);
        Employee employee = getEmployee(employeeId);
        return EmployeeMapper.toEmployeeDtoFull(employee);
    }

    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Employee.class)));
    }

    private void updateEmployeeFields(Employee oldEmployee, EmployeeRtoRequest employeeRtoRequest) {
        String nickName = employeeRtoRequest.getNickName();
        if (nickName != null && !nickName.isBlank()) {
            oldEmployee.setNickName(nickName);
        }
        String city = employeeRtoRequest.getCity();
        if (city != null && !city.isBlank()) {
            oldEmployee.setCity(city);
        }
        String login = employeeRtoRequest.getLogin();
        if (login != null && !login.isBlank()) {
            oldEmployee.setLogin(login);
        }
        String password = employeeRtoRequest.getPassword();
        if (password != null && !password.isBlank()) {
            oldEmployee.setPassword(password);
        }
        LocalDate birthday = employeeRtoRequest.getBirthday();
        if (birthday != null) {
            oldEmployee.setBirthday(birthday);
        }
    }
}
