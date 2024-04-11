package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.RequestEmployeeDto;
import ru.epa.epabackend.dto.employee.RequestEmployeeShortDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.service.EmployeeService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        employeeToSave.setCreated(LocalDate.now());
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
        employeeToSave.setCreated(LocalDate.now());
        return employeeRepository.save(employeeToSave);
    }

    /**
     * Обновление сотрудника
     */
    @Override
    public Employee update(Long employeeId, RequestEmployeeDto requestEmployeeDto, String adminEmail) {
        log.info("Обновление существующего сотрудника {}", requestEmployeeDto.getFullName());
        Employee oldEmployee = findById(employeeId);
        Employee admin = findByEmail(adminEmail);

        checkEvaluatorOrAdminForEmployee(admin, oldEmployee);

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
        return employeeRepository.findAllByCreatorEmail(email, Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Проверка, что сотрудник относится к руководителю
     */
    @Override
    @Transactional(readOnly = true)
    public void checkAdminForEmployee(Employee admin, Employee employee) {
        if (employee.getCreator() == null) {
            throw new BadRequestException(String.format("Пользователь с id %d не является сотрудником", employee.getId()));
        } else if (employee.getCreator().getId() != admin.getId()) {
            throw new BadRequestException(String.format("Сотрудник с id %d не относится к администратору с id %d",
                    employee.getId(), admin.getId()));
        }
    }

    @Override
    public List<Integer> findAllYearsFromAdminCreation(String email) {
        log.info("Получение списка годов с начала регистрации администратора до текущего года.");
        Employee employee = findByEmail(email);
        Employee admin = employee.getCreator();
        int adminCreationYear = admin == null
                ? employee.getCreated().getYear()
                : admin.getCreated().getYear();
        int currentYear = LocalDate.now().getYear();
        List<Integer> years = new ArrayList<>();
        for (int i = adminCreationYear; i <= currentYear; i++) {
            years.add(i);
        }
        return years;
    }

    /**
     * Проверка, что сотрудник оценивает своего коллегу или является его руководителем
     */
    @Override
    @Transactional(readOnly = true)
    public void checkEvaluatorOrAdminForEmployee(Employee evaluator, Employee evaluated) {
        if (evaluator.getCreator() != null
                && evaluated.getCreator() != null
                && !Objects.equals(evaluator.getCreator().getId(), evaluated.getCreator().getId())) {
            throw new BadRequestException(String.format("Пользователь с id %d не ваш коллега", evaluated.getId()));
        } else if (evaluator.getCreator() == null
                && evaluated.getCreator() != null
                && !Objects.equals(evaluator.getId(), evaluated.getCreator().getId())) {
            throw new BadRequestException(String.format("Пользователь с id %d не ваш сотрудник", evaluated.getId()));
        } else if (evaluated.getCreator() == null) {
            throw new BadRequestException(String.format("Пользователь с id %d является руководителем",
                    evaluated.getId()));
        }
    }
}