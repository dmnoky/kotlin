package task4

/**
 * Задание 5 (4). Функции высшего порядка и lambda-выражения
 * 1. Написать функциональный тип с методом, который принимает число и возвращает булево значение.
Написать реализацию такого интерфейса в виде лямбда-выражения, которое возвращает true, если переданное число делится без остатка на 13.
 * 2. Написать функциональный тип с приёмником
 * 3. Написать функциональный тип с методом, который принимает три дробных числа а, b, с и возвращает тоже дробное число.
Написать реализацию такого интерфейса в виде лямбда-выражения, которое возвращает Дискриминант (D = b^2 - 4ас).
 * 4. Написать функциональный тип с методом, который принимает 2 числа и возвращает их сумму.
При этом сделать так, чтобы можно было посчитать сумму чисел типа integer + integer, float + float, double + double.
Написать реализации такого интерфейса в виде лямбда-выражений для каждого типа возвращаемого значения. */
object Task4 {
    /** 1. Написать функциональный тип с методом, который принимает число и возвращает true,
     * если переданное число делится без остатка на 13 */
    val withoutRemainderBy13: (reminder13: Int) -> Boolean = { it % 13 == 0 } //fun (reminder13) = reminder13 % 13 == 0

    /** 2. Написать функциональный тип с приёмником */
    val withoutRemainder: Int.(Int) -> Boolean = { this % it == 0 }

    /** 3. Написать функциональный тип с методом, который возвращает Дискриминант (D = b^2 - 4ас) */
    val discriminant = { a: Double, b: Double, c: Double -> b*b - 4*a*c }

    /** 4. Написать функциональный тип с методом, который принимает 2 числа и возвращает их сумму */
    //val sum = { x: Int, y: Int -> x + y }

    //fun sum(x: Int, y: Int) = x + y
    //fun sum(x: Float, y: Float) = x + y

    /*fun interface Summ<T : Number> { fun sum(x: T, y: T) : T }
    val summ = Summ<Float> { x, y -> x + y }*/

    interface Sum {
        fun sum(x: Int, y: Int) : Int
        fun sum(x: Float, y: Float) : Float
        fun sum(x: Double, y: Double) : Double
    }

    val sum = object : Sum {
        override fun sum(x: Int, y: Int) : Int = x + y
        override fun sum(x: Float, y: Float) : Float = x + y
        override fun sum(x: Double, y: Double) : Double = x + y
    }

    fun start() {
        println("1. Написать функциональный тип с методом")
        println(withoutRemainderBy13(13))
        println(withoutRemainderBy13.invoke(26))
        println(withoutRemainderBy13(25))
        println("2. Написать функциональный тип с приёмником")
        println(39.withoutRemainder(13))
        println(27.withoutRemainder(13))
        println("3. Дискриминант")
        println(discriminant(1.1, 21.2, 234.3))
        println("4. Написать функциональный тип с методом, который принимает 2 числа и возвращает их сумму")
        sum.sum(1, 2)
        sum.sum(1.0,1.1)
        sum.sum(1.0f,1.1f)
    }
}