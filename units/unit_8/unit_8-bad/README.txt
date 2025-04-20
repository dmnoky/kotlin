UNIT_8 - не очень рабочая версия

Задание 1. Общение с БД
Написать приложение, которое будет общаться с помощью ЈРА и Hibernate с базой данных, разработанной в задаче "Задание 1. Создание таблиц в базе данных" в Unit 7. Базы данных. JDBC:
• с помощью Liquibase/Flyway (в зависимости от того, что использует Ваша команда) создать заново таблицы в базе данных
• реализовать для каждой сущности CRUD методы (создание, удаление, изменение и получение данных)
• в качестве сборщика проекта использовать Maven либо Gradle
------------------------------------------------------------------------------------------------------------------
Liquibase в src/main/resources/db/...
2ое задание в src/test/kotlin/
    ClientServiceTest:
        • getFIOByTransportBrand    --получение ФИО всех клиентов, у которых есть автомобиль марки ВМW
        • clearTransportByClientId  --удаление всех записей об автомобилях у определенного клиента
        • getCountTransportAndClientInfo_orderByFIOAsc      --получение количества автомобилей по каждому клиенту
        • getCountTransportAndClientInfo_whereCountTransportGreaterThan2_orderByFIOAsc
    TransportServiceTest
        • findByClientId    --получение автомобилей по данным клиента
------------------------------------------------------------------------------------------------------------------
P.S. Тесты не идеальные (где то просто чтоб данных накидать в таблицу) и не всё ими покрыто.
    Ентити менеджер сделал закрывающимся на уровне сервисов. А сами сервисы отдают не сущность, а дто (чтоб не ловить лезиИнитЭксепт).
    Как будто так надежнее, чем везде открытая сессия.
P.P.S. Из-за подохода с контролем транзакций и закрытия сессии возникли проблемы с entityManager.remove(entity):
    объекта нет в мапе (детач) и ни файнд, ни мердж не добавляют его в мапу (ентити всё так же детач)

    override fun delete(entityManager: EntityManager, model: TransportModel): Boolean {
        println(model)
        println(entityManager.contains(model)) //false
        var entity = findById(entityManager, model.id) //entityManager.find(model::class.java, model.id)
        entity = entityManager.createQuery("FROM TransportModel WHERE id=:id", TransportModel::class.java)
            .setParameter("id", model.id).singleResult
        entityManager.merge(entity) //persist тоже не добавляет
        println(entity)
        println(entityManager.contains(model)) //false, но по логике, find или merge должны были закинуть его в мапу и contains должен быть тру
        entityManager.remove(model) //java.lang.IllegalArgumentException: Removing a detached instance ru.tbank.model.TransportModel#525d369f-e9a2-4cbd-94fb-8fec66ae8788
        return true
    }

