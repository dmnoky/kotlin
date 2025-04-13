package ru.tbank.dao.transport

import ru.tbank.dao.exception.EntityNotFoundException
import ru.tbank.dao.exception.OptimisticLockException
import ru.tbank.model.TransportBrand
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

class TransportBrandSimpleDao private constructor() : TransportBrandDao {
    companion object {
        val singleton: TransportBrandSimpleDao = lazy { TransportBrandSimpleDao() }.value

        fun mapRecordToTransportBrand(rs: ResultSet) = TransportBrand(
            rs.getObject("brand_id") as UUID,
            rs.getLong("brand_version"),
            rs.getString("brand_brand")
        )

        const val TABLE_NAME_TRANSPORT_BRAND = "transport_brand"

        private const val TRANSPORT_BRAND_SQL_INSERT = """
            INSERT INTO $TABLE_NAME_TRANSPORT_BRAND(id, brand) 
            VALUES(?,?)
        """
        private const val TRANSPORT_BRAND_SQL_UPDATE = """
            UPDATE $TABLE_NAME_TRANSPORT_BRAND SET version=?, brand=?
            WHERE id=? AND version=?
        """
        private const val TRANSPORT_BRAND_SQL_QUERY = """
            SELECT t_b.id brand_id, t_b.version brand_version, t_b.brand brand_brand
            FROM $TABLE_NAME_TRANSPORT_BRAND t_b
        """
        private val TRANSPORT_BRAND_SQL_FIND_BY_ID = { id: UUID -> "$TRANSPORT_BRAND_SQL_QUERY WHERE t_b.id='$id'" }
        private val TRANSPORT_BRAND_SQL_FIND_BY_NAME = { brand: String -> "$TRANSPORT_BRAND_SQL_QUERY WHERE t_b.brand='$brand'" }
        private val TRANSPORT_BRAND_SQL_DELETE = { id: UUID -> "DELETE FROM $TABLE_NAME_TRANSPORT_BRAND WHERE id='$id'" }
    }

    override fun findAll(connection: Connection): LinkedHashSet<TransportBrand> {
        println(TRANSPORT_BRAND_SQL_QUERY)
        connection.createStatement().use { st ->
            st.executeQuery(TRANSPORT_BRAND_SQL_QUERY).use { rs ->
                val result = LinkedHashSet<TransportBrand>()
                while (rs.next()) {
                    result.add(mapRecordToTransportBrand(rs))
                }
                return result
            }
        }
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findById(connection: Connection, brandId: UUID): TransportBrand {
        val sql = TRANSPORT_BRAND_SQL_FIND_BY_ID(brandId)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_TRANSPORT_BRAND не найдена!")
                return mapRecordToTransportBrand(rs)
            }
        }
    }

    /** Получение
     * @throws EntityNotFoundException */
    override fun findByName(connection: Connection, brand: String): TransportBrand {
        val sql = TRANSPORT_BRAND_SQL_FIND_BY_NAME(brand)
        println(sql)
        connection.createStatement().use { st ->
            st.executeQuery(sql).use { rs ->
                if (!rs.next()) throw EntityNotFoundException("Запись в таблице $TABLE_NAME_TRANSPORT_BRAND не найдена!")
                return mapRecordToTransportBrand(rs)
            }
        }
    }

    /** Удаление */
    override fun delete(connection: Connection, brand: TransportBrand): Boolean {
        val sql = TRANSPORT_BRAND_SQL_DELETE(brand.id)
        println(sql)
        connection.createStatement().use {
            it.execute(sql) //удаление бренда
            return true
        }
    }

    /** Добавление */
    override fun add(connection: Connection, brand: TransportBrand): Boolean {
        println(TRANSPORT_BRAND_SQL_INSERT)
        connection.prepareStatement(TRANSPORT_BRAND_SQL_INSERT).use {
            it.setObject(1, brand.id)
            it.setString(2, brand.brand)
            it.execute()
            return true
        }
    }

    /** Обновление
     * @throws OptimisticLockException */
    override fun update(connection: Connection, brand: TransportBrand): Boolean {
        println(TRANSPORT_BRAND_SQL_UPDATE)
        connection.prepareStatement(TRANSPORT_BRAND_SQL_UPDATE).use {
            it.setLong(1, brand.version + 1)
            it.setString(2, brand.brand)
            //WHERE
            it.setObject(3, brand.id)
            it.setLong(4, brand.version)
            it.execute()
            if (it.updateCount == 1) return true
            else throw OptimisticLockException("Запись ${brand.id} изменена другим пользователем!")
        }
    }
}