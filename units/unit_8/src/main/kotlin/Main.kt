package ru.tbank

import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence


const val URL = "jdbc:postgresql://localhost:5432/amp?currentSchema=unit8"
const val USER_NAME = "postgres"
const val PASSWORD = "root"

val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("Unit8")

