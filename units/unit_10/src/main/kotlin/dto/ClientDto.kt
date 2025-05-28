package ru.tbank.dto

import com.fasterxml.jackson.annotation.JsonProperty
import ru.tbank.model.Client
import ru.tbank.model.Transport
import java.util.*

data class ClientDto (
    /** без явных @JsonProperty будет требовать Creator */
    @JsonProperty("id")
    val id: UUID?,
    @JsonProperty("version")
    val version: Long?,
    @JsonProperty("deleteAt")
    val deleteAt: Date?,
    @JsonProperty("lastName")
    val lastName: String,
    @JsonProperty("firstName")
    val firstName: String,
    @JsonProperty("middleName")
    val middleName: String?,
    @JsonProperty("city")
    val city: String?,
    @JsonProperty("transports")
    val transports: MutableSet<Transport>?
) {
    constructor(client: Client) : this(client.id, client.version, client.deleteAt, client.lastName, client.firstName, client.middleName, client.city, client.transports)
    
    fun toClient() = Client(this.id?:UUID.randomUUID(), this.version?:0, this.deleteAt, this.lastName, this.firstName, this.middleName, this.city, transports?:LinkedHashSet())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClientDto) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}