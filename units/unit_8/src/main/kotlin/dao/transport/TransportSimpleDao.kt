package ru.tbank.dao.transport

import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.model.Transport
import java.util.*
import javax.persistence.EntityManager

class TransportSimpleDao private constructor() : TransportDao {
    companion object {
        val singleton: TransportSimpleDao = lazy { TransportSimpleDao() }.value
    }

    override fun findAll(entityManager: EntityManager): LinkedHashSet<Transport> {
        return entityManager.createQuery("FROM Transport", Transport::class.java)
            .resultList.toCollection(LinkedHashSet<Transport>())
    }

    override fun findAllByLimit(entityManager: EntityManager, limit: Int): LinkedHashSet<Transport> {
        return entityManager.createQuery("FROM Transport", Transport::class.java)
            .setMaxResults(limit).resultList.toCollection(LinkedHashSet<Transport>())
    }

    /** Получение коллекции по [clientId] */
    override fun findByClientId(entityManager: EntityManager, clientId: UUID): LinkedHashSet<Transport> {
        return entityManager.createQuery("SELECT t FROM Transport t JOIN t.clients ct WHERE ct.id=:clientId", Transport::class.java)
            .setParameter("clientId", clientId).resultList.toCollection(LinkedHashSet<Transport>())
    }

    /** Получение коллекции по [brand] */
    override fun findByBrand(entityManager: EntityManager, brand: String): LinkedHashSet<Transport> {
        return entityManager.createQuery("SELECT t FROM Transport t WHERE t.model.brand.brand=:brand", Transport::class.java)
            .setParameter("brand", brand).resultList.toCollection(LinkedHashSet<Transport>())
    }

    /** Получение по id
     * @throws EntityNotFoundException */
    override fun findById(entityManager: EntityManager, transportId: UUID): Transport {
        try {
            return entityManager.find(Transport::class.java, transportId)
        } catch (e: NullPointerException) {
            throw EntityNotFoundException("Запись не найдена!", e)
        }
    }

    /** Удаление */
    override fun delete(entityManager: EntityManager, transport: Transport): Boolean {
        if (!entityManager.contains(transport)) entityManager.find(transport::class.java, transport.id)
        entityManager.remove(transport)
        return true
    }

    /** Добавление */
    override fun add(entityManager: EntityManager, transport: Transport): Boolean {
        entityManager.persist(transport)
        return true
    }

    /** Обновление
     * @throws javax.persistence.OptimisticLockException */
    override fun update(entityManager: EntityManager, transport: Transport): Boolean {
        entityManager.merge(transport)
        return true
    }
}