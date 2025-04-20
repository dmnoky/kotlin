package ru.tbank.dto

import ru.tbank.model.Client
import ru.tbank.model.Transport
import ru.tbank.model.TransportModel
import java.util.*

data class TransportDto(
    val id: UUID,
    val version: Long,
    val gosRegNum: String,
    val model: TransportModel,
    val clients: MutableSet<Client> = LinkedHashSet()
) {
    constructor(transport: Transport) : this(transport.id, transport.version, transport.gosRegNum, transport.model, transport.clients)

    fun toTransport() = Transport(this.id, this.version, this.gosRegNum, this.model, this.clients)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransportDto) return false
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