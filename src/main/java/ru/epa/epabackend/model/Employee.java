package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.epa.epabackend.util.Role;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс Сотрудник содержит информацию о логине и пароле (для логина используется email),
 * дате рождения, должности/грейду и стеке технологий сотрудника.
 *
 * @author Михаил Безуглов и Валентина Вахламова
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee implements UserDetails {

    /**
     * Идентификатор сотрудника
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Полное имя ФИО
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * Логин сотрудника - email
     */
    private String email;

    /**
     * Пароль
     */
    private String password;

    /**
     * Роль/грейд
     * Возможные роли: ADMIN, USER
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Должность
     */
    private String position;

    @OneToOne
    @JoinColumn(name = "creator_id")
    @Nullable
    private Employee creator;

    /**
     * Дата регистрации
     */
    private LocalDate created;

    /**
     * Список задач сотрудника
     */
    @OneToMany
    @JoinColumn(name = "executor_id")
    @Builder.Default
    private Set<Task> tasks = new HashSet<>();

    /**
     * Стек технологий, которыми владеет сотрудник
     */
    @ManyToMany
    @JoinTable(
            name = "employees_technologies",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id"))
    @Builder.Default
    private Set<Technology> technologies = new HashSet<>();

    /**
     * Список проектов сотрудника
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "projects_employees",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")})
    private List<Project> projects;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id) && email.equals(employee.email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", full name='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password={masked}" +
                ", role=" + role +
                ", position=" + position +
                ", tasks=" + tasks +
                ", technologies=" + technologies +
                ", created=" + created +
                '}';
    }
}
