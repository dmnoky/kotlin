package ru.tbank.model

import java.util.*

data class Client (
    val id: UUID,
    val version: Long,
    val lastName: String,
    val firstName: String,
    val middleName: String?,
    val city: String?, //вроде правильнее вынести это в сущность Address и отдельную таблицу, но в задании не просили
    val transports: MutableSet<Transport> = LinkedHashSet()
) {
    companion object {
        fun buildNewClient(
            lastName: String,
            firstName: String,
            middleName: String?,
            city: String?
        ) = Client(UUID.randomUUID(), 0, lastName, firstName, middleName, city)
    }

    fun getFIO() = "$lastName $firstName $middleName".trim()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Client) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Client(id=$id, version=$version, lastName='$lastName', firstName='$firstName', middleName=$middleName, city=$city)"
    }

}