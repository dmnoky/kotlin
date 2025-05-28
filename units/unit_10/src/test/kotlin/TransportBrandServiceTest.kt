
import TestMain.Companion.findErrCause
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.postgresql.util.PSQLException
import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.model.TransportBrand
import ru.tbank.service.TransportBrandService

@Order(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TransportBrandServiceTest {
    private val transportBrandService = TransportBrandService.singleton

    @ParameterizedTest
    @Order(1)
    @ValueSource(strings = ["BMW", "Tesla", "AUDI", "Volga", "Mazda", "Jiga"])
    fun add_unique(brand: String) {
        transportBrandService.add(TransportBrand.buildNewTransportBrand(brand))
        val err = assertThrows<Exception> { transportBrandService.add(TransportBrand.buildNewTransportBrand(brand)) }
        assert(err.findErrCause(PSQLException::class.java).message!!
            .contains("duplicate key value violates unique constraint"))
    }

    @Test
    @Order(2)
    fun delete_cascade() {
        val toDel = transportBrandService.findByName("Jiga")
        transportBrandService.delete(toDel.toEntity())
        assertThrows<EntityNotFoundException> { transportBrandService.findById(toDel.id!!) }
    }
}