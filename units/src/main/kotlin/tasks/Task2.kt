package tasks

import java.io.File

/**
 * Задание 2. Работа с символьными потоками ввода-вывода
 * Прочитайте файл, содержащий код на языке Kotlin. Определите, какие ключевые слова языка Kotlin этот код содержит.
 * Выведите эти слова и их количество в другой файл. Используйте только символьные потоки ввода-вывода.
 * */
object Task2 {
    fun readFile(fileName: String) {
        File(fileName).reader().buffered().use {
            var readByte = it.read()
            val keyWordBuilder = StringBuilder()
            while (readByte != -1) {
                print(Char(readByte))
                if (INT_CODE_DELIMITER_KEYWORD_CHARACTERS.contains(readByte)) { //конец потенциального кейворда
                    if (keyWordBuilder.isNotEmpty()) {
                        putKeyword(keyWordBuilder.toString())
                        keyWordBuilder.clear() //начало нового потенциального кейворда
                    }
                } else keyWordBuilder.append(Char(readByte))
                readByte = it.read()
            }
        }
    }

    fun writeToFile(fileName: String, content: String) {
        File(fileName).writer().buffered().use {
            it.write(content)
            it.flush()
        }
    }
}

fun main() {
    Task2.readFile("src/main/resources/task2/Task2ReadFile.txt")

    val result = StringBuilder()
    KEYWORDS_KOTLIN_MAP.filter { (_, value) -> value != 0 }.forEach { (key, value) -> result.append(key).append(": ").append(value).append('\n') }

    Task2.writeToFile("src/main/resources/task2/Task2WriteFile.txt", result.toString())
}