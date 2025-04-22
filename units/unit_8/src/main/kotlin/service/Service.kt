package ru.tbank.service

import org.hibernate.Session
import javax.persistence.EntityManager

interface Service {
    companion object {
        /** Стартует транзакцию и не завершает сессию */
        inline fun <T : EntityManager, R> T.newTransaction(block: (T) -> R): R {
            unwrap(Session::class.java).isDefaultReadOnly = false
            if (!transaction.isActive) transaction.begin()
            try {
                return block(this).apply {
                    transaction.commit()
                }
            } catch (e: Throwable) {
                transaction.rollback()
                throw e
            }
        }

        /** Переводит сессию в readOnly */
        inline fun <T : EntityManager, R> T.noTransactionRO(block: (T) -> R): R {
            unwrap(Session::class.java).isDefaultReadOnly = true
            try {
                return block(this)
            } catch (e: Throwable) {
                throw e
            }
        }
    }
}
