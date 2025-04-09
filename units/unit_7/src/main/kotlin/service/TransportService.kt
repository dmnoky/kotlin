package ru.tbank.service

import ru.tbank.dao.client.ClientDao
import ru.tbank.dao.client.ClientSimpleDao
import ru.tbank.dao.transport.TransportDao
import ru.tbank.dao.transport.TransportSimpleDao
import ru.tbank.getConnection
import ru.tbank.model.Transport
import java.util.*

class TransportService private constructor(
    private val transportDao: TransportDao = TransportSimpleDao.singleton,
    private val clientDao: ClientDao = ClientSimpleDao.singleton
) {
    companion object {
        val singleton: TransportService = lazy { TransportService() }.value
    }

    fun findAll(): List<Transport> {
        getConnection().use { conn ->
            conn.isReadOnly = true
            return transportDao.findAll(conn).map {
                //селект клиентов - хз, как правильнее - тут или в дао селектить и разбирать результсет в цилке там
                it.clients.addAll(clientDao.findByTransportId(conn, it.id))
                it
            }
        }
    }

    fun findAllByLimit(limit: Int): List<Transport> {
        getConnection().use { conn ->
            conn.isReadOnly = true
            return transportDao.findAllByLimit(conn, limit).map {
                //селект клиентов - хз, как правильнее - тут или в дао селектить и разбирать результсет в цилке там
                it.clients.addAll(clientDao.findByTransportId(conn, it.id))
                it
            }
        }
    }

    fun findByClientId(clientId: UUID): List<Transport> {
        getConnection().use { conn ->
            conn.isReadOnly = true
            return transportDao.findByClientId(conn, clientId).map {
                it.clients.addAll(clientDao.findByTransportId(conn, it.id))
                it
            }
        }
    }

    fun findByBrand(brand: String): List<Transport> {
        getConnection().use { conn ->
            conn.isReadOnly = true
            return transportDao.findByBrand(conn, brand).map {
                it.clients.addAll(clientDao.findByTransportId(conn, it.id))
                it
            }
        }
    }

    fun findById(transportId: UUID): Transport {
        getConnection().use {
            it.isReadOnly = true
            return transportDao.findById(it, transportId).apply {
                this.clients.addAll(clientDao.findByTransportId(it, this.id))
            }
        }
    }

    fun delete(transport: Transport): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportDao.delete(it, transport)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    fun add(transport: Transport): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportDao.add(it, transport)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    fun update(transport: Transport): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                transportDao.update(it, transport)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }
}