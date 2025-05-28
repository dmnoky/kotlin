package ru.tbank.dto.transport

import com.fasterxml.jackson.annotation.JsonProperty
import ru.tbank.model.TransportBrand
import java.util.*

data class TransportBrandDto(
		@JsonProperty("id")
		val id: UUID?,
		@JsonProperty("version")
		val version: Long?,
		@JsonProperty("brand")
		val brand: String
) {
		constructor(transportBrand: TransportBrand) : this(transportBrand.id, transportBrand.version, transportBrand.brand)
		
		fun toEntity() = TransportBrand(this.id?:UUID.randomUUID(), this.version?:0, this.brand)
		
		override fun equals(other: Any?): Boolean {
				if (this === other) return true
				if (other !is TransportBrandDto) return false
				return id == other.id
		}
		
		override fun hashCode(): Int {
				return id.hashCode()
		}
}