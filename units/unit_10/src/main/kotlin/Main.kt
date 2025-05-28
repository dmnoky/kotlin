package ru.tbank

import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

var persistenceUnitName: String = "Unit10"
var entityManagerFactory: EntityManagerFactory = lazy { Persistence.createEntityManagerFactory(persistenceUnitName) }.value
val objectMapper = ObjectMapper().apply {

}
