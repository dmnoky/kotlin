package ru.tbank.service

import ru.tbank.dao.transport.TransportModelDao
import ru.tbank.dao.transport.TransportModelSimpleDao
import ru.tbank.entityManagerFactory
import ru.tbank.model.TransportModel
import ru.tbank.service.Service.Companion.newTransaction
import ru.tbank.service.Service.Companion.noTransactionRO
import java.util.*

class TransportModelService private constructor(
    private val transportModelDao: TransportModelDao = TransportModelSimpleDao.singleton
) {
    companion object {
        val singleton: TransportModelService = lazy { TransportModelService() }.value
    }

    fun findAll(): LinkedHashSet<TransportModel> {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportModelDao.findAll(it)
        }
    }

    fun findById(modelId: UUID): TransportModel {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportModelDao.findById(it, modelId)
        }
    }

    fun findByName(model: String): TransportModel {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportModelDao.findByName(it, model)
        }
    }

    fun delete(model: TransportModel): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportModelDao.delete(it, model)
        }
        return result
    }

    fun add(model: TransportModel): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportModelDao.add(it, model)
        }
        return result
    }

    fun update(model: TransportModel): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportModelDao.update(it, model)
        }
        return result
    }
}