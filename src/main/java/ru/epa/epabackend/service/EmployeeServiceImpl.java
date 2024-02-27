package ru.epa.epabackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeDtoFull;
import ru.epa.epabackend.dto.employee.EmployeeDtoNew;
import ru.epa.epabackend.dto.employee.EmployeeDtoShort;
import ru.epa.epabackend.dto.employee.EmployeeDtoUpdate;
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
    public EmployeeDtoFull addEmployee(EmployeeDtoNew employeeDtoNew) {
        log.info("Создание нового сотрудника {}", employeeDtoNew.getFullName());
        Employee employee = employeeRepository.save(EmployeeMapper.toEmployee(employeeDtoNew));
        employee.setRole(USER);
        return EmployeeMapper.toEmployeeDtoFull(employee);
    }

    @Override
    public EmployeeDtoFull updateEmployee(Long employeeId, EmployeeDtoUpdate employeeDtoUpdate) {
        log.info("Обновление существующего сотрудника {}", employeeDtoUpdate.getFullName());
        Employee oldEmployee = getEmployee(employeeId);
        String fullName = employeeDtoUpdate.getFullName();
        if (fullName != null && !fullName.isBlank()) {
            String[] full = employeeDtoUpdate.getFullName().split(" ");
            if (full.length != 3) {
                throw new WrongFullNameException("Поле ФИО должно состоять из трёх слов!");
            }
            oldEmployee.setLastName(full[0]);
            oldEmployee.setFirstName(full[1]);
            oldEmployee.setPatronymic(full[2]);
        }

        updateEmployeeFields(oldEmployee, employeeDtoUpdate);

        Role role = employeeDtoUpdate.getRole();
        if (role != null) {
            oldEmployee.setRole(role);
        }
        String position = employeeDtoUpdate.getPosition();
        if (position != null && !position.isBlank()) {
            oldEmployee.setPosition(position);
        }
        String department = employeeDtoUpdate.getDepartment();
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
    public List<EmployeeDtoShort> getAllEmployees() {
        log.info("Получение всех сотрудников");
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toEmployeeDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDtoFull getEmployeeById(Long employeeId) {
        log.info("Получение сотрудника по идентификатору {}", employeeId);
        Employee employee = getEmployee(employeeId);
        return EmployeeMapper.toEmployeeDtoFull(employee);
    }

    private Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Employee.class)));
    }

    private void updateEmployeeFields(Employee oldEmployee, EmployeeDtoUpdate employeeDtoUpdate) {
        String nickName = employeeDtoUpdate.getNickName();
        if (nickName != null && !nickName.isBlank()) {
            oldEmployee.setNickName(nickName);
        }
        String city = employeeDtoUpdate.getCity();
        if (city != null && !city.isBlank()) {
            oldEmployee.setCity(city);
        }
        String login = employeeDtoUpdate.getLogin();
        if (login != null && !login.isBlank()) {
            oldEmployee.setLogin(login);
        }
        String password = employeeDtoUpdate.getPassword();
        if (password != null && !password.isBlank()) {
            oldEmployee.setPassword(password);
        }
        LocalDate birthday = employeeDtoUpdate.getBirthday();
        if (birthday != null) {
            oldEmployee.setBirthday(birthday);
        }
    }
}
