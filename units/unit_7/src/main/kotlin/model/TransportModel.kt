package ru.tbank.model

import java.util.*

data class TransportModel (
    val id: UUID,
    val version: Long,
    val model: String,
    val brand: TransportBrand
) {
    companion object {
        fun buildNewTransportModel(
            model: String,
            brand: TransportBrand
        ) = TransportModel(UUID.randomUUID(), 0, model, brand)
    }
}