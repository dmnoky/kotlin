package ru.tbank.dao.exception

import java.sql.SQLException

class OptimisticLockException(message: String)  : SQLException(message) {
}