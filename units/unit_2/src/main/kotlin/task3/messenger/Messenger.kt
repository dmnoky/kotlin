package task3.messenger

interface Messenger {
    fun sendMessage(message: String)
    fun readMessage() : String
}