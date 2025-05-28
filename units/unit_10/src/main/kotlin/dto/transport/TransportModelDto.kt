package ru.tbank.dto.transport

import com.fasterxml.jackson.annotation.JsonProperty
import ru.tbank.model.TransportBrand
import ru.tbank.model.TransportModel
import java.util.*

data class TransportModelDto(
		@JsonProperty("id")
		val id: UUID?,
		@JsonProperty("version")
		val version: Long?,
		@JsonProperty("model")
		val model: String,
		@JsonProperty("brand")
		val brand: TransportBrand
) {
		constructor(transportModel: TransportModel) : this(transportModel.id, transportModel.version, transportModel.model, transportModel.brand)
		
		fun toEntity() = TransportModel(id?:UUID.randomUUID(), version?:0, model, brand)
		
		override fun equals(other: Any?): Boolean {
				if (this === other) return true
				if (other !is TransportModelDto) return false
				return id == other.id
		}
		
		override fun hashCode(): Int {
				return id.hashCode()
		}
}