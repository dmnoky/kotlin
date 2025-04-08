package ru.tbank.dao.transport

import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.dao.exception.OptimisticLockException
import ru.tbank.dao.transport.TransportBrandSimpleDao.Companion.TABLE_NAME_TRANSPORT_BRAND
import ru.tbank.dao.transport.TransportBrandSimpleDao.Companion.mapRecordToTransportBrand
import ru.tbank.model.TransportModel
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

class TransportModelSimpleDao private constructor() : TransportModelDao {
    companion object {
        val singleton: TransportModelSimpleDao = lazy { TransportModelSimpleDao() }.value
        fun mapRecordToTransportModel(rs: ResultSet) = TransportModel(
            rs.getObject("model_id") as UUID,
            rs.getLong("model_version"),
            rs.getString("model_model"),
            mapRecordToTransportBrand(rs)
        )

        const val TABLE_NAME_TRANSPORT_MODEL = "transport_model"
        const val TABLE_NAME_TRANSPORT_MODEL_BRAND = "transport_model_brand"

        private const val TRANSPORT_MODEL_SQL_INSERT = """
            INSERT INTO $TABLE_NAME_TRANSPORT_MODEL(id, model) 
            VALUES(?,?)
        """
        private const val TABLE_NAME_TRANSPORT_MODEL_BRAND_SQL_INSERT = """
            INSERT INTO $TABLE_NAME_TRANSPORT_MODEL_BRAND(id, model_id, brand_id) 
            VALUES(?,?,?)
        """
        private const val TRANSPORT_MODEL_SQL_UPDATE = """
            UPDATE $TABLE_NAME_TRANSPORT_MODEL SET version=?, model=?
        """
        private const val TRANSPORT_MODEL_SQL_QUERY = """
            SELECT  t_m.id model_id, t_m.version model_version, t_m.model model_model, 
                    t_b.id brand_id, t_b.version brand_version, t_b.brand brand_brand
            FROM $TABLE_NAME_TRANSPORT_MODEL t_m
            JOIN $TABLE_NAME_TRANSPORT_MODEL_BRAND t_m_b ON t_m.id = t_m_b.model_id
            JOIN $TABLE_NAME_TRANSPORT_BRAND t_b ON t_b.id = t_m_b.brand_id
        """
        private val TRANSPORT_MODEL_SQL_FIND_BY_ID = { id: UUID -> """
            $TRANSPORT_MODEL_SQL_QUERY WHERE t_m.id='$id'
            """.trimIndent()
        }
        private val TRANSPORT_MODEL_SQL_FIND_BY_NAME = { model: String -> """
            $TRANSPORT_MODEL_SQL_QUERY WHERE t_m.model='$model'
            """.trimIndent()
        }
        private val TRANSPORT_MODEL_SQL_SELECT_VERSION = { id: UUID -> "SELECT version FROM $TABLE_NAME_TRANSPORT_MODEL WHERE id='$id'" }
        private val TRANSPORT_MODEL_SQL_DELETE = { id: UUID -> "DELETE FROM $TABLE_NAME_TRANSPORT_MODEL WHERE id='$id'" }
        private val TRANSPORT_MODEL_BRAND_SQL_DELETE = { id: UUID -> "DELETE FROM $TABLE_NAME_TRANSPORT_MODEL_BRAND WHERE model_id='$id'" }
    }

    override fun findAll(connection: Connection): LinkedHashSet<TransportModel> {
        println(TRANSPORT_MODEL_SQL_QUERY)
        connection.createStatement().use { st ->
            st.executeQuery(TRANSPORT_MODEL_SQL_QUERY).use { rs ->
                val result = LinkedHashSet<TransportModel>()
                while (rs.next()) {
                    result.add(mapRecordToTransportModel(rs))
                }
                return result
            }
        }
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findById(connection: Connection, modelId: UUID): TransportModel {
        val sql = TRANSPORT_MODEL_SQL_FIND_BY_ID(modelId)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_TRANSPORT_MODEL не найдена!")
                return mapRecordToTransportModel(rs)
            }
        }
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findByName(connection: Connection, model: String): TransportModel {
        val sql = TRANSPORT_MODEL_SQL_FIND_BY_NAME(model)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_TRANSPORT_MODEL не найдена!")
                return mapRecordToTransportModel(rs)
            }
        }
    }

    /** Удаление */
    override fun delete(connection: Connection, model: TransportModel): Boolean {
        var sql = TRANSPORT_MODEL_BRAND_SQL_DELETE(model.id)
        println(sql)
        connection.createStatement().use {
            it.execute(sql) //удаление из интерсекшен таблицы
            sql = TRANSPORT_MODEL_SQL_DELETE(model.id)
            println(sql)
            it.execute(sql) //удаление модели
            return true
        }
    }

    /** Добавление */
    override fun add(connection: Connection, model: TransportModel): Boolean {
        println(TRANSPORT_MODEL_SQL_INSERT)
        connection.prepareStatement(TRANSPORT_MODEL_SQL_INSERT).use {
            it.setObject(1, model.id)
            it.setString(2, model.model)
            it.execute()
        }
        println(TABLE_NAME_TRANSPORT_MODEL_BRAND_SQL_INSERT)
        connection.prepareStatement(TABLE_NAME_TRANSPORT_MODEL_BRAND_SQL_INSERT).use {
            it.setObject(1, UUID.randomUUID())
            it.setObject(2, model.id)
            it.setObject(3, model.brand.id)
            it.execute()
        }
        return true
    }

    /** Обновление
     * @throws OptimisticLockException
     * @throws EntityNotFoundException */
    override fun update(connection: Connection, model: TransportModel): Boolean {
        val sql = TRANSPORT_MODEL_SQL_SELECT_VERSION(model.id)
        println(sql)
        val dbVersion = connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_TRANSPORT_MODEL не найдена!")
                rs.getLong("version")
            }
        }
        if (dbVersion != model.version) throw OptimisticLockException("Запись ${model.id} изменена другим пользователем!")
        println(TRANSPORT_MODEL_SQL_UPDATE)
        connection.prepareStatement(TRANSPORT_MODEL_SQL_UPDATE).use {
            it.setObject(1, dbVersion + 1)
            it.setString(2, model.model)
            it.execute()
            return true
        }
    }
}