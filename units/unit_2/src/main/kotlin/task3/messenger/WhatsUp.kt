package task3.messenger

class WhatsUp : Messenger {
    override fun sendMessage(message: String) {
        println("Отправка сообщения из WhatsUp: \n$message")
    }

    override fun readMessage(): String {
        return "Получено сообщение из WhatsUp"
    }
}