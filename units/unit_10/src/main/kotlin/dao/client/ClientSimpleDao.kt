package ru.tbank.dao.client

import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.dto.CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_GREATER_THAN_X_ORDER_BY_FIO
import ru.tbank.dto.CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_ORDER_BY_FIO
import ru.tbank.dto.ClientWithTransportCountDto
import ru.tbank.model.Client
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.TypedQuery

class ClientSimpleDao private constructor() : ClientDao {
    companion object {
        val singleton: ClientSimpleDao = lazy { ClientSimpleDao() }.value
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findById(entityManager: EntityManager, clientId: UUID): Client {
        try {
            return entityManager.createQuery("FROM Client WHERE id=:clientId", Client::class.java)
                .setParameter("clientId", clientId).singleResult
        } catch (e: Exception) {
            throw
                if (e is NoResultException || e is NullPointerException) EntityNotFoundException("Запись не найдена!", e)
                else e
        }
    }

    override fun findAll(entityManager: EntityManager): LinkedHashSet<Client> {
        return entityManager.createQuery("FROM Client", Client::class.java)
            .resultList.toCollection(LinkedHashSet<Client>())
    }

    /** Получение по [transportId] */
    override fun findByTransportId(entityManager: EntityManager, transportId: UUID): LinkedHashSet<Client> {
        val namedQuery: TypedQuery<Client> =
            entityManager.createQuery("SELECT c FROM Client c JOIN c.transports ct WHERE ct.id=:transportId", Client::class.java)
                .setParameter("transportId", transportId)
        return namedQuery.resultList.toCollection(LinkedHashSet<Client>())
    }

    /** Получение кол-ва автомобилей по каждому клиенту (на экран вывести id клиента, ФИО и кол-во автомобилей;
     * Отсортировать результат по ФИО в алфавитном порядке) */
    override fun getCountTransportAndClientInfoOrderByFIOAsc(entityManager: EntityManager): LinkedHashSet<ClientWithTransportCountDto> {
        return entityManager.createQuery(CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_ORDER_BY_FIO, ClientWithTransportCountDto::class.java)
            .resultList.toCollection(LinkedHashSet())
    }

    /** Переделать предыдущий запрос таким образом, чтобы на экран выводились клиенты, у которых больше 2х машин */
    override fun getCountTransportAndClientInfoOrderByFIOAsc(entityManager: EntityManager, greaterThan: Int): LinkedHashSet<ClientWithTransportCountDto> {
        return entityManager.createQuery(CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_GREATER_THAN_X_ORDER_BY_FIO, ClientWithTransportCountDto::class.java)
            .setParameter("greaterThan", greaterThan).resultList.toCollection(LinkedHashSet())
    }

    /** Получение ФИО клиентов, у которых есть Transport с [brand] */
    override fun getFIOByTransportBrand(entityManager: EntityManager, brand: String): LinkedList<String> {
        return entityManager.createQuery("""
            SELECT new java.lang.String(trim(c.lastName || ' ' || c.firstName || ' ' || c.middleName))
            FROM Client c
            JOIN c.transports ct
            WHERE ct.model.brand.brand=:brand""", String::class.java)
            .setParameter("brand", brand)
            .resultList.toCollection(LinkedList())
    }

    /** Софт-Удаление
     * @throws javax.persistence.OptimisticLockException */
    override fun delete(entityManager: EntityManager, client: Client): Boolean {
        entityManager.merge(client.copy(deleteAt = Date()))
        return true
    }

    /** Добавление */
    override fun add(entityManager: EntityManager, client: Client): Boolean {
        entityManager.persist(client)
        return true
    }

    /** Обновление
     * @throws javax.persistence.OptimisticLockException */
    override fun update(entityManager: EntityManager, client: Client): Boolean {
        entityManager.merge(client)
        return true
    }

}