package ru.tbank.service

import ru.tbank.dao.client.ClientDao
import ru.tbank.dao.client.ClientSimpleDao
import ru.tbank.dao.transport.TransportDao
import ru.tbank.dao.transport.TransportSimpleDao
import ru.tbank.dto.ClientWithTransportCount
import ru.tbank.getConnection
import ru.tbank.model.Client
import ru.tbank.model.Transport
import java.util.*

class ClientService private constructor(
    private val clientDao: ClientDao = ClientSimpleDao.singleton,
    private val transportDao: TransportDao = TransportSimpleDao.singleton
) {
    companion object {
        val singleton: ClientService = lazy { ClientService() }.value
    }

    fun findAll(): List<Client> {
        getConnection().use { conn ->
            conn.isReadOnly = true
            return clientDao.findAll(conn).map {
                //селект транспорта - хз, как правильнее - тут или в дао селектить и разбирать результсет в цилке там
                it.transports.addAll(transportDao.findByClientId(conn, it.id))
                it
            }
        }
    }

    /** Получение */
    fun findById(clientId: UUID): Client {
        getConnection().use { conn ->
            conn.isReadOnly = true
            return clientDao.findById(conn, clientId).also {
                it.transports.addAll(transportDao.findByClientId(conn, it.id))
            }
        }
    }

    /** Получение ФИО клиентов, у которых есть марка [brand], например BMW */
    fun getFIOByTransportBrand(brand: String): List<String> {
        getConnection().use {
            it.isReadOnly = true
            return clientDao.getFIOByTransportBrand(it, brand)
        }
    }

    /** Получение кол-ва автомобилей по каждому клиенту (на экран вывести id клиента, ФИО и кол-во автомобилей;
     * Отсортировать результат по ФИО в алфавитном порядке) */
    fun getCountTransportAndClientInfoOrderByFIOAsc(greaterThan: Int): LinkedHashSet<ClientWithTransportCount> {
        getConnection().use {
            it.isReadOnly = true
            return clientDao.getCountTransportAndClientInfoOrderByFIOAsc(it, greaterThan)
        }
    }

    /** Переделать предыдущий запрос таким образом, чтобы на экран выводились клиенты, у которых больше 2х машин */
    fun getCountTransportAndClientInfoOrderByFIOAsc(): LinkedHashSet<ClientWithTransportCount> {
        getConnection().use {
            it.isReadOnly = true
            return clientDao.getCountTransportAndClientInfoOrderByFIOAsc(it)
        }
    }

    /** Удаление */
    fun delete(client: Client): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                clientDao.delete(it, client)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    /** Удаление всех записей об автомобилях у определенного клиента */
    fun clearTransportByClientId(clientId: UUID): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                clientDao.clearTransportByClientId(it, clientId)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    /** Добавление */
    fun add(client: Client): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                clientDao.add(it, client)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    /** Обновление */
    fun update(client: Client): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                clientDao.update(it, client)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }

    /** Добавляет транспорт клиенту */
    fun linkTransport(client: Client, transport: Transport): Boolean {
        getConnection().use {
            it.autoCommit = false
            try {
                clientDao.linkTransport(it, client, transport)
            } catch (err : Exception) {
                it.rollback()
                throw err
            }
            it.commit()
        }
        return true
    }
}