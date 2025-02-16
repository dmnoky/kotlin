package task3

import task3.messenger.Telegram
import task3.messenger.WhatsUp

/**
 * Задание 3. Интерфейсы
 * Разработать программу, которая предоставит возможность клиенту общаться в мессенджере.
 * Создать 3 мессенджера, у которых должны быть обязательно определены методы sendMessage() и readMessage().
 * У клиента может быть только 1 из 3 мессенджеров, но заранее неизвестно, какой именно.
 * */
object Task3 {
    fun start() {
        val client = Client("ОАО Таск", Telegram())
        client.messenger.sendMessage("1 sendMessage")
        client.messenger = WhatsUp()
        client.messenger.sendMessage("2 sendMessage")
    }
}