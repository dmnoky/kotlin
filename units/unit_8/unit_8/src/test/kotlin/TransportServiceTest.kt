
import TestMain.Companion.findErrCause
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.postgresql.util.PSQLException
import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.dto.TransportDto
import ru.tbank.model.Client
import ru.tbank.model.Transport
import ru.tbank.service.ClientService
import ru.tbank.service.TransportModelService
import ru.tbank.service.TransportService
import java.util.*
import kotlin.test.assertEquals

/** Получение автомобилей по данным клиента внизу
 * Остальные задания юнита в ClientServiceTest */
class TransportServiceTest {
    private val transportService = TransportService.singleton
    private val transportModel = TransportModelService.singleton
    private val clientService = ClientService.singleton

    /** EMPTY_SELECT + INSERT + SELECT_BY_ID */
    @Test
    fun notFoundById_then_add_then_findById_then_uniqueErr() {
        val transport = Transport.buildNewTransport("98223311", transportModel.findByName("A2"))
        assertThrows<EntityNotFoundException> { transportService.findById(transport.id) }
        transportService.add(transport)
        assertEquals(transportService.findById(transport.id).id, transport.id)
        val err = assertThrows<Exception> { transportService.add(transport.copy(id = UUID.randomUUID(), clients = HashSet())) } //что-то обернувшее PSQLException
        assert(err.findErrCause(PSQLException::class.java).message!!
            .contains("повторяющееся значение ключа нарушает ограничение уникальности"))
    }



    /** EMPTY_SELECT + INSERT + SELECT_BY_ID + DELETE + EMPTY_SELECT */
    @Test
    fun add_then_delete() {
        val transport = Transport.buildNewTransport("2613213211", transportModel.findByName("X4"))
        assertThrows<EntityNotFoundException> { transportService.findById(transport.id) }
        transportService.add(transport)
        assertEquals(transportService.findById(transport.id).id, transport.id)
        transportService.delete(transport)
        assertThrows<EntityNotFoundException> { transportService.findById(transport.id) }
    }

    /** Получение автомобилей по данным клиента */
    @Test
    fun findByClientId() {
        val transports: List<TransportDto> = transportService.findAllByLimit(3)
        val client = Client.buildNewClient(UUID.randomUUID().toString(), "fstName", "midName", "city").also {
            transports.forEach { t -> it.transports.add(t.toTransport()) }
        }
        clientService.add(client)
        val transportByClientId = transportService.findByClientId(client.id)
        assertEquals(transportByClientId.size, transports.size)
        assert(transportByClientId.containsAll(transports))
    }

    @Test
    fun findAll() {
        transportService.findAll().forEach {
            println(it)
        }
    }
}