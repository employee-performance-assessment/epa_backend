package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeDtoRequest;
import ru.epa.epabackend.dto.employee.EmployeeFullDto;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.exception.exceptions.WrongFullNameException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.epa.epabackend.util.Role.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;

    @Override

    public EmployeeFullDto addEmployee(EmployeeDtoRequest employeeRtoRequest) {
        log.info("Создание нового сотрудника {}", employeeRtoRequest.getFullName());
        Employee employee = employeeRepository.save(employeeMapper.mapToEntity(employeeRtoRequest));
        employee.setPassword(passwordEncoder.encode(employeeRtoRequest.getPassword()));
        employee.setRole(ROLE_USER);
        return employeeMapper.mapToFullDto(employee);
    }

    @Override
    public EmployeeFullDto updateEmployee(Long employeeId, EmployeeDtoRequest employeeDtoRequest) {
        log.info("Обновление существующего сотрудника {}", employeeDtoRequest.getFullName());
        Employee oldEmployee = getEmployee(employeeId);
        String fullName = employeeDtoRequest.getFullName();
        if (fullName != null && !fullName.isBlank()) {
            String[] full = employeeDtoRequest.getFullName().split(" ");
            if (full.length != 3) {
                throw new WrongFullNameException("Поле ФИО должно состоять из трёх слов!");
            }
            oldEmployee.setLastName(full[0]);
            oldEmployee.setFirstName(full[1]);
            oldEmployee.setPatronymic(full[2]);
        }

        updateEmployeeFields(oldEmployee, employeeDtoRequest);

        Role role = employeeDtoRequest.getRole();
        if (role != null) {
            oldEmployee.setRole(role);
        }
        String position = employeeDtoRequest.getPosition();
        if (position != null && !position.isBlank()) {
            oldEmployee.setPosition(position);
        }
        String department = employeeDtoRequest.getDepartment();
        if (department != null && !department.isBlank()) {
            oldEmployee.setDepartment(department);
        }
        return employeeMapper.mapToFullDto(oldEmployee);
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
    public List<EmployeeShortDto> getAllEmployees() {
        log.info("Получение всех сотрудников");
        return employeeRepository.findAll().stream().map(employeeMapper::mapToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeFullDto getEmployeeById(Long employeeId) {
        log.info("Получение сотрудника по идентификатору {}", employeeId);
        Employee employee = getEmployee(employeeId);
        return employeeMapper.mapToFullDto(employee);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getEmployeeByEmail;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Неверный email"));
    }

    @Override
    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Employee.class)));
    }

    private void updateEmployeeFields(Employee oldEmployee, EmployeeDtoRequest employeeDtoRequest) {
        String nickName = employeeDtoRequest.getNickName();
        if (nickName != null && !nickName.isBlank()) {
            oldEmployee.setNickName(nickName);
        }
        String city = employeeDtoRequest.getCity();
        if (city != null && !city.isBlank()) {
            oldEmployee.setCity(city);
        }
        String email = employeeDtoRequest.getEmail();
        if (email != null && !email.isBlank()) {
            oldEmployee.setEmail(email);
        }
        String password = employeeDtoRequest.getPassword();
        if (password != null && !password.isBlank()) {
            oldEmployee.setPassword(passwordEncoder.encode(password));
        }
        LocalDate birthday = employeeDtoRequest.getBirthday();
        if (birthday != null) {
            oldEmployee.setBirthday(birthday);
        }
    }
}
