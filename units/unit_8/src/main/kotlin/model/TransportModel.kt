package ru.tbank.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "transport_model")
data class TransportModel (
    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    val id: UUID,
    @Version
    val version: Long,
    val model: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="brand_id", nullable=false)
    @Fetch(FetchMode.JOIN)
    val brand: TransportBrand
) {
    companion object {
        fun buildNewTransportModel(
            model: String,
            brand: TransportBrand
        ) = TransportModel(UUID.randomUUID(), 0, model, brand)
    }
}