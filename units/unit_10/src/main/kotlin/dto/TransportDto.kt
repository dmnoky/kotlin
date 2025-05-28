package ru.tbank.dto

import com.fasterxml.jackson.annotation.JsonProperty
import ru.tbank.model.Client
import ru.tbank.model.Transport
import ru.tbank.model.TransportModel
import java.util.*

data class TransportDto(
    @JsonProperty("id")
    val id: UUID?,
    @JsonProperty("version")
    val version: Long?,
    @JsonProperty("gosRegNum")
    val gosRegNum: String,
    @JsonProperty("model")
    val model: TransportModel,
    @JsonProperty("clients")
    val clients: MutableSet<Client>?
) {
    constructor(transport: Transport) : this(transport.id, transport.version, transport.gosRegNum, transport.model, transport.clients)

    fun toTransport() = Transport(this.id?:UUID.randomUUID(), this.version?:0, this.gosRegNum, this.model, this.clients?:LinkedHashSet())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransportDto) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Transport(id=$id, version=$version, gosRegNum='$gosRegNum', model=$model)"
    }
}