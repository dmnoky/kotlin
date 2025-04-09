package ru.tbank.dao.transport

import ru.tbank.model.TransportBrand
import java.sql.Connection
import java.util.*

interface TransportBrandDao {
    fun findAll(connection: Connection): LinkedHashSet<TransportBrand>
    fun findById(connection: Connection, brandId: UUID): TransportBrand
    fun findByName(connection: Connection, brand: String): TransportBrand
    fun delete(connection: Connection, brand: TransportBrand): Boolean
    fun add(connection: Connection, brand: TransportBrand): Boolean
    fun update(connection: Connection, brand: TransportBrand): Boolean
}