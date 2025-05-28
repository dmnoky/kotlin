package ru.tbank.service

import ru.tbank.dao.transport.TransportModelDao
import ru.tbank.dao.transport.TransportModelSimpleDao
import ru.tbank.dto.transport.TransportModelDto
import ru.tbank.entityManagerFactory
import ru.tbank.model.TransportModel
import ru.tbank.service.Service.Companion.newTransaction
import ru.tbank.service.Service.Companion.noTransactionRO
import java.util.*
import javax.persistence.EntityManager

class TransportModelService private constructor(
    private val entityManager: EntityManager = entityManagerFactory.createEntityManager(),
    private val transportModelDao: TransportModelDao = TransportModelSimpleDao.singleton
) {
    companion object {
        val singleton: TransportModelService = lazy { TransportModelService() }.value
    }

    fun findAll(): LinkedHashSet<TransportModelDto> {
        entityManager.noTransactionRO {
            return transportModelDao.findAll(it)
                .map { e -> TransportModelDto(e) }.toCollection(LinkedHashSet<TransportModelDto>())
        }
    }

    fun findById(modelId: UUID): TransportModelDto {
        entityManager.noTransactionRO {
            return TransportModelDto(transportModelDao.findById(it, modelId))
        }
    }

    fun findByName(model: String): TransportModelDto {
        entityManager.noTransactionRO {
            return TransportModelDto(transportModelDao.findByName(it, model))
        }
    }

    fun delete(model: TransportModel): Boolean =
        entityManager.newTransaction {
            return@newTransaction transportModelDao.delete(it, model)
        }

    fun add(model: TransportModel): Boolean = entityManager.newTransaction {
            return@newTransaction transportModelDao.add(it, model)
        }

    fun update(model: TransportModel): Boolean = entityManager.newTransaction {
            return@newTransaction transportModelDao.update(it, model)
        }
}