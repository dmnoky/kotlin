package ru.tbank.dao.client

import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.dao.exception.OptimisticLockException
import ru.tbank.dao.transport.TransportBrandSimpleDao.Companion.TABLE_NAME_TRANSPORT_BRAND
import ru.tbank.dao.transport.TransportModelSimpleDao.Companion.TABLE_NAME_TRANSPORT_MODEL
import ru.tbank.dao.transport.TransportModelSimpleDao.Companion.TABLE_NAME_TRANSPORT_MODEL_BRAND
import ru.tbank.dao.transport.TransportSimpleDao.Companion.TABLE_NAME_TRANSPORT
import ru.tbank.dto.ClientWithTransportCount
import ru.tbank.model.Client
import ru.tbank.model.Transport
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

class ClientSimpleDao private constructor() : ClientDao {
    companion object {
        val singleton: ClientSimpleDao = lazy { ClientSimpleDao() }.value
        fun mapRecordToClient(rs: ResultSet) = Client(
            rs.getObject("client_id") as UUID,
            rs.getLong("client_version"),
            rs.getString("client_last_name"),
            rs.getString("client_first_name"),
            rs.getString("client_middle_name"),
            rs.getString("client_city")
        )

        const val TABLE_NAME_CLIENT = "client"
        const val TABLE_NAME_CLIENT_TRANSPORT = "client_transport"

        private const val CLIENT_SQL_QUERY = """
            SELECT c.id client_id, c.version client_version, c.city client_city,
                c.last_name client_last_name, c.first_name client_first_name, c.middle_name client_middle_name
            FROM $TABLE_NAME_CLIENT c
        """
        private const val CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT = """
            SELECT c.id client_id, trim(c.last_name || ' ' || c.first_name || ' ' || c.middle_name) FIO,
	            count(t.id) TRANSPORT_COUNT
            FROM $TABLE_NAME_CLIENT c
            LEFT JOIN $TABLE_NAME_CLIENT_TRANSPORT ct ON ct.client_id = c.id
            LEFT JOIN $TABLE_NAME_TRANSPORT t ON t.id = ct.transport_id
            GROUP BY (c.id)
        """
        private const val CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_ORDER_BY_FIO = "$CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT ORDER BY FIO"
        private val CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_GREATER_THAN_X_ORDER_BY_FIO = { greaterThan: Int ->
            "$CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT HAVING count(t.id) > $greaterThan ORDER BY c.last_name, c.first_name, c.middle_name"
        }
        private const val CLIENT_SQL_INSERT = """
            INSERT INTO $TABLE_NAME_CLIENT(id, last_name, first_name, middle_name, city) 
            VALUES(?,?,?,?,?)
        """
        private const val CLIENT_TRANSPORT_SQL_INSERT = """
            INSERT INTO $TABLE_NAME_CLIENT_TRANSPORT(id, transport_id, client_id) 
            VALUES(?,?,?)
        """
        private const val CLIENT_SQL_UPDATE = """
            UPDATE $TABLE_NAME_CLIENT SET version=?, last_name=?, first_name=?, middle_name=?, city=?
        """
        private val CLIENT_SQL_SELECT_VERSION = { clientId: UUID -> "SELECT version FROM $TABLE_NAME_CLIENT WHERE id='$clientId'" }
        private val CLIENT_SQL_DELETE = { clientId: UUID -> "DELETE FROM $TABLE_NAME_CLIENT WHERE id='$clientId'" }
        private val CLIENT_TRANSPORT_SQL_CLEAR_BY_CLIENT_ID = { clientId: UUID -> "DELETE FROM $TABLE_NAME_CLIENT_TRANSPORT WHERE client_id='$clientId'" }
        private val CLIENT_TRANSPORT_SQL_DELETE = { clientId: UUID -> "DELETE FROM $TABLE_NAME_CLIENT_TRANSPORT WHERE client_id='$clientId'" }
        private val CLIENT_SQL_FIND_BY_ID = { clientId: UUID -> "$CLIENT_SQL_QUERY WHERE c.id='$clientId'" }
        private val CLIENT_SQL_SELECT_BY_TRANSPORT_ID = { transportId: UUID ->
            "$CLIENT_SQL_QUERY LEFT JOIN $TABLE_NAME_CLIENT_TRANSPORT ct ON ct.client_id = c.id WHERE ct.transport_id='$transportId'"
        }
        private val CLIENT_SQL_SELECT_FIO_BY_BRAND = {
            brand: String -> """
                SELECT trim(c.last_name || ' ' || c.first_name || ' ' || c.middle_name) FIO
                FROM $TABLE_NAME_CLIENT c
                JOIN $TABLE_NAME_CLIENT_TRANSPORT ct ON ct.client_id = c.id
                JOIN $TABLE_NAME_TRANSPORT t ON t.id = ct.transport_id
                JOIN $TABLE_NAME_TRANSPORT_MODEL t_m ON t_m.id = t.model_id
                JOIN $TABLE_NAME_TRANSPORT_MODEL_BRAND t_m_b ON t_m.id = t_m_b.model_id
                JOIN $TABLE_NAME_TRANSPORT_BRAND t_b ON t_b.id = t_m_b.brand_id AND t_b.brand='$brand'
            """.trimIndent()
        }
    }

    //DataFrame.readSqlTable(dbConfig, TABLE_NAME_CLIENT).cast<Client>(verify=false).toList()
    override fun findAll(connection: Connection): LinkedHashSet<Client> {
        println(CLIENT_SQL_QUERY)
        connection.createStatement().use { st ->
            st.executeQuery(CLIENT_SQL_QUERY).use { rs ->
                val result = LinkedHashSet<Client>()
                while (rs.next()) {
                    result.add(mapRecordToClient(rs))
                }
                return result
            }
        }
        //return connection.readDataFrame("SELECT * FROM client").cast<Client>().toList()
    }

    /** Получение по [transportId] */
    override fun findByTransportId(connection: Connection, transportId: UUID): LinkedHashSet<Client> {
        val sql = CLIENT_SQL_SELECT_BY_TRANSPORT_ID(transportId)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                val result = LinkedHashSet<Client>()
                while (rs.next()) {
                    result.add(mapRecordToClient(rs))
                }
                return result
            }
        }
    }

    /** Получение кол-ва автомобилей по каждому клиенту (на экран вывести id клиента, ФИО и кол-во автомобилей;
     * Отсортировать результат по ФИО в алфавитном порядке) */
    override fun getCountTransportAndClientInfoOrderByFIOAsc(connection: Connection): LinkedHashSet<ClientWithTransportCount> {
        println(CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_ORDER_BY_FIO)
        connection.createStatement().use { st ->
            st.executeQuery(CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_ORDER_BY_FIO).use { rs ->
                val result = LinkedHashSet<ClientWithTransportCount>()
                while (rs.next()) {
                    result.add(ClientWithTransportCount(
                        rs.getObject("client_id") as UUID,
                        rs.getString("FIO"),
                        rs.getInt("TRANSPORT_COUNT")
                    ))
                }
                return result
            }
        }
    }

    /** Переделать предыдущий запрос таким образом, чтобы на экран выводились клиенты, у которых больше 2х машин */
    override fun getCountTransportAndClientInfoOrderByFIOAsc(connection: Connection, greaterThan: Int): LinkedHashSet<ClientWithTransportCount> {
        val sql = CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_GREATER_THAN_X_ORDER_BY_FIO(greaterThan)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                val result = LinkedHashSet<ClientWithTransportCount>()
                while (rs.next()) {
                    result.add(ClientWithTransportCount(
                        rs.getObject("client_id") as UUID,
                        rs.getString("FIO"),
                        rs.getInt("TRANSPORT_COUNT")
                    ))
                }
                return result
            }
        }
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findById(connection: Connection, clientId: UUID): Client {
        val sql = CLIENT_SQL_FIND_BY_ID(clientId)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_CLIENT не найдена!")
                return mapRecordToClient(rs)
            }
        }
    }

    /** Получение ФИО клиентов, у которых есть Transport с [brand] */
    override fun getFIOByTransportBrand(connection: Connection, brand: String): LinkedList<String> {
        val sql = CLIENT_SQL_SELECT_FIO_BY_BRAND(brand)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                val result = LinkedList<String>()
                while (rs.next()) {
                    result.add(rs.getString("FIO"))
                }
                return result
            }
        }
    }

    /** Удаление */
    override fun delete(connection: Connection, client: Client): Boolean {
        var sql = CLIENT_TRANSPORT_SQL_DELETE(client.id)
        println(sql)
        connection.createStatement().use {
            it.execute(sql) //удаление из интерсекшен таблицы
            sql = CLIENT_SQL_DELETE(client.id)
            println(sql)
            it.execute(sql) //удаление
            return true
        }
    }

    /** Удаление всех записей об автомобилях у определенного клиента [clientId] */
    override fun clearTransportByClientId(connection: Connection, clientId: UUID): Boolean {
        val sql = CLIENT_TRANSPORT_SQL_CLEAR_BY_CLIENT_ID(clientId)
        println(sql)
        connection.createStatement().use {
            it.execute(sql) //удаление из интерсекшен таблицы
        }
        return true
    }

    /** Добавление */
    override fun add(connection: Connection, client: Client): Boolean {
        println(CLIENT_SQL_INSERT)
        connection.prepareStatement(CLIENT_SQL_INSERT).use {
            it.setObject(1, client.id)
            it.setString(2, client.lastName)
            it.setString(3, client.firstName)
            it.setString(4, client.middleName)
            it.setString(5, client.city)
            it.execute()
            return true
        }
    }

    /** Обновление
     * @throws OptimisticLockException
     * @throws EntityNotFoundException */
    override fun update(connection: Connection, client: Client): Boolean {
        val sql = CLIENT_SQL_SELECT_VERSION(client.id)
        println(sql)
        val dbVersion = connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_CLIENT не найдена!")
                rs.getLong("version")
            }
        }
        if (dbVersion != client.version) throw OptimisticLockException("Запись ${client.id} изменена другим пользователем!")
        println(CLIENT_SQL_UPDATE)
        connection.prepareStatement(CLIENT_SQL_UPDATE).use {
            it.setObject(1, dbVersion + 1)
            it.setString(2, client.lastName)
            it.setString(3, client.firstName)
            it.setString(4, client.middleName)
            it.setString(5, client.city)
            it.execute()
            return true
        }
    }

    /** Добавляет транспорт клиенту */
    override fun linkTransport(connection: Connection, client: Client, transport: Transport): Boolean {
        println(CLIENT_TRANSPORT_SQL_INSERT)
        connection.prepareStatement(CLIENT_TRANSPORT_SQL_INSERT).use {
            it.setObject(1, UUID.randomUUID()) //id
            it.setObject(2, transport.id) //transport_id
            it.setObject(3, client.id) //client_id
            it.execute()
        }
        return true
    }
}