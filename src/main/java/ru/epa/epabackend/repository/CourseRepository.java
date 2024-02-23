package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
