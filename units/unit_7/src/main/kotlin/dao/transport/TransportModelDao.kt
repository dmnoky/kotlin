package ru.tbank.dao.transport

import ru.tbank.model.TransportModel
import java.sql.Connection
import java.util.*

interface TransportModelDao {
    fun findAll(connection: Connection): LinkedHashSet<TransportModel>
    fun findById(connection: Connection, modelId: UUID): TransportModel
    fun findByName(connection: Connection, model: String): TransportModel
    fun delete(connection: Connection, model: TransportModel): Boolean
    fun add(connection: Connection, model: TransportModel): Boolean
    fun update(connection: Connection, model: TransportModel): Boolean
}