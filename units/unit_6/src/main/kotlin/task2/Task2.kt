package task2

/** На котлине вылетает райнтайм ошибка ERROR: Unable to find the resource: /META-INF/BenchmarkList
 * из-за того что во время компиляции плагин котлина не подсосовывает этот файл в мета-инф
 * Не смог в котиловский плагин всторить фикс с jmh-generator-annprocess
 * На джаве же фикс работает:
 * @see [https://github.com/dmnoky/kotlin/tree/main/units/unit6_java]
 * @sample
 * Задание 2. Бенчмарки
 *  * Сравнить производительность двух коллекций: ArrayList и LinkedList. Необходимо сравнить производительность таких операций, как:
 *  * • вставка элемента в конце add(Any)
 *  * •
 *  * вставка элемента в середину add(index, Any)
 *  * • получение элемента по индексу get(index)
 *  * • операция поиска contains(Any)
 *  * удаление элемента remove(Any)
 *  * • удаление элемента по индексу remove(index)
 *  * Сравнение проводить на 50 000, 500 000 и 1 000 000 элементах. Предполагается использование инструмента Java Microbenchmark Harness (краткое руководство - https://www.baeldung.com/java-microbenchmark-harness)
 *  * Объясните полученные результаты.
 * */
object Task2 {
}