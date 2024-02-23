package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

/**
 * Класс Сотрудник содержит информацию о логине и пароле (для логина используется email),
 * дате рождения, должности/грейду и стеке технологий сотрудника.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {
    /**
     * Идентификатор сотрудника.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Имя.
     */
    private String fistName;
    /**
     * Фамилия.
     */
    private String LastName;
    /**
     * Отчество.
     */
    private String patronymic;

    /**
     * Ник в корпоративном мессенджере.
     */
    private String nik;
    /**
     * Город проживания.
     */
    private String city;
    /**
     * Часовой пояс сотрудника.
     */
    private TimeZone timeZone;

    /**
     * Логин сотрудника - email.
     */
    private String login;

    /**
     * Пароль.
     */
    private String password;

    /**
     * День рождения.
     */
    private LocalDate birthday;

    /**
     * Роль/грейд
     * Возможные роли: ADMIN, SENIOR, MIDDLE, JUNIOR.
     */
    private Role role;

    /**
     * Стек технологий, которыми владеет сотрудник.
     */
    @ManyToMany
    @JoinTable(
            name = "employees_technologies",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "technologies_id"))
    private Set<Technology> technologies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id) && login.equals(employee.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fistName='" + fistName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", nik='" + nik + '\'' +
                ", city='" + city + '\'' +
                ", timeZone=" + timeZone +
                ", login='" + login + '\'' +
                ", password={masked}" +
                ", birthday=" + birthday +
                ", role=" + role +
                ", technologies=" + technologies +
                '}';
    }
}
