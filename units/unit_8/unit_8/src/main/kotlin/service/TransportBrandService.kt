package ru.tbank.service

import ru.tbank.dao.transport.TransportBrandDao
import ru.tbank.dao.transport.TransportBrandSimpleDao
import ru.tbank.entityManagerFactory
import ru.tbank.model.TransportBrand
import ru.tbank.service.Service.Companion.newTransaction
import ru.tbank.service.Service.Companion.noTransactionRO
import java.util.*
import javax.persistence.EntityManager

class TransportBrandService private constructor(
    private val entityManager: EntityManager = entityManagerFactory.createEntityManager(),
    private val transportBrandDao: TransportBrandDao = TransportBrandSimpleDao.singleton
) {
    companion object {
        val singleton: TransportBrandService = lazy { TransportBrandService() }.value
    }

    fun findAll(): LinkedHashSet<TransportBrand> {
        entityManager.noTransactionRO {
            return transportBrandDao.findAll(it)
        }
    }

    fun findById(brandId: UUID): TransportBrand {
        entityManager.noTransactionRO {
            return transportBrandDao.findById(it, brandId)
        }
    }

    fun findByName(brand: String): TransportBrand {
        entityManager.noTransactionRO {
            return transportBrandDao.findByName(it, brand)
        }
    }

    fun delete(brand: TransportBrand): Boolean {
        var result = false
        entityManager.newTransaction {
            result = transportBrandDao.delete(it, brand)
        }
        return result
    }

    fun add(brand: TransportBrand): Boolean {
        var result = false
        entityManager.newTransaction {
            result = transportBrandDao.add(it, brand)
        }
        return result
    }

    fun update(brand: TransportBrand): Boolean {
        var result = false
        entityManager.newTransaction {
            result = transportBrandDao.update(it, brand)
        }
        return result
    }
}