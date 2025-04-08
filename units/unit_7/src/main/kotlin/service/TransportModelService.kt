package ru.tbank.service

import ru.tbank.dao.transport.TransportModelDao
import ru.tbank.dao.transport.TransportModelSimpleDao
import ru.tbank.getConnection
import ru.tbank.model.TransportModel
import java.util.*

class TransportModelService private constructor(
    private val transportModelDao: TransportModelDao = TransportModelSimpleDao.singleton
) {
    companion object {
        val singleton: TransportModelService = lazy { TransportModelService() }.value
    }

    fun findAll(): LinkedHashSet<TransportModel> {
        getConnection().use {
            it.isReadOnly = true
            return transportModelDao.findAll(it)
        }
    }

    fun findById(modelId: UUID): TransportModel {
        getConnection().use {
            it.isReadOnly = true
            return transportModelDao.findById(it, modelId)
        }
    }

    fun findByName(model: String): TransportModel {
        getConnection().use {
            it.isReadOnly = true
            return transportModelDao.findByName(it, model)
        }
    }

    fun delete(model: TransportModel): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportModelDao.delete(it, model)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    fun add(model: TransportModel): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportModelDao.add(it, model)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    fun update(model: TransportModel): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportModelDao.update(it, model)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }
}