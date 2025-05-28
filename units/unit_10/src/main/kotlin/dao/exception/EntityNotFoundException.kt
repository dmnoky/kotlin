package ru.tbank.dao.exception

import ru.tbank.exception.NotFoundException

class EntityNotFoundException(message: String, cause: Throwable? = null) : NotFoundException(message, cause) {
}