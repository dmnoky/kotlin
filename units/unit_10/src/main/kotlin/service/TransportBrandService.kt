package ru.tbank.service

import ru.tbank.dao.transport.TransportBrandDao
import ru.tbank.dao.transport.TransportBrandSimpleDao
import ru.tbank.dto.transport.TransportBrandDto
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

    fun findAll(): LinkedHashSet<TransportBrandDto> {
        entityManager.noTransactionRO {
            return transportBrandDao.findAll(it)
                .map { e -> TransportBrandDto(e) }.toCollection(LinkedHashSet<TransportBrandDto>())
        }
    }

    fun findById(brandId: UUID): TransportBrandDto {
        entityManager.noTransactionRO {
            return TransportBrandDto(transportBrandDao.findById(it, brandId))
        }
    }

    fun findByName(brand: String): TransportBrandDto {
        entityManager.noTransactionRO {
            return TransportBrandDto(transportBrandDao.findByName(it, brand))
        }
    }

    fun delete(brand: TransportBrand): Boolean = entityManager.newTransaction {
            return@newTransaction transportBrandDao.delete(it, brand)
        }

    fun add(brand: TransportBrand): Boolean = entityManager.newTransaction {
        return@newTransaction transportBrandDao.add(it, brand)
    }

    fun update(brand: TransportBrand): Boolean {
        var result = false
        entityManager.newTransaction {
            result = transportBrandDao.update(it, brand)
        }
        return result
    }
}