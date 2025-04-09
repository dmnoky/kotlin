package ru.tbank.model

import java.util.*

data class TransportBrand (
    val id: UUID,
    val version: Long,
    val brand: String,
) {
    companion object {
        fun buildNewTransportBrand(
            brand: String
        ) = TransportBrand(UUID.randomUUID(), 0, brand)
    }
}