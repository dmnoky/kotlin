package ru.tbank.service

import ru.tbank.dao.client.ClientDao
import ru.tbank.dao.client.ClientSimpleDao
import ru.tbank.dto.ClientDto
import ru.tbank.dto.ClientWithTransportCountDto
import ru.tbank.entityManagerFactory
import ru.tbank.model.Client
import ru.tbank.service.Service.Companion.newTransaction
import ru.tbank.service.Service.Companion.noTransactionRO
import java.util.*
import javax.persistence.EntityManager


class ClientService private constructor(
    private val entityManager: EntityManager = entityManagerFactory.createEntityManager(),
    private val clientDao: ClientDao = ClientSimpleDao.singleton
) {
    companion object {
        val singleton: ClientService = lazy { ClientService() }.value
    }

    fun findAll(): List<ClientDto> {
        entityManager.noTransactionRO {
            return clientDao.findAll(it)
                .map { c -> ClientDto(c) }.toCollection(ArrayList<ClientDto>())
        }
    }

    /** Получение */
    fun findById(clientId: UUID): ClientDto {
        entityManager.noTransactionRO {
            return ClientDto(clientDao.findById(it, clientId))
        }
    }

    /** Получение ФИО клиентов, у которых есть марка [brand], например BMW */
    fun getFIOByTransportBrand(brand: String): List<String> {
        entityManager.noTransactionRO {
            return clientDao.getFIOByTransportBrand(it, brand)
        }
    }

    /** Получение клиентов по [transportId] */
    fun findByTransportId(transportId: UUID): List<ClientDto> {
        entityManager.noTransactionRO {
            return clientDao.findByTransportId(it, transportId)
                .map { c -> ClientDto(c) }.toCollection(ArrayList<ClientDto>())
        }
    }

    /** Получение кол-ва автомобилей по каждому клиенту (на экран вывести id клиента, ФИО и кол-во автомобилей;
     * Отсортировать результат по ФИО в алфавитном порядке) */
    fun getCountTransportAndClientInfoOrderByFIOAsc(greaterThan: Int): LinkedHashSet<ClientWithTransportCountDto> {
        entityManager.noTransactionRO {
            return clientDao.getCountTransportAndClientInfoOrderByFIOAsc(it, greaterThan)
        }
    }

    /** Переделать предыдущий запрос таким образом, чтобы на экран выводились клиенты, у которых больше 2х машин */
    fun getCountTransportAndClientInfoOrderByFIOAsc(): LinkedHashSet<ClientWithTransportCountDto> {
        entityManager.noTransactionRO {
            return clientDao.getCountTransportAndClientInfoOrderByFIOAsc(it)
        }
    }

    /** Удаление */
    fun delete(client: Client): Boolean {
        var result = false
        entityManager.newTransaction {
            result = clientDao.delete(it, client)
        }
        return result
    }

    /** Удаление всех записей об автомобилях у определенного клиента */
    fun clearTransportByClientId(client: Client): Boolean {
        var result = false
        entityManager.newTransaction {
            client.transports.clear()
            result = clientDao.update(it, client)
        }
        return result
    }

    /** Добавление */
    fun add(client: Client): Boolean {
        var result = false
        entityManager.newTransaction {
            result = clientDao.add(it, client)
        }
        return result
    }

    /** Обновление */
    fun update(client: Client): Boolean {
        var result = false
        entityManager.newTransaction {
            result = clientDao.update(it, client)
        }
        return result
    }
}