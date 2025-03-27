package task1

import java.io.FileReader
import java.util.Properties

/**
 * Задание 1. Использование Мар
 * Создать "универсальный класс", позволяющий получить значение из любого properties-файла.
 * Физическое чтение файла должно происходить только один раз.
 * Результаты чтения хранить в коллекции типа Мар.
 * Ответить на вопрос: как ведет себя Мар-коллекция, если в нее добавить элемент с ключом, который уже присутствует?
 *  */
class Task1(propFileName: String) {
    val props: Properties //наследуется от Hashtable, который имплементит Мар
    init {
        FileReader(propFileName).use { props = Properties().apply {
            this.load(it)
        }}
    }

    fun getProperty(key: String): String? = props.getProperty(key)
}

fun main() {
    val task1 = Task1("src/main/resources/mime.properties")
    var prop = task1.getProperty("png")
    println(prop)
    check(prop == "image/png")

    //Ответить на вопрос: как ведет себя Мар-коллекция, если в нее добавить элемент с ключом, который уже присутствует?
    check(task1.getProperty("newKey") == null)

    task1.props["newKey"] = "newValue"

    prop = task1.getProperty("newKey")
    println(prop)
    check(prop == "newValue")

    task1.props["newKey"] = "newValueRepeat"

    prop = task1.getProperty("newKey")
    println(prop)
    check(prop == "newValueRepeat")
    //Ответ: перезапишет велью
}