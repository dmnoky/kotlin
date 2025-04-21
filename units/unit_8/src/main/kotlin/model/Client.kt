package ru.tbank.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import org.hibernate.annotations.Where
import java.util.*
import javax.persistence.*

@Entity
@Where(clause = "delete_at is null") //игнор через нетив скл ((entityManager.delegate as Session).createNativeQuery("Select * from Client", Client::class.java)
data class Client (
    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    val id: UUID,
    @Version
    val version: Long,
    //@DeletedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delete_at")
    val deleteAt: Date?,
    @Column(name = "last_name")
    val lastName: String,
    @Column(name = "first_name")
    val firstName: String,
    @Column(name = "middle_name")
    val middleName: String?,
    val city: String?,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="client_transport",
        joinColumns=[JoinColumn(name="client_id", referencedColumnName="id")],
        inverseJoinColumns=[JoinColumn(name="transport_id", referencedColumnName="id")]
    )
    @Fetch(FetchMode.SUBSELECT)
    val transports: MutableSet<Transport> = LinkedHashSet()
) {
    companion object {
        fun buildNewClient(
            lastName: String,
            firstName: String,
            middleName: String?,
            city: String?
        ) = Client(UUID.randomUUID(), 0, null, lastName, firstName, middleName, city)
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