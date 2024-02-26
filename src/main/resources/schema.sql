DROP TABLE IF EXISTS public.projects, public.employees, public.tasks, public.projects_tasks, public.technologies,
    public.subtasks, public.courses, public.courses_technologies, public.tasks_technologies,
    public.employees_technologies, public.employees_courses, public.employees_tasks;

CREATE TABLE IF NOT EXISTS projects (
  id     BIGINT GENERATED BY DEFAULT AS IDENTITY,
  name   VARCHAR(255) NOT NULL,
  status VARCHAR(100) NOT NULL,
  CONSTRAINT pk_project PRIMARY KEY (id),
  CONSTRAINT uq_projects_for_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS employees (
  id         BIGINT GENERATED BY DEFAULT AS IDENTITY,
  last_name  VARCHAR(50) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  patronymic VARCHAR(50) NOT NULL,
  nik        VARCHAR(255),
  city       VARCHAR(255),
  login      VARCHAR(255) NOT NULL,
  password   VARCHAR(255) NOT NULL,
  birthday   DATE,
  role       VARCHAR(100),
  position   VARCHAR(100),
  department VARCHAR(100),
  CONSTRAINT pk_useremployee PRIMARY KEY (id),
  CONSTRAINT uq_employees_name_login UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS tasks (
  id           BIGINT GENERATED BY DEFAULT AS IDENTITY,
  priority     INTEGER NOT NULL,
  name         VARCHAR(255) NOT NULL,
  description  VARCHAR(255) NOT NULL,
  creator_id   BIGINT NOT NULL,
  executor_id  BIGINT,
  duration     INTEGER NOT NULL,
  start_date   TIMESTAMP WITHOUT TIME ZONE,
  finish_date  TIMESTAMP WITHOUT TIME ZONE,
  status       VARCHAR(255),
  basic_points INTEGER NOT NULL,
  points       INTEGER,
  CONSTRAINT pk_task PRIMARY KEY (id),
  CONSTRAINT fk_comeve_on_creator FOREIGN KEY (creator_id) REFERENCES employees (id),
  CONSTRAINT fk_comeve_on_executor FOREIGN KEY (executor_id) REFERENCES employees (id),
  CONSTRAINT uq_tasks_for_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS projects_tasks (
  project_id BIGINT NOT NULL,
  task_id    BIGINT NOT NULL,
  CONSTRAINT pk_projects_events PRIMARY KEY (project_id, task_id),
  CONSTRAINT fk_comeve_on_project FOREIGN KEY (project_id) REFERENCES projects (id),
  CONSTRAINT fk_comeve_on_task FOREIGN KEY (task_id) REFERENCES tasks (id)
);

CREATE TABLE IF NOT EXISTS technologies (
  id   BIGINT GENERATED BY DEFAULT AS IDENTITY,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT pk_technology PRIMARY KEY (id),
  CONSTRAINT uq_technologies_for_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS subtasks (
  task_id    BIGINT NOT NULL,
  subtask_id BIGINT NOT NULL,
  CONSTRAINT pk_stak_subtasks PRIMARY KEY (task_id, subtask_id),
  CONSTRAINT fk_comeve_on_task FOREIGN KEY (task_id) REFERENCES tasks (id),
  CONSTRAINT fk_comeve_on_subtask FOREIGN KEY (subtask_id) REFERENCES tasks (id)
);

CREATE TABLE IF NOT EXISTS courses (
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
  name        VARCHAR(255) NOT NULL,
  link        VARCHAR(255),
  start_date  TIMESTAMP WITHOUT TIME ZONE,
  finish_date TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_course PRIMARY KEY (id),
  CONSTRAINT uq_courses_for_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS courses_technologies (
  course_id     BIGINT NOT NULL,
  technology_id BIGINT NOT NULL,
  CONSTRAINT pk_cources_technologies PRIMARY KEY (course_id, technology_id),
  CONSTRAINT fk_comeve_on_course FOREIGN KEY (course_id) REFERENCES courses (id),
  CONSTRAINT fk_comeve_on_technology FOREIGN KEY (technology_id) REFERENCES technologies (id)
);

CREATE TABLE IF NOT EXISTS tasks_technologies (
  task_id       BIGINT NOT NULL,
  technology_id BIGINT NOT NULL,
  CONSTRAINT pk_tasks_technologies PRIMARY KEY (task_id, technology_id),
  CONSTRAINT fk_comeve_on_task FOREIGN KEY (task_id) REFERENCES tasks (id),
  CONSTRAINT fk_comeve_on_technology FOREIGN KEY (technology_id) REFERENCES technologies (id)
);

CREATE TABLE IF NOT EXISTS employees_technologies (
  employee_id   BIGINT NOT NULL,
  technology_id BIGINT NOT NULL,
  CONSTRAINT pk_employees_technologies PRIMARY KEY (employee_id, technology_id),
  CONSTRAINT fk_comeve_on_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
  CONSTRAINT fk_comeve_on_technology FOREIGN KEY (technology_id) REFERENCES technologies (id)
);

CREATE TABLE IF NOT EXISTS employees_courses (
  employee_id BIGINT NOT NULL,
  course_id   BIGINT NOT NULL,
  CONSTRAINT pk_employees_courses PRIMARY KEY (employee_id, course_id),
  CONSTRAINT fk_comeve_on_course FOREIGN KEY (course_id) REFERENCES courses (id),
  CONSTRAINT fk_comeve_on_employee FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE IF NOT EXISTS employees_tasks (
  employee_id BIGINT NOT NULL,
  task_id     BIGINT NOT NULL,
  CONSTRAINT pk_employees_tasks PRIMARY KEY (employee_id, task_id),
  CONSTRAINT fk_comeve_on_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
  CONSTRAINT fk_comeve_on_task FOREIGN KEY (task_id) REFERENCES tasks (id)
);
