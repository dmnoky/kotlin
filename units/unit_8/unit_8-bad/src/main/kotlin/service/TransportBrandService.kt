package ru.tbank.service

import ru.tbank.dao.transport.TransportBrandDao
import ru.tbank.dao.transport.TransportBrandSimpleDao
import ru.tbank.entityManagerFactory
import ru.tbank.model.TransportBrand
import ru.tbank.service.Service.Companion.newTransaction
import ru.tbank.service.Service.Companion.noTransactionRO
import java.util.*

class TransportBrandService private constructor(
    private val transportBrandDao: TransportBrandDao = TransportBrandSimpleDao.singleton
) {
    companion object {
        val singleton: TransportBrandService = lazy { TransportBrandService() }.value
    }

    fun findAll(): LinkedHashSet<TransportBrand> {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportBrandDao.findAll(it)
        }
    }

    fun findById(brandId: UUID): TransportBrand {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportBrandDao.findById(it, brandId)
        }
    }

    fun findByName(brand: String): TransportBrand {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportBrandDao.findByName(it, brand)
        }
    }

    fun delete(brand: TransportBrand): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportBrandDao.delete(it, brand)
        }
        return result
    }

    fun add(brand: TransportBrand): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportBrandDao.add(it, brand)
        }
        return result
    }

    fun update(brand: TransportBrand): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportBrandDao.update(it, brand)
        }
        return result
    }
}