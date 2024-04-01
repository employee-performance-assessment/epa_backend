package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.RequestEmployeeDto;
import ru.epa.epabackend.dto.employee.RequestEmployeeShortDto;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.service.EmployeeService;

import java.util.List;

import static ru.epa.epabackend.util.Role.ROLE_ADMIN;
import static ru.epa.epabackend.util.Role.ROLE_USER;

/**
 * Класс EmployeeServiceImpl содержит бизнес-логику работы с сотрудниками
 *
 * @author Валентина Вахламова
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;

    /**
     * Создание нового сотрудника
     */
    @Override
    public Employee create(RequestEmployeeDto requestEmployeeDto, String email) {
        log.info("Создание нового сотрудника {}", requestEmployeeDto.getFullName());
        Employee employeeToSave = employeeMapper.mapToEntity(requestEmployeeDto);
        employeeToSave.setPassword(passwordEncoder.encode(requestEmployeeDto.getPassword()));
        employeeToSave.setRole(ROLE_USER);
        Employee admin = findByEmail(email);
        employeeToSave.setCreator(admin);
        return employeeRepository.save(employeeToSave);
    }

    /**
     * Саморегистрация администратора
     */
    @Override
    public Employee createSelfRegister(RequestEmployeeShortDto requestEmployeeShortDto) {
        log.info("Создание нового сотрудника {}", requestEmployeeShortDto.getFullName());
        Employee employeeToSave = employeeMapper.mapToEntity(requestEmployeeShortDto);
        employeeToSave.setPassword(passwordEncoder.encode(requestEmployeeShortDto.getPassword()));
        employeeToSave.setRole(ROLE_ADMIN);
        return employeeRepository.save(employeeToSave);
    }

    /**
     * Обновление сотрудника
     */
    @Override
    public Employee update(Long employeeId, RequestEmployeeDto requestEmployeeDto) {
        log.info("Обновление существующего сотрудника {}", requestEmployeeDto.getFullName());
        Employee oldEmployee = findById(employeeId);
        String password = requestEmployeeDto.getPassword();

        if (password != null && !password.isBlank()) {
            oldEmployee.setPassword(passwordEncoder.encode(password));
        }

        employeeMapper.updateFields(requestEmployeeDto, oldEmployee);
        return employeeRepository.save(oldEmployee);
    }

    /**
     * Удаление сотрудника
     */
    @Override
    public void delete(Long employeeId) {
        log.info("Удаление сотрудника по идентификатору {}", employeeId);
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        } else {
            throw new EntityNotFoundException(String.format("Сотрудник с id %s не найден", employeeId));
        }
    }

    /**
     * Получение всех сотрудников
     */
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        log.info("Получение всех сотрудников");
        return employeeRepository.findAll();
    }

    /**
     * Получение сотрудника по id
     */
    @Override
    @Transactional(readOnly = true)
    public Employee findByIdDto(Long employeeId) {
        log.info("Получение сотрудника по идентификатору {}", employeeId);
        return findById(employeeId);
    }

    /**
     * Получение данных сотрудника для аутентификации
     */
    @Override
    public UserDetailsService userDetailsService() {
        log.info("Получение данных сотрудника для аутентификации");
        return this::findByEmail;
    }

    /**
     * Получение сотрудника по email
     */
    @Override
    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        log.info("Получение сотрудника по email {}", email);
        return employeeRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(String.format("Сотрудник с email %s не найден", email)));
    }

    /**
     * Получение сотрудника по id и проверка его наличия в базе данных
     */
    @Override
    @Transactional(readOnly = true)
    public Employee findById(Long employeeId) {
        log.info("Получение сотрудника по идентификатору {}", employeeId);
        return employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Сотрудник с id %s не найден", employeeId)));
    }

    /**
     * Получение всех сотрудников для одного админа
     */
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllByCreatorEmail(String email) {
        log.info("Получение всех сотрудников для одного админа {}", email);
        return employeeRepository.findAllByCreatorEmail(email);
    }
}