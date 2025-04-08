package ru.tbank.dao.client

import ru.tbank.dto.ClientWithTransportCount
import ru.tbank.model.Client
import ru.tbank.model.Transport
import java.sql.Connection
import java.util.*

interface ClientDao {
    fun findAll(connection: Connection): LinkedHashSet<Client>
    fun findById(connection: Connection, clientId: UUID): Client
    fun findByTransportId(connection: Connection, transportId: UUID): LinkedHashSet<Client>
    fun getFIOByTransportBrand(connection: Connection, brand: String): List<String>
    fun clearTransportByClientId(connection: Connection, clientId: UUID): Boolean
    fun delete(connection: Connection, client: Client): Boolean
    fun add(connection: Connection, client: Client): Boolean
    fun update(connection: Connection, client: Client): Boolean
    fun linkTransport(connection: Connection, client: Client, transport: Transport): Boolean
    fun getCountTransportAndClientInfoOrderByFIOAsc(connection: Connection, greaterThan: Int): LinkedHashSet<ClientWithTransportCount>
    fun getCountTransportAndClientInfoOrderByFIOAsc(connection: Connection): LinkedHashSet<ClientWithTransportCount>
}