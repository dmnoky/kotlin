UNIT_7

Задание 1. Создание таблиц в базе данных
Задачи
Создать базу данных, в которую включить набор связанных сущностей, предусмотрев связи 1:М и М:N и индексы (в том числе уникальные и составные). Заполнить таблицы данными
Предлагаемые сущности: сведения по клиентам, их автомобилям и маркам автомобилей. Обязательные данные по клиенту: уникальный id, ФИО, город проживания. Клиент может быть владельцем нескольких автомобилей, а один автомобиль может принадлежать нескольким клиентам. Сущность автомобиля может содержать такие данные, как id записи, гос. номер, марка и модель.

Задание 2. JDBC
Для таблиц, созданных в предыдущей задаче, написать обычные crud операции (предусмотреть soft delete и optimistic lock для некоторых сущностей) и несколько запросов на получение/удаление/изменение данных в таблицах с использованием JDBC.
Примеры:
• получение ФИО всех клиентов, у которых есть автомобиль марки ВМW
• получение автомобилей по данным клиента
• удаление всех записей об автомобилях у определенного клиента
• получение количества автомобилей по каждому клиенту (на экран вывести id клиента, ФИО и количество автомобилей; отсортировать результат по ФИО в алфавитном порядке)
• переделать предыдущий запрос таким образом, чтобы на экран вывелись клиенты, у которых больше 2 машин
------------------------------------------------------------------------------------------------------------------
1ое задание юнита в src/test/resources/V1__ddl_unit7.sql
2ое задание в src/test/kotlin/
    ClientServiceTest:
        • getFIOByTransportBrand    --получение ФИО всех клиентов, у которых есть автомобиль марки ВМW
        • clearTransportByClientId  --удаление всех записей об автомобилях у определенного клиента
        • getCountTransportAndClientInfo_orderByFIOAsc      --получение количества автомобилей по каждому клиенту
        • getCountTransportAndClientInfo_whereCountTransportGreaterThan2_orderByFIOAsc
    TransportServiceTest
        • findByClientId    --получение автомобилей по данным клиента

P.S. Тесты не идеальные (где то просто чтоб данных накидать в таблицу) и не всё ими покрыто.