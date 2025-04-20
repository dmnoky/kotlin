package ru.tbank.model

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
data class Transport (
    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    val id: UUID,
    @Version
    val version: Long,
    @Column(name = "gos_reg_num")
    val gosRegNum: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="model_id", nullable=false)
    val model: TransportModel,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="client_transport",
        joinColumns=[JoinColumn(name="transport_id", referencedColumnName="id")],
        inverseJoinColumns=[JoinColumn(name="client_id", referencedColumnName="id")]
    )
    @Fetch(FetchMode.SUBSELECT)
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