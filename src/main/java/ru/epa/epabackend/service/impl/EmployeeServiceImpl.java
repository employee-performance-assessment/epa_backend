package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.exception.exceptions.WrongFullNameException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.epa.epabackend.util.Role.ROLE_ADMIN;
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
    public Employee create(EmployeeRequestDto employeeDtoRequest) {
        log.info("Создание нового сотрудника {}", employeeDtoRequest.getFullName());
        Employee employeeToSave = employeeMapper.mapToEntity(employeeDtoRequest);
        employeeToSave.setPassword(passwordEncoder.encode(employeeDtoRequest.getPassword()));
        employeeToSave.setRole(ROLE_USER);
        return employeeRepository.save(employeeToSave);
    }

    @Override
    public Employee createSelfRegister(
            EmployeeRequestDto employeeRtoRequest) {
        log.info("Создание нового сотрудника {}", employeeRtoRequest.getFullName());
        Employee employeeToSave = employeeMapper.mapToEntity(employeeRtoRequest);
        employeeToSave.setPassword(passwordEncoder.encode(employeeRtoRequest.getPassword()));
        employeeToSave.setRole(ROLE_ADMIN);
        return employeeRepository.save(employeeToSave);
    }

    @Override
    public Employee update(
            Long employeeId, EmployeeRequestDto employeeDtoRequest) {
        log.info("Обновление существующего сотрудника {}", employeeDtoRequest.getFullName());
        Employee oldEmployee = findById(employeeId);
        String fullName = employeeDtoRequest.getFullName();
        if (fullName != null && !fullName.isBlank()) {
            String[] full = employeeDtoRequest.getFullName().split(" ");
            if (full.length != 3) {
                throw new WrongFullNameException("Поле ФИО должно состоять из трёх слов!");
            }
            oldEmployee.setFullName(fullName);
        }

        updateFields(oldEmployee, employeeDtoRequest);

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
        return employeeRepository.save(oldEmployee);
    }

    @Override
    public void delete(Long employeeId) {
        log.info("Удаление сотрудника по идентификатору {}", employeeId);
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        } else {
            throw new EntityNotFoundException(String.format("Объект класса %s не найден", Employee.class));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        log.info("Получение всех сотрудников");
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByIdDto(Long employeeId) {
        log.info("Получение сотрудника по идентификатору {}", employeeId);
        return findById(employeeId);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::findByEmail;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Неверный email, oбъект класса %s не " +
                        "найден", Employee.class)));
    }

    @Override
    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект класса %s не найден",
                        Employee.class)));
    }

    private void updateFields(Employee oldEmployee, EmployeeRequestDto employeeDtoRequest) {
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