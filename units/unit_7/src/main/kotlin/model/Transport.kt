package ru.tbank.model

import java.util.*

data class Transport (
    val id: UUID,
    val version: Long,
    val gosRegNum: String,
    val model: TransportModel,
    val clients: MutableSet<Client> = LinkedHashSet()
) {
    companion object {
        fun buildNewTransport(
            gosRegNum: String,
            model: TransportModel
        ) = Transport(UUID.randomUUID(), 0, gosRegNum, model)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Transport) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Transport(id=$id, version=$version, gosRegNum='$gosRegNum', model=$model)"
    }
}