package task3

import java.util.*
import kotlin.random.Random

/***
 * Задание 3. Stream API
 *  * Необходимо создать класс клиента со следующими полями: уникальный идентификатор, имя, возраст. Также у клиента есть список телефонов. Класс телефона содержит само значение и тип (стационарный или мобильный).
 *  Создать массив клиентов и выполнить следующие задания:
 *  * 1. Рассчитать суммарный возраст для определенного имени
 *  * 2. Получить Set, который содержит в себе только имена клиентов в порядке их упоминания в исходном массиве.
 *  * 3. Узнать, содержит ли список хотя бы одного клиента, у которого возраст больше заданного числа.
 *  * 4. Преобразовать массив в Мар, у которой ключ уникальный идентификатор, значение имя. Поддержать порядок, в котором клиенты Добавлены в массив.
 *  * 5. Преобразовать массив в Мар, у которой ключ возраст, значение коллекция клиентов с таким возрастом.
 *  * 6. Получить строку, содержащую телефоны всех клиентов через запятую. Предусмотреть, что у клиента телефонов может и не быть.
 *  * 7. Найти самого возрастного клиента, которой пользуется стационарным телефоном.
 */
object Task3 {
    /** массив клиентов */
    val arrayOfClient: Array<Client> = Array(100) {
        Client(it.toLong(), "Client ${Random.nextInt(1, 5)}", Random.nextInt(11, 99)).apply {
            if (it%3 == 0) this.phoneList.add(Phone("+734348866"+Random.nextInt(11, 99), PhoneType.stationary))
            if (it%2 == 0) this.phoneList.add(Phone("+799988866"+Random.nextInt(11, 99)))
        }
    }

    /** 1. Рассчитать суммарный возраст для определенного имени. */
    fun getClientAgeSum(name: String) =
        arrayOfClient.filter { it.name == name }.sumOf { it.age }

    /** 2. Получить Set, который содержит в себе только имена клиентов в порядке их упоминания в исходном массиве. */
    fun getLinkedSetOfClientNames() =
        arrayOfClient.map { it.name }.toCollection(LinkedHashSet<String>())


    /** 3. Узнать, содержит ли список хотя бы одного клиента, у которого возраст больше заданного числа. */
    fun containsClientAgeMoreThan(age: Int) :Boolean =
        arrayOfClient.find { it.age > age } != null

    /** 4. Преобразовать массив в Мар, у которой ключ уникальный идентификатор, значение имя. Поддержать порядок, в котором клиенты Добавлены в массив. */
    fun convertToMapIdWithName() : Map<Long, String> =
        arrayOfClient.associateBy(
            keySelector = { it.id },
            valueTransform = { it.name }
        )

    /** 5. Преобразовать массив в Мар, у которой ключ возраст, значение коллекция клиентов с таким возрастом. */
    fun convertToMapAgeWithClients() : Map<Int, List<Client>> =
        arrayOfClient.groupBy(
            keySelector = { it.age }
        )

    /** 6. Получить строку, содержащую телефоны всех клиентов через запятую. Предусмотреть, что у клиента телефонов может и не быть. */
    fun getAllPhonesAsString() =
        arrayOfClient.filter { it.phoneList.size > 0 }.joinToString { it -> it.phoneList.joinToString { "${it.type} ${it.phoneAddress}" } }

    /** 7. Найти самого возрастного клиента, которой пользуется стационарным телефоном. */
    fun getOldestClientWithStationaryPhone() : Client =
        arrayOfClient.maxWith(
            object : Comparator<Client> {
                override fun compare(o1: Client, o2: Client): Int {
                    if (o2.phoneList.find { it.type == PhoneType.stationary } == null) return 1
                    if (o1.age == o2.age) return 0
                    return if (o1.age > o2.age) 1 else -1
                }
            }
        )
}

enum class PhoneType(val displayName: String) {
    stationary("стационарный"),
    mobile("мобильный");

    override fun toString(): String = displayName
}

data class Phone(
    val phoneAddress: String,
    val type: PhoneType = PhoneType.mobile
)

data class Client(
    val id: Long,
    val name: String,
    val age: Int,
    val phoneList: MutableList<Phone> = LinkedList()
) {

    override fun equals(other: Any?): Boolean = other != null && other is Client && id == other.id

    override fun hashCode(): Int = id.hashCode()
}

fun main() {
    Task3.arrayOfClient.iterator().forEach { println(it) }

    val client = Task3.arrayOfClient[0]
    println("getClientAgeSum by $client = " + Task3.getClientAgeSum(client.name))
    println("getLinkedSetOfClientNames")
    Task3.getLinkedSetOfClientNames().forEach { println(it) }
    println("containsClientAgeMoreThan")
    println(Task3.containsClientAgeMoreThan(100))
    println(Task3.containsClientAgeMoreThan(88))
    println("convertToMapIdWithName")
    Task3.convertToMapIdWithName().forEach{ println("${it.key}: ${it.value}") }
    println("convertToMapAgeWithClients")
    Task3.convertToMapAgeWithClients().forEach{ println("${it.key}: ${it.value}") }
    println("getAllPhonesAsString")
    println(Task3.getAllPhonesAsString())
    println("getOldestClientWithStationaryPhone")
    println(Task3.getOldestClientWithStationaryPhone())
}