
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.dto.TransportDto
import ru.tbank.model.Client
import ru.tbank.service.ClientService
import ru.tbank.service.TransportService
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/** Получение автомобилей по данным клиента юнита в TransportServiceTest
 * Остальные задания юнита внизу */
@Order(5)
class ClientServiceTest {
    private val clientService = ClientService.singleton
    private val transportService = TransportService.singleton

    private val tstClient = Client.buildNewClient("tstClient", "tstClient", "tstClient", "city")

    /** EMPTY_SELECT + INSERT + SELECT_BY_ID */
    @Test
    fun notFoundById_then_add_then_findById() {
        val client = tstClient.copy(UUID.randomUUID())
        assertThrows<EntityNotFoundException> { clientService.findById(client.id) }
        clientService.add(client)
        assertEquals(clientService.findById(client.id).id, client.id)
    }

    /** INSERT + SELECT_ALL + UPDATE */
    @Test
    fun findAll_then_updateFstClient() {
        clientService.add(tstClient.copy(UUID.randomUUID()))
        val findAll = clientService.findAll()
        assert(findAll.isNotEmpty())
        val client = findAll[0].copy(lastName = "newLastName ${UUID.randomUUID()}")
        clientService.update(client.toClient())
        assertEquals(clientService.findById(client.id!!).lastName, client.lastName)
    }

    /** EMPTY_SELECT + INSERT + SELECT_BY_ID + DELETE + EMPTY_SELECT */
    @Test
    fun add_then_delete() {
        val client = tstClient.copy(UUID.randomUUID())
        assertThrows<EntityNotFoundException> { clientService.findById(client.id) }
        clientService.add(client)
        assertEquals(clientService.findById(client.id).id, client.id)
        clientService.delete(client)
        assertThrows<EntityNotFoundException> { clientService.findById(client.id) }
    }

    /** Оптимистическая блокировка */
    @Test
    fun update_optimisticLock() {
        val client = tstClient.copy(UUID.randomUUID())
        clientService.add(client)
        val updatedClient = client.copy(lastName = "newLastName ${UUID.randomUUID()}")
        clientService.update(updatedClient)
        val updatedClientIncVersion = clientService.findById(client.id)
        assertEquals(updatedClientIncVersion.lastName, updatedClient.lastName)
        assertEquals(updatedClientIncVersion.version, updatedClient.version+1)
        assertThrows<javax.persistence.OptimisticLockException> { clientService.update(updatedClient) }
    }

    /** Получение ФИО клиентов, у которых есть автомобиль марки BMW
     * Остальные задания юнита в TransportServiceTest */
    @Test
    fun getFIOByTransportBrand() {
				transportService.findAll().forEach(::println)
        val transports = transportService.findByBrand("BMW")
        val client = Client.buildNewClient(UUID.randomUUID().toString(), "fstName", "midName", "city").also {
            it.transports.add(transports.first().toTransport())
        }
        clientService.add(client)
        val fioByTransportBrand = clientService.getFIOByTransportBrand("BMW")
        assertContains(fioByTransportBrand, client.getFIO())
    }

    /** Удаление всех записей об автомобилях у определенного клиента */
    @Test
    fun clearTransportByClientId() {
        val transports = transportService.findAllByLimit(3)
        val client = createClientWithTransports(transports)
        assertEquals(client.transports.size, 3)

        clientService.clearTransportByClientId(client)

        val clientNoTransport = clientService.findById(client.id)
        assertEquals(clientNoTransport.transports!!.size, 0)
    }

    /** Получение кол-ва автомобилей по каждому клиенту (на экран вывести id клиента, ФИО и кол-во автомобилей;
     * Отсортировать результат по ФИО в алфавитном порядке) */
    @Test
    fun getCountTransportAndClientInfo_orderByFIOAsc() {
        val transports = transportService.findAllByLimit(2)
        val client = createClientWithTransports(transports)
        val countTransportAndClientInfoOrderByFIOAsc = clientService.getCountTransportAndClientInfoOrderByFIOAsc()
        assertNotNull(countTransportAndClientInfoOrderByFIOAsc.firstOrNull { it.id == client.id && it.count == 2 })
    }

    /** Переделать предыдущий запрос таким образом, чтобы на экран выводились клиенты, у которых больше 2х машин */
    @Test
    fun getCountTransportAndClientInfo_whereCountTransportGreaterThan2_orderByFIOAsc() {
        val transports = transportService.findAllByLimit(3)
        val client3Transport = createClientWithTransports(transports)
        val client1Transport = createClientWithTransports(transports.subList(0, 1))
        val countTransportAndClientInfoOrderByFIOAsc = clientService.getCountTransportAndClientInfoOrderByFIOAsc(2)
        countTransportAndClientInfoOrderByFIOAsc.forEach { println(it) }
        assertNotNull(countTransportAndClientInfoOrderByFIOAsc.firstOrNull { it.id == client3Transport.id && it.count == 3 })
        assertNull(countTransportAndClientInfoOrderByFIOAsc.firstOrNull { it.id == client1Transport.id })
    }

    private fun createClientWithTransports(transports: List<TransportDto>): Client{
        val client = Client.buildNewClient(UUID.randomUUID().toString(), "fstName", "midName", "city").also {
            transports.forEach { t -> it.transports.add(t.toTransport()) }
        }
        clientService.add(client)
        return clientService.findById(client.id).toClient()
    }

    //@Test
    fun findAll() {
        clientService.findAll().forEach {
            println(it)
            it.transports?.forEach { println(it) }
        }
    }
}