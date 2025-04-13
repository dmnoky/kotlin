package ru.tbank

import java.sql.Connection
import java.sql.DriverManager


const val URL = "jdbc:postgresql://localhost:5432/amp?currentSchema=unit7"
const val USER_NAME = "postgres"
const val PASSWORD = "root"

fun getConnection(): Connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)