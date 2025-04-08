package ru.tbank.dao.transport

import ru.tbank.dao.client.ClientSimpleDao.Companion.TABLE_NAME_CLIENT_TRANSPORT
import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.dao.exception.OptimisticLockException
import ru.tbank.dao.transport.TransportBrandSimpleDao.Companion.TABLE_NAME_TRANSPORT_BRAND
import ru.tbank.dao.transport.TransportModelSimpleDao.Companion.TABLE_NAME_TRANSPORT_MODEL
import ru.tbank.dao.transport.TransportModelSimpleDao.Companion.TABLE_NAME_TRANSPORT_MODEL_BRAND
import ru.tbank.dao.transport.TransportModelSimpleDao.Companion.mapRecordToTransportModel
import ru.tbank.model.Transport
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

class TransportSimpleDao private constructor() : TransportDao {
    companion object {
        val singleton: TransportSimpleDao = lazy { TransportSimpleDao() }.value

        fun mapRecordToTransport(rs: ResultSet) = Transport(
            rs.getObject("transport_id") as UUID,
            rs.getLong("transport_version"),
            rs.getString("transport_gos_reg_num"),
            mapRecordToTransportModel(rs)
        )

        const val TABLE_NAME_TRANSPORT = "transport"

        private const val TRANSPORT_SQL_INSERT = """
            INSERT INTO $TABLE_NAME_TRANSPORT(id, gos_reg_num, model_id) 
            VALUES(?,?,?)
        """
        private const val TRANSPORT_SQL_UPDATE = """
            UPDATE $TABLE_NAME_TRANSPORT SET version=?, gos_reg_num=?, model_id=?
        """
        private const val TRANSPORT_SQL_QUERY = """
            SELECT  t.id transport_id, t.version transport_version, t.gos_reg_num transport_gos_reg_num,
                    t_m.id model_id, t_m.version model_version, t_m.model model_model, 
                    t_b.id brand_id, t_b.version brand_version, t_b.brand brand_brand
            FROM $TABLE_NAME_TRANSPORT t
            JOIN $TABLE_NAME_TRANSPORT_MODEL t_m ON t_m.id = t.model_id
            JOIN $TABLE_NAME_TRANSPORT_MODEL_BRAND t_m_b ON t_m.id = t_m_b.model_id
            JOIN $TABLE_NAME_TRANSPORT_BRAND t_b ON t_b.id = t_m_b.brand_id
        """
        private val TRANSPORT_SQL_QUERY_LIMIT = { limit: Int -> "$TRANSPORT_SQL_QUERY limit $limit" }
        private val TRANSPORT_SQL_FIND_BY_ID = { id: UUID -> "$TRANSPORT_SQL_QUERY WHERE t.id='$id'" }
        private val TRANSPORT_SQL_FIND_BY_BRAND = { brand: String -> "$TRANSPORT_SQL_QUERY WHERE t_b.brand='$brand'" }
        private val TRANSPORT_SQL_SELECT_BY_CLIENT_ID = { clientId: UUID ->
            "$TRANSPORT_SQL_QUERY LEFT JOIN $TABLE_NAME_CLIENT_TRANSPORT ct ON ct.transport_id = t.id WHERE ct.client_id='$clientId'"
        }
        private val TRANSPORT_SQL_SELECT_VERSION = { id: UUID -> "SELECT version FROM $TABLE_NAME_TRANSPORT WHERE id='$id'" }
        private val TRANSPORT_SQL_DELETE = { id: UUID -> "DELETE FROM $TABLE_NAME_TRANSPORT WHERE id='$id'" }
        private val CLIENT_TRANSPORT_SQL_DELETE = { transportId: UUID -> "DELETE FROM $TABLE_NAME_CLIENT_TRANSPORT WHERE transport_id='$transportId'" }
    }

    override fun findAll(connection: Connection): LinkedHashSet<Transport> {
        println(TRANSPORT_SQL_QUERY)
        connection.createStatement().use { st ->
            st.executeQuery(TRANSPORT_SQL_QUERY).use { rs ->
                val result = LinkedHashSet<Transport>()
                while (rs.next()) {
                    result.add(mapRecordToTransport(rs))
                }
                return result
            }
        }
    }

    override fun findAllByLimit(connection: Connection, limit: Int): LinkedHashSet<Transport> {
        val sql = TRANSPORT_SQL_QUERY_LIMIT(limit)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                val result = LinkedHashSet<Transport>()
                while (rs.next()) {
                    result.add(mapRecordToTransport(rs))
                }
                return result
            }
        }
    }

    /** Получение коллекции по [clientId] */
    override fun findByClientId(connection: Connection, clientId: UUID): LinkedHashSet<Transport> {
        val sql = TRANSPORT_SQL_SELECT_BY_CLIENT_ID(clientId)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                val result = LinkedHashSet<Transport>()
                while (rs.next()) {
                    result.add(mapRecordToTransport(rs))
                }
                return result
            }
        }
    }

    /** Получение коллекции по [brand] */
    override fun findByBrand(connection: Connection, brand: String): LinkedHashSet<Transport> {
        val sql = TRANSPORT_SQL_FIND_BY_BRAND(brand)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                val result = LinkedHashSet<Transport>()
                while (rs.next()) {
                    result.add(mapRecordToTransport(rs))
                }
                return result
            }
        }
    }

    /** Получение по id
     * @throws EntityNotFoundException */
    override fun findById(connection: Connection, transportId: UUID): Transport {
        val sql = TRANSPORT_SQL_FIND_BY_ID(transportId)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_TRANSPORT не найдена!")
                return mapRecordToTransport(rs)
            }
        }
    }

    /** Удаление */
    override fun delete(connection: Connection, transport: Transport): Boolean {
        var sql = CLIENT_TRANSPORT_SQL_DELETE(transport.id)
        println(sql)
        connection.createStatement().use {
            it.execute(sql) //удаление из интерсекшен таблицы
            sql = TRANSPORT_SQL_DELETE(transport.id)
            println(sql)
            it.execute(sql) //удаление
            return true
        }
    }

    /** Добавление */
    override fun add(connection: Connection, transport: Transport): Boolean {
        println(TRANSPORT_SQL_INSERT)
        connection.prepareStatement(TRANSPORT_SQL_INSERT).use {
            it.setObject(1, transport.id)
            it.setString(2, transport.gosRegNum)
            it.setObject(3, transport.model.id)
            it.execute()
        }
        return true
    }

    /** Обновление
     * @throws OptimisticLockException
     * @throws EntityNotFoundException */
    override fun update(connection: Connection, transport: Transport): Boolean {
        val sql = TRANSPORT_SQL_SELECT_VERSION(transport.id)
        println(sql)
        val dbVersion = connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_TRANSPORT не найдена!")
                rs.getLong("version")
            }
        }
        if (dbVersion != transport.version) throw OptimisticLockException("Запись ${transport.id} изменена другим пользователем!")
        println(TRANSPORT_SQL_UPDATE)
        connection.prepareStatement(TRANSPORT_SQL_UPDATE).use {
            it.setObject(1, dbVersion + 1)
            it.setString(2, transport.gosRegNum)
            it.setObject(3, transport.model.id)
            it.execute()
            return true
        }
    }
}