package ru.epa.epabackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.EmployeeDto;
import ru.epa.epabackend.dto.EmployeeDtoShort;
import ru.epa.epabackend.exception.WrongFullNameException;
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
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        log.info("Создание нового сотрудника {}", employeeDto.getFullName());
        Employee employee = employeeRepository.save(EmployeeMapper.toEmployee(employeeDto));
        employee.setRole(USER);
        return EmployeeMapper.toEmployeeDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        log.info("Обновление существующего сотрудника {}", employeeDto.getFullName());
        Employee oldEmployee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Employee.class)));
        String fullName = employeeDto.getFullName();
        if (fullName != null && !fullName.isBlank()) {
            String[] full = employeeDto.getFullName().split(" ");
            if (full.length != 3) {
                throw new WrongFullNameException("Поле ФИО должно состоять из трёх слов!");
            }
            oldEmployee.setLastName(full[0]);
            oldEmployee.setFirstName(full[1]);
            oldEmployee.setPatronymic(full[2]);
        }
        String nik = employeeDto.getNik();
        if (nik != null && !nik.isBlank()) {
            oldEmployee.setNik(nik);
        }
        String city = employeeDto.getCity();
        if (city != null && !city.isBlank()) {
            oldEmployee.setCity(city);
        }
        String login = employeeDto.getLogin();
        if (login != null && !login.isBlank()) {
            oldEmployee.setLogin(login);
        }
        String password = employeeDto.getPassword();
        if (password != null && !password.isBlank()) {
            oldEmployee.setPassword(password);
        }
        LocalDate birthday = employeeDto.getBirthday();
        if (birthday != null) {
            oldEmployee.setBirthday(birthday);
        }
        Role role = employeeDto.getRole();
        if (role != null) {
            oldEmployee.setRole(role);
        }
        String position = employeeDto.getPosition();
        if (position != null && !position.isBlank()) {
            oldEmployee.setPosition(position);
        }
        String department = employeeDto.getDepartment();
        if (department != null && !department.isBlank()) {
            oldEmployee.setDepartment(department);
        }
        return EmployeeMapper.toEmployeeDto(oldEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        log.info("Удаление сотрудника по идентификатору {}", employeeId);
        employeeRepository.deleteById(employeeId);
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
    public EmployeeDto getEmployeeById(Long employeeId) {
        log.info("Получение сотрудника по идентификатору {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Employee.class)));
        return EmployeeMapper.toEmployeeDto(employee);
    }
}
