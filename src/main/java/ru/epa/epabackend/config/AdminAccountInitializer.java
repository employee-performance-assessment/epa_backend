package ru.epa.epabackend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.util.Role;

/**
 * Первичная инициализация аккаунта администратора. Логин и пароль задаются в файле настроек. Если настройки не заданы
 * используется значение по умолчанию: логин 'admin' пароль 'admin'.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminAccountInitializer implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.login:admin}")
    private String login;

    @Value("${admin.password:admin}")
    private String password;

    @Override
    public void run(String... args) {
        Employee admin = Employee.builder()
                .lastName("last_name")
                .firstName("first_name")
                .patronymic("patronymic")
                .email(login)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_ADMIN)
                .build();

        try {
            //ERROR выбран намеренно для лучшей читаемости
            employeeRepository.save(admin);
            log.error("--------------------------------------");
            log.error("Initial admin login '{}'.", login);
            log.error("Initial admin password '{}'.", password);
            log.error("--------------------------------------");
        } catch (DataIntegrityViolationException e) {
            log.error("Admin account already registered");
        }
    }
}
