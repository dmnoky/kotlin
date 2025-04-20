package ru.tbank.dto

import java.util.*

data class ClientWithTransportCountDto (
    val id: UUID,
    val fio: String,
    val count: Int
)

/** https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/queryhql.html
 * SELECT c.id, trim(c.last_name || ' ' || c.first_name || ' ' || c.middle_name) fio, count(t.id) count
 * FROM unit8.client c
 * LEFT JOIN unit8.client_transport ct ON ct.client_id = c.id
 * LEFT JOIN unit8.transport t ON t.id = ct.transport_id
 * WHERE c.delete_at is null
 * GROUP BY (c.id)
 * ORDER BY c.last_name, c.first_name, c.middle_name
 * */
const val CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT = """
    SELECT new ru.tbank.dto.ClientWithTransportCountDto(c.id, trim(c.lastName || ' ' || c.firstName || ' ' || c.middleName), c.transports.size)
    FROM Client c, Transport t
    WHERE c.deleteAt is null
    GROUP BY (c.id)
"""
const val ORDER_BY_FIO = "ORDER BY c.lastName, c.firstName, c.middleName"
const val CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_ORDER_BY_FIO = "$CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT $ORDER_BY_FIO"
/** HAVING count(t.id) > :greaterThan */
const val CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT_GREATER_THAN_X_ORDER_BY_FIO =
    "$CLIENT_SQL_QUERY_WITH_TRANSPORT_COUNT HAVING c.transports.size > :greaterThan $ORDER_BY_FIO"
