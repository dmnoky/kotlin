
import TestMain.Companion.findErrCause
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.postgresql.util.PSQLException
import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.model.TransportModel
import ru.tbank.service.TransportBrandService
import ru.tbank.service.TransportModelService
import java.util.stream.Stream

class TransportModelServiceTest {

    private val transportModelService = TransportModelService.singleton
    private val transportBrandService = TransportBrandService.singleton

    companion object {
        @JvmStatic
        fun add_unique_params(): Stream<Arguments> = Stream.of(
            Arguments.of("BMW", "X5"),
            Arguments.of("BMW", "X4"),
            Arguments.of("BMW", "X3"),
            Arguments.of("BMW", "X2"),
            Arguments.of("Tesla", "Model 1"),
            Arguments.of("Tesla", "Model 2"),
            Arguments.of("Tesla", "Model 3"),
            Arguments.of("AUDI", "A2"),
            Arguments.of("AUDI", "A3"),
        )
    }

    @ParameterizedTest
    @Order(1)
    @MethodSource("add_unique_params")
    fun add_unique(brandName: String, modelName: String) {
        val brand = transportBrandService.findByName(brandName)
        try {
            transportModelService.findByName(modelName)
        } catch (e: EntityNotFoundException) {
            transportModelService.add(TransportModel.buildNewTransportModel(modelName, brand))
        }

        val err = assertThrows<Exception> { transportModelService.add(TransportModel.buildNewTransportModel(modelName, brand)) }
        assert(err.findErrCause(PSQLException::class.java).message!!
            .contains("повторяющееся значение ключа нарушает ограничение уникальности"))
    }

    @Test
    @Order(2)
    fun delete_cascade() {
        var model: TransportModel
        try {
            model = transportModelService.findByName("X5")
        } catch (e: EntityNotFoundException) {
            val brand = transportBrandService.findByName("BMW")
            transportModelService.add(TransportModel.buildNewTransportModel("X5", brand))
            model = transportModelService.findByName("X5")
        }

        //println(model)
        transportModelService.delete(model)
        assertThrows<EntityNotFoundException> { transportModelService.findById(model.id) }
    }

    @Test
    fun findAll() {
        transportModelService.findAll().forEach {
            println(it)
        }
    }
}