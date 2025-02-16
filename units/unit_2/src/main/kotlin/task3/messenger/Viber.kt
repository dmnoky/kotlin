package task3.messenger

class Viber : Messenger {
    override fun sendMessage(message: String) {
        println("Отправка сообщения из Viber: \n$message")
    }

    override fun readMessage(): String {
        return "Получено сообщение из Viber"
    }
}