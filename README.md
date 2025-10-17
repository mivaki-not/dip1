[![Java CI with Gradle](https://github.com/mivaki-not/dip1/actions/workflows/gradle.yml/badge.svg)](https://github.com/mivaki-not/dip1/actions/workflows/gradle.yml)

# Курсовой проект по модулю «Автоматизация тестирования» для профессии «Инженер по тестированию»
Курсовой проект — автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API банка.

[**План автоматизации**]()

[**Отчёт по итогам автоматизации**]()

## Начало работы

1. Сделайте fork [проекта]() на GitHub
2. Перейдите в папку (директорию) на вашем компьютере, где будет храниться проект.
3. С помощью терминала, командной строки вашей операционной системы или консоли Git (ПКМ -> GitBash Here) откройте выбранную вами директорию.
4. Склонируйте репозиторий с домашними заданиями с помощью команды git clone в открывшемся терминале или командной строке.
5. Перейдите в директорию склонированного репозитория.

### Prerequisites

Для запуска необходимы следующие инструменты:

* Git
* IntelliJ IDEA
* браузер Google Chrome
* Docker Desktop
* СУБД: Mysql 8 и PostgreSQL 15

### Установка и запуск

1. Запустить Docker Desktop
2. Загрузить контейнеры mysql в терминале IDEA командой

   `docker-compose up`
3. Запуск SUT и тестов с MySQL
   3.1. Запустить .jar-файл с MySQL:

   `java -jar artifacts/aqa-shop.jar -Dspring.datasource.url=jdbc:mysql://localhost:3306/app`

   3.2. Запустить автотесты с MySQL:

   `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app" "-Ddb.user=app" "-Ddb.password=pass"`

4. Запуск SUT и тестов с PostgreSQL
   4.1. Запустить .jar-файл с PostgreSQL:

   `java -jar artifacts/aqa-shop.jar -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app`

   4.2. Запустить автотесты с PostgreSQL:

   `./gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app" "-Ddb.user=app -Ddb.password=pass"`

5. В браузере открыть SUT в окне с адресом:

http://localhost:8080/
6. Остановить SUT командой CTRL + C
7. Остановить контейнеры командой CTRL + C и после удалить контейнеры командой

   `docker-compose down`


8. Посмотреть отчёт о проведённом тестировании:

   `./gradlew allureServe`