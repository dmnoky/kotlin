package ru.tbank.service

import ru.tbank.dao.transport.TransportDao
import ru.tbank.dao.transport.TransportSimpleDao
import ru.tbank.dto.TransportDto
import ru.tbank.entityManagerFactory
import ru.tbank.model.Transport
import ru.tbank.service.Service.Companion.newTransaction
import ru.tbank.service.Service.Companion.noTransactionRO
import java.util.*

class TransportService private constructor(
    private val transportDao: TransportDao = TransportSimpleDao.singleton
) {
    companion object {
        val singleton: TransportService = lazy { TransportService() }.value
    }

    fun findAll(): List<TransportDto> {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportDao.findAll(it)
                .map { t -> TransportDto(t) }.toCollection(ArrayList<TransportDto>())
        }
    }

    fun findAllByLimit(limit: Int): List<TransportDto> {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportDao.findAllByLimit(it, limit)
                .map { t -> TransportDto(t) }.toCollection(ArrayList<TransportDto>())
        }
    }

    fun findByClientId(clientId: UUID): List<TransportDto> {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportDao.findByClientId(it, clientId)
                .map { t -> TransportDto(t) }.toCollection(ArrayList<TransportDto>())
        }
    }

    fun findByBrand(brand: String): List<TransportDto> {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return transportDao.findByBrand(it, brand)
                .map { t -> TransportDto(t) }.toCollection(ArrayList<TransportDto>())
        }
    }

    fun findById(transportId: UUID): TransportDto {
        entityManagerFactory.createEntityManager().noTransactionRO {
            return TransportDto(transportDao.findById(it, transportId))
        }
    }

    fun delete(transport: Transport): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportDao.delete(it, transport)
        }
        return result
    }

    fun add(transport: Transport): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportDao.add(it, transport)
        }
        return result
    }

    fun update(transport: Transport): Boolean {
        var result = false
        entityManagerFactory.createEntityManager().newTransaction {
            result = transportDao.update(it, transport)
        }
        return result
    }
}