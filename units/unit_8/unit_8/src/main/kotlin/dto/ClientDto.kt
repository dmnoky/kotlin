package ru.tbank.dto

import ru.tbank.model.Client
import ru.tbank.model.Transport
import java.util.*

data class ClientDto (
    val id: UUID,
    val version: Long,
    val deleteAt: Date?,
    val lastName: String,
    val firstName: String,
    val middleName: String?,
    val city: String?,
    val transports: MutableSet<Transport> = LinkedHashSet()
) {
    constructor(client: Client) : this(client.id, client.version, client.deleteAt, client.lastName, client.firstName, client.middleName, client.city) {
        this.transports.addAll(client.transports)
    }

    fun toClient() = Client(this.id, this.version, this.deleteAt, this.lastName, this.firstName, this.middleName, this.city, this.transports)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClientDto) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}