package ru.tbank.dao.transport

import ru.tbank.model.TransportModel
import java.util.*
import javax.persistence.EntityManager

interface TransportModelDao {
    fun findAll(entityManager: EntityManager): LinkedHashSet<TransportModel>
    fun findById(entityManager: EntityManager, modelId: UUID): TransportModel
    fun findByName(entityManager: EntityManager, model: String): TransportModel
    fun delete(entityManager: EntityManager, model: TransportModel): Boolean
    fun add(entityManager: EntityManager, model: TransportModel): Boolean
    fun update(entityManager: EntityManager, model: TransportModel): Boolean
}