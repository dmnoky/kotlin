package ru.tbank.dao.transport

import ru.tbank.model.Transport
import java.sql.Connection
import java.util.*

interface TransportDao {
    fun findAll(connection: Connection): LinkedHashSet<Transport>
    fun findAllByLimit(connection: Connection, limit: Int): LinkedHashSet<Transport>
    fun findByClientId(connection: Connection, clientId: UUID): LinkedHashSet<Transport>
    fun findById(connection: Connection, transportId: UUID): Transport
    fun findByBrand(connection: Connection, brand: String): LinkedHashSet<Transport>
    fun delete(connection: Connection, transport: Transport): Boolean
    fun add(connection: Connection, transport: Transport): Boolean
    fun update(connection: Connection, transport: Transport): Boolean
}