package ru.tbank.service

import ru.tbank.dao.transport.TransportBrandDao
import ru.tbank.dao.transport.TransportBrandSimpleDao
import ru.tbank.getConnection
import ru.tbank.model.TransportBrand
import java.util.*

class TransportBrandService private constructor(
    private val transportBrandDao: TransportBrandDao = TransportBrandSimpleDao.singleton
) {
    companion object {
        val singleton: TransportBrandService = lazy { TransportBrandService() }.value
    }

    fun findAll(): LinkedHashSet<TransportBrand> {
        getConnection().use {
            it.isReadOnly = true
            return transportBrandDao.findAll(it)
        }
    }

    fun findById(brandId: UUID): TransportBrand {
        getConnection().use {
            it.isReadOnly = true
            return transportBrandDao.findById(it, brandId)
        }
    }

    fun findByName(brand: String): TransportBrand {
        getConnection().use {
            it.isReadOnly = true
            return transportBrandDao.findByName(it, brand)
        }
    }

    fun delete(brand: TransportBrand): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportBrandDao.delete(it, brand)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    fun add(brand: TransportBrand): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportBrandDao.add(it, brand)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    fun update(brand: TransportBrand): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportBrandDao.update(it, brand)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }
}