package ru.tbank.service

import ru.tbank.dao.transport.TransportDao
import ru.tbank.dao.transport.TransportSimpleDao
import ru.tbank.dto.TransportDto
import ru.tbank.entityManagerFactory
import ru.tbank.model.Transport
import ru.tbank.service.Service.Companion.newTransaction
import ru.tbank.service.Service.Companion.noTransactionRO
import java.util.*
import javax.persistence.EntityManager

class TransportService private constructor(
    private val entityManager: EntityManager = entityManagerFactory.createEntityManager(),
    private val transportDao: TransportDao = TransportSimpleDao.singleton
) {
    companion object {
        val singleton: TransportService = lazy { TransportService() }.value
    }

    fun findAll(): List<TransportDto> {
        entityManager.noTransactionRO {
            return transportDao.findAll(it)
                .map { t -> TransportDto(t) }.toCollection(ArrayList<TransportDto>())
        }
    }

    fun findAllByLimit(limit: Int): List<TransportDto> {
        entityManager.noTransactionRO {
            return transportDao.findAllByLimit(it, limit)
                .map { t -> TransportDto(t) }.toCollection(ArrayList<TransportDto>())
        }
    }

    fun findByClientId(clientId: UUID): List<TransportDto> {
        entityManager.noTransactionRO {
            return transportDao.findByClientId(it, clientId)
                .map { t -> TransportDto(t) }.toCollection(ArrayList<TransportDto>())
        }
    }

    fun findByBrand(brand: String): List<TransportDto> {
        entityManager.noTransactionRO {
            return transportDao.findByBrand(it, brand)
                .map { t -> TransportDto(t) }.toCollection(ArrayList<TransportDto>())
        }
    }

    fun findById(transportId: UUID): TransportDto {
        entityManager.noTransactionRO {
            return TransportDto(transportDao.findById(it, transportId))
        }
    }

    fun delete(transport: Transport): Boolean =
        entityManager.newTransaction {
            return@newTransaction transportDao.delete(it, transport)
        }

    fun add(transport: Transport): Boolean =
        entityManager.newTransaction {
            return@newTransaction transportDao.add(it, transport)
        }

    fun update(transport: Transport): Boolean =
        entityManager.newTransaction {
            return@newTransaction transportDao.update(it, transport)
        }
}