INSERT INTO CRITERIA (NAME, IS_DEFAULT)
VALUES (' Погружается в проект', true),
       (' Выполняет задачи', true),
       (' Работает в команде', true),
       (' Соблюдает дедлайны', true),
       (' Расставляет приоритеты', true),
       (' Умеет решать сложные задачи', true),
       (' Ясно объясняет свои идеи команде', true),
       (' Эффективно работает над несколькими задачами одновременно', true),
       (' Воспринимает конструктивную критику', true),
       (' Помогает коллегам с их задачами', true),
       (' Запрашивает необходимую информацию и мнение коллег для решения совместных задач', true);
INSERT INTO criteria (name) VALUES
                                ('активный в чатах'),
                                ('помогает при обращении к нему с просьбой'),
                                ('компетентен в своей области знаний'),
                                ('выходит на связь в течение дня'),
                                ('Танцы'),
                                ('Пенее'),
                                ('Пустая болтовня');


INSERT INTO employees (full_name,nick_name,email,"password","role","position",creator_id) VALUES
                                                                                              ('Петр Петров',NULL,'admin@yandex.ru','$2a$10$MAMgOTOsugsjgWt4QEKct.7Fd3Qp7CVgujeDoKhGznYcCr/TxdoOW','ROLE_ADMIN','техлид',NULL),
                                                                                              ('Георг',NULL,'igor@yandex.ru','$2a$10$jsw7LihcMcaGye/8iYn7Zu2qBHqhswT/zeNl8tJVMccg5gXYqxJfi','ROLE_USER','pm',1),
                                                                                              ('Иванов Тест Иванович',NULL,'mail2@mail.ru','$2a$10$oeikmoI/Wf4jwo6OwDrIxeVK3YqyQiPcuNLh7Wwhv8UjUKX7qmsKW','ROLE_USER','disign',1),
                                                                                              ('Оклахом Борис Джовански','@appalachi','obj@yahoo.com','$2a$10$RlOcrNxozsvNSIbfQSQIM.ArQvwtzp183dQ6stnrxcZ5RrCsHjrYq','ROLE_USER','front dev',1),
                                                                                              ('Богдан Хмельницкий','@kazak','hmel@ukr.net','$2a$10$wZZhRI0ySqq5BSGowbdkBe82zqzCfarmn1RdFIk5tSrCxJPFY7b1m','ROLE_USER','back dev',1),
                                                                                              ('Рексар','@klyk','ork@orda.net','$2a$10$SfQ582Yt.L3riXzIYWpZf.ZBYkLSU5IEXldw9FE.rpg4ScFG21pYq','ROLE_USER','mobile dev',1),
                                                                                              ('Гарри Поттер',NULL,'grand-magician@muggle.net','$2a$10$baPrJfLnI81f742.NrdY0eczliFxXjkFtQYj64yDcgH3SYZEsCDPa','ROLE_USER','qa',1);


INSERT INTO projects (name,status,created) VALUES
                                               ('Developer sheeps','COMPLETED','2024-03-01'),
                                               ('Googlez','TODO','2024-03-16');


INSERT INTO tasks (name,description,project_id,executor_id,start_date,dead_line,finish_date,status,basic_points,points,penalty_points,owner_id) VALUES
                                                                                                                                                    ('task1','u task1 description',1,2,'2024-03-01','2024-03-15','2024-03-14','DONE',100,105,5,1),
                                                                                                                                                    ('task2','u task1 description',1,2,'2024-03-01','2024-03-15','2024-03-13','DONE',100,110,5,1),
                                                                                                                                                    ('task3','u task1 description',1,3,'2024-03-01','2024-03-15','2024-03-12','DONE',100,115,5,1),
                                                                                                                                                    ('task4','u task1 description',1,3,'2024-03-01','2024-03-15','2024-03-18','DONE',100,85,5,1),
                                                                                                                                                    ('task5','u task1 description',1,4,'2024-03-01','2024-03-15','2024-03-20','DONE',100,75,5,1),


                                                                                                                                                    ('task6','u task1 description',2,5,'2024-03-16','2024-04-15',NULL,'NEW',100,0,5,1),
                                                                                                                                                    ('task7','u task1 description',2,5,'2024-03-16','2024-04-15',NULL,'IN_PROGRESS',100,0,5,1),
                                                                                                                                                    ('task8','u task1 description',2,6,'2024-03-16','2024-04-15',NULL,'IN_PROGRESS',100,0,5,1),
                                                                                                                                                    ('task9','u task1 description',2,6,'2024-03-16','2024-04-15',NULL,'REVIEW',100,0,5,1);


INSERT INTO projects_employees (project_id,employee_id) VALUES
                                                            (1,1),
                                                            (1,2),
                                                            (1,3),
                                                            (1,4),
                                                            (2,5),
                                                            (2,6),
                                                            (2,7);


INSERT INTO employees_tasks (employee_id, task_id) VALUES
                                                       (2,1),
                                                       (2,2),
                                                       (3,3),
                                                       (3,4),
                                                       (4,5),
                                                       (5,6),
                                                       (5,7),
                                                       (6,8),
                                                       (7,9);


INSERT INTO questionnaires (author_id,created,status) VALUES
                                                          (1,'2024-04-01','SHARED'),
                                                          (1,'2024-04-01','SHARED'),
                                                          (1,'2024-04-01','SHARED'),
                                                          (1,'2024-04-01','SHARED'),
                                                          (1,NULL,'CREATED');


INSERT INTO employee_evaluation (evaluated_id,evaluator_id,create_day,criteria_id,score) VALUES
                                                                                             (3,4,'2024-03-31',1,5),
                                                                                             (3,4,'2024-03-31',2,5),
                                                                                             (3,4,'2024-03-31',3,5),
                                                                                             (3,2,'2024-03-31',2,4),
                                                                                             (3,2,'2024-03-31',3,2),
                                                                                             (3,2,'2024-03-31',1,1),
                                                                                             (3,1,'2024-03-31',1,4),
                                                                                             (3,1,'2024-03-31',2,4),
                                                                                             (3,1,'2024-03-31',3,5);
INSERT INTO employee_evaluation (evaluated_id,evaluator_id,create_day,criteria_id,score) VALUES
                                                                                             (2,3,'2024-04-01',2,5),
                                                                                             (2,3,'2024-04-01',3,5),
                                                                                             (2,3,'2024-04-01',1,4),
                                                                                             (2,4,'2024-04-01',3,2),
                                                                                             (2,4,'2024-04-01',2,1),
                                                                                             (2,4,'2024-04-01',1,4);


INSERT INTO recommendations (recipient_id,sender_id,create_day,recommendation) VALUES
                                                                                   (1,2,'2024-04-01','Для успешного выполнения задач важно правильно организовать рабочее пространство и время. Попробуйте метод помидора - ставьте таймер на 25 минут, в течение которых работайте интенсивно, полностью погружаясь в задачу, а затем делайте 5-минутный перерыв. Такой подход поможет вам оставаться продуктивным, не переутомляясь.'),
                                                                                   (1,3,'2024-04-01','Твоя работа действительно впечатляет. Ты очень ответственный и талантливый сотрудник.');


INSERT INTO questionnaires_criterias (questionnaire_id,criteria_id) VALUES
                                                                        (1,1),
                                                                        (1,2),
                                                                        (1,3),
                                                                        (1,4),
                                                                        (1,5),
                                                                        (1,6),
                                                                        (1,7),
                                                                        (1,8),
                                                                        (1,9),
                                                                        (1,10);
INSERT INTO questionnaires_criterias (questionnaire_id,criteria_id) VALUES
                                                                        (1,11),
                                                                        (2,14),
                                                                        (2,15),
                                                                        (3,14),
                                                                        (3,15),
                                                                        (4,14),
                                                                        (4,15),
                                                                        (5,12),
                                                                        (5,13);
