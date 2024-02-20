package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.epa.epabackend.until.TaskStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer priority;

    private String name;

    private Integer duration;

    private LocalDate start_date;

    private LocalDate finish_date;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Integer basicPoints;

    private Integer points;
}
