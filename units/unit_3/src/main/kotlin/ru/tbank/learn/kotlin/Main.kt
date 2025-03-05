package ru.tbank.learn.kotlin


fun main() {
    println("Task5. Класс Абонент и Телефонная книга.")
    try {
        val phoneBook = PhoneBook()
        phoneBook.addSubscriber(Subscriber("8231213211", "criber1"))
        phoneBook.addSubscriber(Subscriber("8231213212", "criber2"))
        phoneBook.addSubscriber(Subscriber("8231213213", "Subscriber1"))
        phoneBook.addSubscriber(Subscriber("8231213214", "subscriber2"))
        phoneBook.getSubscriberList('s').forEach { println(it) }
        phoneBook.editSubscriber(Subscriber("8231213211", "Subscriber3"))
        phoneBook.getSubscriberList('s').forEach { println(it) }
    } catch (e: RuntimeException) {
        println("Error: ${e.message}")
    }
}
