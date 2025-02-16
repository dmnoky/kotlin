package task3.messenger

class Telegram : Messenger {
    override fun sendMessage(message: String) {
        println("Отправка сообщения из Telegram: \n$message")
    }

    override fun readMessage(): String {
        return "Получено сообщение из Telegram"
    }
}