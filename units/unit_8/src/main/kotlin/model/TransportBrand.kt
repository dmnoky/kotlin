package ru.tbank.model

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "transport_brand")
data class TransportBrand (
    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    val id: UUID,
    @Version
    val version: Long,
    val brand: String
) {
    companion object {
        fun buildNewTransportBrand(
            brand: String
        ) = TransportBrand(UUID.randomUUID(), 0, brand)
    }
}