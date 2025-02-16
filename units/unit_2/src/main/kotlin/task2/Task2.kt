package task2

/**
 * Задание 2. Наследование
 * Разработайте иерархию канцелярских товаров (достаточно 3 предметов).
 * Создайте "набор новичка", используя созданную иерархию.
 * Сделать так, чтобы невозможно было создать объект базового класса. */
object Task2 {
    /** "Набор новичка" */
    private val beginnerArr: Array<Stationery> = arrayOf(
        Pen("Boss pen", 200.25),
        Pencil("Boss pencil", 44.22),
        Pen("Sub pen", 200.25),
        Pencil("Sub pencil", 44.22),
    )

    fun start() {
        beginnerArr.asSequence().forEach { el -> el.use() }
    }
}