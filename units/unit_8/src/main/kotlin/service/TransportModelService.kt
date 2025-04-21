package ru.tbank.service

import ru.tbank.dao.transport.TransportModelDao
import ru.tbank.dao.transport.TransportModelSimpleDao
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

    fun findAll(): LinkedHashSet<TransportModel> {
        entityManager.noTransactionRO {
            return transportModelDao.findAll(it)
        }
    }

    fun findById(modelId: UUID): TransportModel {
        entityManager.noTransactionRO {
            return transportModelDao.findById(it, modelId)
        }
    }

    fun findByName(model: String): TransportModel {
        entityManager.noTransactionRO {
            return transportModelDao.findByName(it, model)
        }
    }

    fun delete(model: TransportModel): Boolean {
        var result = false
        entityManager.newTransaction {
            result = transportModelDao.delete(it, model)
        }
        return result
    }

    fun add(model: TransportModel): Boolean {
        var result = false
        entityManager.newTransaction {
            result = transportModelDao.add(it, model)
        }
        return result
    }

    fun update(model: TransportModel): Boolean {
        var result = false
        entityManager.newTransaction {
            result = transportModelDao.update(it, model)
        }
        return result
    }
}