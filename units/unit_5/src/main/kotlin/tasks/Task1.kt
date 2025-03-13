package tasks

import java.io.File
import kotlin.text.StringBuilder

/**
 * Задание 1. Работа с байтовыми потоками ввода-вывода
 * Прочитайте файл, содержащий код на языке Kotlin. Определите, какие ключевые слова языка Kotlin этот код содержит.
 * Выведите эти слова и их количество в другой файл. Используйте только байтовые потоки ввода-вывода.
 * */

/** Кейворды котлина, где ключ == кейворд, а велью == их кол-во в файле
 * https://kotlinlang.org/docs/keyword-reference.html#modifier-keywords*/
val KEYWORDS_KOTLIN_MAP = hashMapOf(
    Pair("as", 0), Pair("as?", 0), Pair("break", 0), Pair("class", 0), Pair("continue", 0), Pair("do", 0), Pair("else", 0),
    Pair("false", 0), Pair("for", 0), Pair("fun", 0), Pair("if", 0), Pair("in", 0), Pair("!in", 0), Pair("interface", 0),
    Pair("is", 0), Pair("!is", 0), Pair("null", 0), Pair("object", 0), Pair("package", 0), Pair("return", 0),
    Pair("super", 0), Pair("this", 0), Pair("throw", 0), Pair("true", 0), Pair("try", 0), Pair("typealias", 0),
    Pair("typeof", 0), Pair("val", 0), Pair("var", 0), Pair("when", 0), Pair("while", 0), Pair("by", 0), Pair("catch", 0),
    Pair("constructor", 0), Pair("delegate", 0), Pair("dynamic", 0), Pair("field", 0), Pair("file", 0), Pair("finally", 0),
    Pair("get", 0), Pair("import", 0), Pair("init", 0), Pair("param", 0), Pair("property", 0), Pair("receiver", 0),
    Pair("set", 0), Pair("setparam", 0), Pair("value", 0), Pair("class", 0), Pair("where", 0), Pair("abstract", 0),
    Pair("actual", 0), Pair("annotation", 0), Pair("companion", 0), Pair("const", 0), Pair("crossinline", 0),
    Pair("data", 0), Pair("enum", 0), Pair("expect", 0), Pair("external", 0), Pair("final", 0), Pair("infix", 0),
    Pair("inline", 0), Pair("inner", 0), Pair("internal", 0), Pair("lateinit", 0), Pair("noinline", 0), Pair("open", 0),
    Pair("operator", 0), Pair("out", 0), Pair("override", 0), Pair("private", 0), Pair("protected", 0), Pair("public", 0),
    Pair("reified", 0), Pair("sealed", 0), Pair("suspend", 0), Pair("tailrec", 0), Pair("vararg", 0)
)

/** ascii код возможных завершений кейворда */
val INT_CODE_DELIMITER_KEYWORD_CHARACTERS = intArrayOf(
    9/*Horizontal Tab*/, 10/*Line Feed*/, 11/*Vertical Tabulation*/,
    12/*Form Feed*/, 13/*Carriage Return*/, 32/*Space*/
)

val putKeyword = {
    keyWord: String ->
    val valCount = KEYWORDS_KOTLIN_MAP[keyWord]
    if (valCount != null) KEYWORDS_KOTLIN_MAP[keyWord] = valCount + 1
}

object Task1 {
    fun readFile(fileName: String) {
        File(fileName).inputStream().buffered().use {
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
        File(fileName).outputStream().buffered().use {
            it.write(content.toByteArray())
            it.flush()
        }
    }
}

fun main() {
    Task1.readFile("src/main/resources/task1/Task1ReadFile.txt")

    val result = StringBuilder()
    KEYWORDS_KOTLIN_MAP.filter { (_, value) -> value != 0 }.forEach { (key, value) -> result.append(key).append(": ").append(value).append('\n') }

    Task1.writeToFile("src/main/resources/task1/Task1WriteFile.txt", result.toString())
}