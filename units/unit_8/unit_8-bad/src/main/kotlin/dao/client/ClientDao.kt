package ru.tbank.dao.client

import ru.tbank.dto.ClientWithTransportCountDto
import ru.tbank.model.Client
import java.util.*
import javax.persistence.EntityManager

interface ClientDao {
    fun findAll(entityManager: EntityManager): LinkedHashSet<Client>
    fun findById(entityManager: EntityManager, clientId: UUID): Client
    fun findByTransportId(entityManager: EntityManager, transportId: UUID): LinkedHashSet<Client>
    fun getFIOByTransportBrand(entityManager: EntityManager, brand: String): List<String>
    fun delete(entityManager: EntityManager, client: Client): Boolean
    fun add(entityManager: EntityManager, client: Client): Boolean
    fun update(entityManager: EntityManager, client: Client): Boolean
    fun getCountTransportAndClientInfoOrderByFIOAsc(entityManager: EntityManager, greaterThan: Int): LinkedHashSet<ClientWithTransportCountDto>
    fun getCountTransportAndClientInfoOrderByFIOAsc(entityManager: EntityManager): LinkedHashSet<ClientWithTransportCountDto>
}