package ru.tbank.dao.transport

import ru.tbank.model.Transport
import java.util.*
import javax.persistence.EntityManager

interface TransportDao {
    fun findAll(entityManager: EntityManager): LinkedHashSet<Transport>
    fun findAllByLimit(entityManager: EntityManager, limit: Int): LinkedHashSet<Transport>
    fun findByClientId(entityManager: EntityManager, clientId: UUID): LinkedHashSet<Transport>
    fun findById(entityManager: EntityManager, transportId: UUID): Transport
    fun findByBrand(entityManager: EntityManager, brand: String): LinkedHashSet<Transport>
    fun delete(entityManager: EntityManager, transport: Transport): Boolean
    fun add(entityManager: EntityManager, transport: Transport): Boolean
    fun update(entityManager: EntityManager, transport: Transport): Boolean
}