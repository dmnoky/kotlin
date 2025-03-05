package ru.tbank.learn.kotlin


/**
 * Абонент
 * @param phone телефон. Неизменяем. Можно использовать как ключ.
 * @param FIO имя абонента.
 * */
data class Subscriber(private val phone: String, var FIO: String) {
    fun getPhone(): String {
        return phone
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Subscriber) return false
        return phone == other.phone
    }

    override fun hashCode(): Int {
        return phone.hashCode()
    }
}