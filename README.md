[![login-page](login-page.png)](http://a0944694.xsph.ru/login)

# Well Done - сервис оценки эффективности сотрудников
[![Java](https://img.shields.io/badge/-Java%2017-F29111?style=for-the-badge&logo=java&logoColor=e38873)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/-Spring%20Boot%203.2-6AAD3D?style=for-the-badge&logo=spring-boot&logoColor=90fd87)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/-Spring%20Security%20(JWT)-6AAD3D?style=for-the-badge&logo=spring-security&logoColor=90fd87)](https://spring.io/projects/spring-security)
[![Postgresql 14](https://img.shields.io/badge/-postgresql%20-31648C?style=for-the-badge&logo=postgresql&logoColor=FFFFFF)](https://www.postgresql.org/)
[![Hibernate](https://img.shields.io/badge/-Hibernate%206.4-B6A975?style=for-the-badge&logo=hibernate&logoColor=717c88)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/-Maven-7D2675?style=for-the-badge&logo=apache&logoColor=e38873)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Portainer](https://img.shields.io/badge/Portainer-13bef9?style=for-the-badge&logo=portainer&logoColor=white)](https://www.portainer.io/)
[![Postman](https://img.shields.io/badge/Postman%2011-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://www.postman.com/)
[![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)](https://editor-next.swagger.io/)
[![Watchtower](https://img.shields.io/badge/Watchtower-406170?style=for-the-badge&logo=watchtower&logoColor=white)](https://containrrr.dev/watchtower/)
[![CI/CD](https://img.shields.io/badge/CI/CD-118249?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/features/actions)
[![Liquibase](https://img.shields.io/badge/Liquibase-2a62ff?style=for-the-badge&logo=liquibase&logoColor=white)](https://www.liquibase.com/)
[![Linux](https://img.shields.io/badge/Linux%20Ubuntu%2022.04-373637?style=for-the-badge&logo=linux&logoColor=white)](https://www.linux.org/)
[![JUnit](https://img.shields.io/badge/JUnit%205-6CA315?style=for-the-badge&logo=JUnit&logoColor=white)](https://junit.org/junit5/docs/current/user-guide/)
[![Mockito](https://img.shields.io/badge/-mockito%205.7-6CA315?style=for-the-badge&logo=mockito&logoColor=90fd87)](https://site.mockito.org/)
[![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)](https://git-scm.com/)
[![MapStruct](https://img.shields.io/badge/MapStruct-d23120?style=for-the-badge&logo=&logoColor=white)](https://mapstruct.org/)
[![LogBook](https://img.shields.io/badge/Logbook-ff6900?style=for-the-badge&logo=logbook&logoColor=white)](https://github.com/zalando/logbook)

## Описание
На платформе руководитель распределяет задачи среди сотрудников и проводит анкетирование.
Из выполненных задач и оценок коллег складывается эффективность сотрудника. Согласно им руководитель принимает решения
относительно сотрудника - выдать бо'льшую премию, повысить в должности или наоборот.
Баллы за задачи нужны для оценки качеств сотрудника вне команды по итогам месяца, а оценки коллег позволяют определить,
насколько сотрудник является командным игроком и совпадает ли мнение руководителя с оценкой коллектива.

## Архитектура
Приложение запущено в виде 4 контейнеров:
- epa-backend - бекенд-приложение
- epa-frontend - фронтенд-приложение
- epa-db - база данных для хранения данных руководителей и их сотрудников
- watchtower - автоматическое обновление контейнеров при появлении новых docker-образов

## Функциональность
В приложении есть есть открытые эндпоинты для саморегистрации руководителей.
Закрытые эндпоинты различаются для руководителей и сотрудников.
Подробное описание эндпоинтов, классов и требований к ним приведены в спецификации
[Swagger](http://45.80.69.141:60606/swagger-ui/index.html#/)

## Диаграмма базы данных
![схема БД](epa_schema_DB.png)

## Как запустить и использовать
Приложение развёрнуто на хостинге по ссылке http://a0944694.xsph.ru/login

Для локального запуска бекенд-приложения с БД установите и откройте программу
[Docker Desktop](https://www.docker.com/products/docker-desktop/).
<br>Затем в командной строке cmd выполните следующие команды

   ```
git clone git@github.com:employee-performance-assessment/epa_backend.git
cd ~/epa-backend   
mvn clean package
git checkout dev
docker-compose up
   ```
Приложение готово к использованию! Сервис доступен по андресу [http://localhost:60606](http://localhost:60606).
<br>Swagger-документация при локальном запуске [http://localhost:60606/swagger-ui/index.html#/](http://localhost:60606/swagger-ui/index.html#/)
<br>Со сценариями работы приложения можно ознакомся, запустив коллекции
[Postman-тестов](postman/epa.postman_collection.json), указав в разделе Variables переменную baseUrl http://localhost:60606