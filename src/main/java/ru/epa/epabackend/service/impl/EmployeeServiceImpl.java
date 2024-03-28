package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeRequestDto;
import ru.epa.epabackend.dto.employee.EmployeeShortRequestDto;
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
    public Employee create(EmployeeRequestDto employeeRequestDto, String email) {
        log.info("Создание нового сотрудника {}", employeeRequestDto.getFullName());
        Employee employeeToSave = employeeMapper.mapToEntity(employeeRequestDto);
        employeeToSave.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));
        employeeToSave.setRole(ROLE_USER);
        Employee admin = findByEmail(email);
        employeeToSave.setCreator(admin);
        return employeeRepository.save(employeeToSave);
    }

    /**
     * Саморегистрация администратора
     */
    @Override
    public Employee createSelfRegister(EmployeeShortRequestDto employeeShortRequestDto) {
        log.info("Создание нового сотрудника {}", employeeShortRequestDto.getFullName());
        Employee employeeToSave = employeeMapper.mapToEntity(employeeShortRequestDto);
        employeeToSave.setPassword(passwordEncoder.encode(employeeShortRequestDto.getPassword()));
        employeeToSave.setRole(ROLE_ADMIN);
        return employeeRepository.save(employeeToSave);
    }

    /**
     * Обновление сотрудника
     */
    @Override
    public Employee update(Long employeeId, EmployeeRequestDto employeeRequestDto) {
        log.info("Обновление существующего сотрудника {}", employeeRequestDto.getFullName());
        Employee oldEmployee = findById(employeeId);
        String password = employeeRequestDto.getPassword();

        if (password != null && !password.isBlank()) {
            oldEmployee.setPassword(passwordEncoder.encode(password));
        }

        employeeMapper.updateFields(employeeRequestDto, oldEmployee);
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
     * Получение данных сотрудникаи для аутентификации
     */
    @Override
    public UserDetailsService userDetailsService() {
        return this::findByEmail;
    }

    /**
     * Получение сотрудника по email
     */
    @Override
    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(String.format("Сотрудник с email %s не найден", email)));
    }

    /**
     * Получение сотрудника по id и проверка его наличия в базе данных
     */
    @Override
    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Сотрудник с id %s не найден", employeeId)));
    }

    /**
     * Получение всех сотрудников для одного админа
     */
    @Override
    public List<Employee> findAllByCreatorEmail(String email) {
        return employeeRepository.findAllByCreatorEmail(email);
    }
}