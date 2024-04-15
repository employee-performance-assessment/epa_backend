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
import ru.epa.epabackend.exception.exceptions.ConflictException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.TaskRepository;
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
    private final EmployeeEvaluationRepository employeeEvaluationRepository;
    private final TaskRepository taskRepository;

    /**
     * Создание нового сотрудника
     */
    @Override
    public Employee create(RequestEmployeeDto requestEmployeeDto, String email) {
        requestEmployeeDto.setEmail(requestEmployeeDto.getEmail().toLowerCase());
        String employeeEmail = requestEmployeeDto.getEmail();
        log.info("Создание нового сотрудника {} с email {}", requestEmployeeDto.getFullName(), employeeEmail);
        if (employeeRepository.existsByEmail(employeeEmail)) {
            throw new ConflictException("Пользователь с таким email уже существует.");
        }
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
        requestEmployeeShortDto.setEmail(requestEmployeeShortDto.getEmail().toLowerCase());
        String employeeEmail = requestEmployeeShortDto.getEmail();
        log.info("Создание нового сотрудника {}", requestEmployeeShortDto.getFullName());
        if (employeeRepository.existsByEmail(employeeEmail)) {
            throw new ConflictException("Пользователь с таким email уже существует.");
        }
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
        if(requestEmployeeDto.getEmail()!=null) {
            requestEmployeeDto.setEmail(requestEmployeeDto.getEmail().toLowerCase());
        }
        Employee oldEmployee = findById(employeeId);
        Employee admin = findByEmail(adminEmail);

        checkAdminForEmployee(admin, oldEmployee);

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
    public void delete(Long employeeId, String email) {
        log.info("Удаление сотрудника по идентификатору {}", employeeId);
        Employee admin = findByEmail(email);
        Employee employee = findById(employeeId);
        checkAdminForEmployee(admin, employee);
        if (employeeEvaluationRepository.existsByEvaluatedIdOrEvaluatorId(employeeId, employeeId)) {
            throw new ConflictException("Нельзя удалить сотрудника, с которым связаны оценки");
        }
        if (taskRepository.existsByExecutorId(employeeId)) {
            throw new ConflictException("Нельзя удалить сотрудника, у которого были задачи");
        }
        employeeRepository.deleteById(employeeId);
    }

    /**
     * Получение всех сотрудников
     */
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll(String email) {
        Employee employee = findByEmail(email);
        String adminEmail = employee.getCreator() != null ? employee.getCreator().getEmail() : employee.getEmail();
        log.info("Получение всех сотрудников");
        return employeeRepository.findAllByCreatorEmail(adminEmail, Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Получение сотрудника по id. Можно запрашивать сотрудника того же админа
     */
    @Override
    @Transactional(readOnly = true)
    public Employee findByIdDto(Long employeeId, String email) {
        log.info("Получение сотрудника по идентификатору {}", employeeId);
        Employee user = findByEmail(email);
        Employee admin = user.getCreator() == null ? user : user.getCreator();
        Employee employee = findById(employeeId);
        checkAdminForEmployee(admin, employee);
        return employee;
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
                new EntityNotFoundException("Сотрудник не найден"));
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
            if (!Objects.equals(admin.getId(), employee.getId())) {
                throw new BadRequestException(String.format("Пользователь %s не является сотрудником",
                        employee.getFullName()));
            }
        } else if (!Objects.equals(employee.getCreator().getId(), admin.getId())) {
            throw new BadRequestException(String.format("%s не является сотрудником руководителя %s",
                    employee.getFullName(), admin.getFullName()));
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
    public void checkEvaluatorForEmployee(Employee evaluator, Employee evaluated) {
        Employee evaluatorCreator = evaluator.getCreator();
        Employee evaluatedCreator = evaluated.getCreator();
        if (evaluatedCreator == null) {
            throw new BadRequestException(String.format("Оцениваемый пользователь %s является руководителем",
                    evaluated.getFullName()));
        }
        if (evaluatorCreator != null && !Objects.equals(evaluatorCreator.getId(), evaluatedCreator.getId())) {
            throw new BadRequestException(String.format("Сотрудник %s не ваш коллега", evaluated.getFullName()));
        }
        if (evaluatorCreator == null && !Objects.equals(evaluator.getId(), evaluatedCreator.getId())) {
            throw new BadRequestException(String.format("Оцениваемый пользователь %s не ваш сотрудник",
                    evaluated.getFullName()));
        }
    }
}