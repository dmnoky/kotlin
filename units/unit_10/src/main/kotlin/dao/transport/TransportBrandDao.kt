package ru.tbank.dao.transport

import ru.tbank.model.TransportBrand
import java.util.*
import javax.persistence.EntityManager

interface TransportBrandDao {
    fun findAll(entityManager: EntityManager): LinkedHashSet<TransportBrand>
    fun findById(entityManager: EntityManager, brandId: UUID): TransportBrand
    fun findByName(entityManager: EntityManager, brand: String): TransportBrand
    fun delete(entityManager: EntityManager, brand: TransportBrand): Boolean
    fun add(entityManager: EntityManager, brand: TransportBrand): Boolean
    fun update(entityManager: EntityManager, brand: TransportBrand): Boolean
}