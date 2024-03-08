drop TABLE IF EXISTS projects, employees, tasks, technologies, tasks_technologies, employees_technologies, subtasks,
    employees_tasks, courses, courses_technologies, employees_courses, projects_tasks, projects_employees CASCADE;

CREATE TABLE IF NOT EXISTS projects
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name   VARCHAR(255) NOT NULL,
    status VARCHAR(100) NOT NULL,
    created DATE,
    CONSTRAINT pk_project PRIMARY KEY (id),
    CONSTRAINT uq_projects_for_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS employees (
  id         BIGINT GENERATED BY DEFAULT AS IDENTITY,
  full_name VARCHAR(255) NOT NULL,
  nick_name  VARCHAR(255),
  city       VARCHAR(255),
  email      VARCHAR(255) NOT NULL,
  password   VARCHAR(255) NOT NULL,
  birthday   DATE,
  role       VARCHAR(100),
  position   VARCHAR(100),
  department VARCHAR(100),
  CONSTRAINT pk_useremployee PRIMARY KEY (id),
  CONSTRAINT uq_employees_name_email UNIQUE (email)
);

create table if not exists tasks
(
    id             bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name           VARCHAR(255)                            NOT NULL,
    description    VARCHAR(255)                            NOT NULL,
    project_id     BIGINT                                  NOT NULL,
    executor_id    BIGINT,
    start_date     DATE,
    dead_line      DATE,
    finish_date    DATE,
    status         VARCHAR(255),
    basic_points   INTEGER                                 NOT NULL,
    points         INTEGER,
    penalty_points INTEGER                                 NOT NULL,
    CONSTRAINT pk_task PRIMARY KEY (id),
    CONSTRAINT fk_comeve_on_project foreign key (project_id) references projects (id),
    CONSTRAINT fk_comeve_on_executor foreign key (executor_id) references employees (id),
    CONSTRAINT uq_tasks_for_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS technologies (
  id   BIGINT GENERATED BY DEFAULT AS IDENTITY,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT pk_technology PRIMARY KEY (id),
  CONSTRAINT uq_technologies_for_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS subtasks
(
    task_id    BIGINT NOT NULL,
    subtask_id BIGINT NOT NULL,
    CONSTRAINT pk_stak_subtasks PRIMARY KEY (task_id, subtask_id),
    CONSTRAINT fk_comeve_on_task FOREIGN KEY (task_id) REFERENCES tasks (id),
    CONSTRAINT fk_comeve_on_subtask FOREIGN KEY (subtask_id) REFERENCES tasks (id)
);

CREATE TABLE IF NOT EXISTS courses
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name        VARCHAR(255) NOT NULL,
    link        VARCHAR(255),
    start_date  DATE,
    finish_date DATE,
    CONSTRAINT pk_course PRIMARY KEY (id),
    CONSTRAINT uq_courses_for_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS courses_technologies
(
    course_id     BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    CONSTRAINT pk_cources_technologies PRIMARY KEY (course_id, technology_id),
    CONSTRAINT fk_comeve_on_course FOREIGN KEY (course_id) REFERENCES courses (id),
    CONSTRAINT fk_comeve_on_technology FOREIGN KEY (technology_id) REFERENCES technologies (id)
);

CREATE TABLE IF NOT EXISTS employees_technologies
(
    employee_id   BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    CONSTRAINT pk_employees_technologies PRIMARY KEY (employee_id, technology_id),
    CONSTRAINT fk_comeve_on_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_comeve_on_technology FOREIGN KEY (technology_id) REFERENCES technologies (id)
);

CREATE TABLE IF NOT EXISTS employees_courses
(
    employee_id BIGINT NOT NULL,
    course_id   BIGINT NOT NULL,
    CONSTRAINT pk_employees_courses PRIMARY KEY (employee_id, course_id),
    CONSTRAINT fk_comeve_on_course FOREIGN KEY (course_id) REFERENCES courses (id),
    CONSTRAINT fk_comeve_on_employee FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE IF NOT EXISTS employees_tasks
(
    employee_id BIGINT NOT NULL,
    task_id     BIGINT NOT NULL,
    CONSTRAINT pk_employees_tasks PRIMARY KEY (employee_id, task_id),
    CONSTRAINT fk_comeve_on_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_comeve_on_task FOREIGN KEY (task_id) REFERENCES tasks (id)
);

CREATE TABLE IF NOT EXISTS projects_employees (
    project_id BIGINT NOT NULL REFERENCES projects(id),
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    PRIMARY KEY (project_id, employee_id)
);
