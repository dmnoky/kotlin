import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

//1. Функции
object Task1 {
    /** 1 вершок в сантиметрах */
    const val ONE_INCH_SM: Float = 4.445f

    /** Преобразует инпуты в метры: {1 fathom} == {3 arshin} == {48 inch} == {4.445 sm * 48}
     * @param fathom сажень
     * @param arshin аршин
     * @param inch вершок
     * @return сумма инпутов в метрах %.2f */
    fun convertToMeter(fathom: Int = 0, arshin: Int = 0, inch: Int = 0) :Float {
        return floor((fathom * 48 + arshin * 48 / 3 + inch) * ONE_INCH_SM) / 100
    }

    fun start() = println("Task1. Определение длины отрезка в метрах.\n" +
            "${convertToMeter(8, 2, 11) == 18.98f}\n")
}

//2. Ветвление
object Task2 {
    /** Поиск корней уровнения: ax^4 + bx^2 + c = 0 */
    fun sqrtDis(a: Float, b: Float, c: Float) : Pair<Float, Float> {
        val equationFun: Function<Float> //формула поиска корней
        val d: Float //дискриминант

        if (b % 2 != 0f) { //нечетный b
            d = b * b - 4 * a * c
            equationFun = fun(b: Float, d: Float, sign: Int) : Float { return (-b + sqrt(d) * sign) / 2 * a }
        } else { //четный b
            d = (b / 2).pow(2) - a * c
            equationFun = fun(b: Float, d: Float, sign: Int) : Float { return (-b / 2 + sqrt(d) * sign) / a }
        }

        return if (d < 0) throw IllegalArgumentException("Дискриминант < 0! Корней нет.")
        else if (d > 0) { //2 корня
            Pair(equationFun(b, d, 1), equationFun(b, d, -1))
        }
        else { //1 корень
            val result = equationFun(b, d, 1)
            Pair(result, result)
        }
    }
    /** @return минимальный корень уровнения */
    fun sqrtDisMin(a: Float, b: Float, c: Float) : Float {
        val pair :Pair<Float, Float> = sqrtDis(a, b, c)
        return if (pair.first < pair.second) pair.first else pair.second
    }
    /** @return максимальный корень уровнения */
    fun sqrtDisMax(a: Float, b: Float, c: Float) : Float {
        val pair :Pair<Float, Float> = sqrtDis(a, b, c)
        return if (pair.first > pair.second) pair.first else pair.second
    }

    fun start() {
        try {
            println("Task2. Функция поиска наименьшего корня биквадратного уравнения.\nВведите значения a b c с новой строки:")
            println(sqrtDisMin(readLine()!!.toFloat(), readLine()!!.toFloat(), readLine()!!.toFloat())) //println(sqrtDisMin(-1f,6f,7f)) //-1
            println()
        } catch (e: IllegalArgumentException) {
            println(e)
        }
    }
}

//3. Циклы
object Task3 {
    /** Массив совершенных чисел, с лимитом до лонга */
    private val perfectNumbers = arrayOf(6, 28, 496, 8128, 33550336, 8589869056, 137438691328, 2305843008139952128)

    fun numIsPerfect(num: Long) : Boolean {
        for (el in perfectNumbers) {
            if (el == num) return true
        }
        return false
    }

    fun start() {
        println("Task3. Функция проверки числа на совершенность.")
        println(numIsPerfect(2305843008139952128))
    }
}

//4. Массивы
object Task4 {
    private const val ROWS = 3
    private const val COLS = 4
    private var matrix = Array(ROWS) { Array(COLS) {""} }

    fun fillMatrix() {
        for (arr in matrix) {
            for (i in arr.indices) {
                arr[i] = readLine()!!
            }
        }
    }

    fun outMatrix() {
        println("${matrix[0].size}x${matrix.size}")
        matrix.forEach { arr ->
            arr.forEach { el -> print("$el ") }
            println()
        }
    }

    fun transposeMatrix() {
        val newMatrix = Array(COLS) { Array(ROWS) {""} }
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                newMatrix[j][i] = matrix[i][j]
            }
        }
        matrix = newMatrix
    }

    fun start() {
        println("Task4. Матрица 3х4 - траспронировать и вывести на экран - до и после.")
        fillMatrix()
        outMatrix()
        transposeMatrix()
        outMatrix()
    }
}

//5. Простейшие классы и объекты
object Task5 {
    /**
     * Абонент
     * @param phone телефон. Неизменяем. Можно использовать как ключ.
     * @param FIO имя абонента.
     * */
    data class Subscriber(private val phone: String, var FIO: String) {
        fun getPhone(): String {
            return phone
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Subscriber) return false
            return phone == other.phone
        }

        override fun hashCode(): Int {
            return phone.hashCode()
        }
    }
    /**
     * Справочник [book] абонентов [Subscriber]
     * Абоненты в справочнике укикальны по телефону [Subscriber.phone]
     * */
    class PhoneBook() {
        private var book = arrayOfNulls<Subscriber>(0) //я б мапу использовал, но по задаче массив, а не коллекция
        /**
         * Ищет абонента по телефону в текущем массиве
         *
         * @param subscriber искомый абонент
         * @return пара из значений,
         * где fst == индексу в массиве (-1, если не найден),
         * а snd == найденному абоненту по equals ([Subscriber.phone]) (null, если не найден)
         *  */
        private fun hasSubscriber(subscriber: Subscriber) : Pair<Int, Subscriber?> {
            for (i in book.indices) {
                if (book[i] == subscriber) return Pair(i, book[i])
            }
            return Pair(-1, null)
        }

        /**
         * Добавляет абонента в массив [book]
         *
         * @param subscriberNew, добавляемый абонент
         * @throws RuntimeException, если абонент уже в массиве
         * */
        fun addSubscriber(subscriberNew: Subscriber) {
            val indexAndSubscriber: Pair<Int, Subscriber?> = hasSubscriber(subscriberNew)
            if (indexAndSubscriber.second != null) throw RuntimeException("Абобнент уже в списке")
            book += subscriberNew
        }

        /**
         * Редактирует абонента (перезаписывает в массиве найденного абонента на переданного в аргументы)
         *
         * @param subscriberNew, изменяемый абонент
         * @throws RuntimeException, если абонента нет в массиве
         * */
        fun editSubscriber(subscriberNew: Subscriber) {
            val indexAndSubscriber: Pair<Int, Subscriber?> = hasSubscriber(subscriberNew)
            if (indexAndSubscriber.second == null) throw RuntimeException("Абобнент не найден")
            book[indexAndSubscriber.first] = subscriberNew
        }

        /**
         * Возращает всех абонентов с фамилией, начинающейся с указанной буквы
         *
         * @param FIOStart первая буква фамилии (ФИО)
         * @return новый массив, отфильтрованный по ФИО первой буквой
         * */
        fun getSubscriberList(FIOStart: Char) : Array<Subscriber?> {
            var i = 0
            val filterArray = arrayOfNulls<Subscriber>(book.size)
            for (el in book) {
                if (el != null && el.FIO.startsWith(FIOStart, true)) filterArray[i++] = el
            }
            return filterArray.copyOf(i)
        }

    }

    fun start() {
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
}

fun main() {
    Task1.start()
    //Task2.start()
    //Task3.start()
    //Task4.start()
    //Task5.start()
}