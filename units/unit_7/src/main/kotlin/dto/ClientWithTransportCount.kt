package ru.tbank.dto

import java.util.*

data class ClientWithTransportCount (
    val id: UUID,
    val fio: String,
    val transportCount: Int,
)