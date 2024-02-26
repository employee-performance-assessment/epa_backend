package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
     * Фамилия.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Имя.
     */
    @Column(name = "first_name")
    private String firstName;

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
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Должность.
     */
    private String position;

    /**
     * Отдел/подразделение.
     */
    private String department;

    /**
     * Список задач сотрудника.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "executor_id")
    private Set<Task> tasks = new HashSet<>();

    /**
     * Стек технологий, которыми владеет сотрудник.
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employees_technologies",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private Set<Technology> technologies = new HashSet<>();

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
                ", last name='" + lastName + '\'' +
                ", first name='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", nik='" + nik + '\'' +
                ", city='" + city + '\'' +
                ", login='" + login + '\'' +
                ", password={masked}" +
                ", birthday=" + birthday +
                ", role=" + role +
                ", position=" + position +
                ", tasks=" + tasks +
                ", technologies=" + technologies +
                '}';
    }
}
