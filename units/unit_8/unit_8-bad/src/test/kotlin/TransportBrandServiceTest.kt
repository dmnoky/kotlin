
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.postgresql.util.PSQLException
import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.model.TransportBrand
import ru.tbank.service.TransportBrandService

class TransportBrandServiceTest {

    private val transportBrandService = TransportBrandService.singleton

    @ParameterizedTest
    @Order(1)
    @ValueSource(strings = ["BMW", "Tesla", "AUDI", "Volga", "Mazda"])
    fun add_unique(brand: String) {
        transportBrandService.add(TransportBrand.buildNewTransportBrand(brand))
        val err = assertThrows<PSQLException> { transportBrandService.add(TransportBrand.buildNewTransportBrand(brand)) }
        assert(err.message!!.contains("повторяющееся значение ключа нарушает ограничение уникальности"))
    }

    @Test
    @Order(2)
    fun delete_cascade() {
        val first = transportBrandService.findAll().first
        transportBrandService.delete(first)
        assertThrows<EntityNotFoundException> { transportBrandService.findById(first.id) }
    }
}