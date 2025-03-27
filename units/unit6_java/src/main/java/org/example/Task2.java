package org.example;

import java.io.IOException;

/**Задание 2. Бенчмарки
 *  * Сравнить производительность двух коллекций: ArrayList и LinkedList. Необходимо сравнить производительность таких операций, как:
 *  * • вставка элемента в конце add(Any)
 *  * •
 *  * вставка элемента в середину add(index, Any)
 *  * • получение элемента по индексу get(index)
 *  * • операция поиска contains(Any)
 *  * • удаление элемента remove(Any)
 *  * • удаление элемента по индексу remove(index)
 *  * Сравнение проводить на 50 000, 500 000 и 1 000 000 элементах. Предполагается использование инструмента Java Microbenchmark Harness (краткое руководство - https://www.baeldung.com/java-microbenchmark-harness)
 *  * Объясните полученные результаты.
 * ********************************************************************************************************************
 * Результаты:
 * Benchmark                                            Mode     Cnt     Score     Error   Units
 * BenchmarkListAdd.addArrayList50_000                  avgt       6     0,798 �   0,058   ms/op
 * BenchmarkListAdd.addLinkedList50_000                 avgt       6     1,034 �   0,318   ms/op
 * BenchmarkListAdd.addMidArrayList50_000               avgt       6    45,028 �   1,633   ms/op
 * BenchmarkListAdd.addMidLinkedList50_000              avgt       6   799,186 �  36,647   ms/op
 * BenchmarkListGet.containsArrayList50_000             avgt       6   934,230 �  64,351   ms/op
 * BenchmarkListGet.containsLinkedList50_000            avgt       6  1969,028 � 118,699   ms/op
 * BenchmarkListGet.getArrayList50_000                  avgt       6     0,009 �   0,001   ms/op
 * BenchmarkListGet.getLinkedList50_000                 avgt       6   693,476 �  26,777   ms/op
 * BenchmarkListGet.removeArrayList50_000               avgt       6   378,738 �  59,870   ms/op
 * BenchmarkListGet.removeByIndexArrayList50_000        avgt       6        10+            ms/op    минимальное 9,304
 * BenchmarkListGet.removeByIndexLinkedList50_000       avgt       6        10+            ms/op    минимальное 73,233
 * BenchmarkListGet.removeLinkedList50_000              avgt       6  1231,257 � 210,074   ms/op
 * ********************************************************************************************************************
 * для 500_000 записей добавление в середину - addMidLinkedList500_000 - идет очень тяжко (по часу на итерацию)
 * ********************************************************************************************************************
 * Вывод:
 * Добавление в конец (add...): ArrayList на малом кол-ве данных не сильно отличается от LinkedList,
 * * но на больших объемах преимущество у LinkedList O(1), т.к. ArrayList будет тратить ресурсы на System.arraycopy O(n);
 * Добавление в середину (addMid...): ArrayList в 17,8 раз быстрее, чем в LinkedList,
 * * т.к. ArrayList обращается на прямую в массив по индексу O(1) ( но дальше System.arraycopy O(n) ),
 * * а в LinkedList приходится пробегать половину связанного списка, т.е. основные траты идут на поиск. Оба O(n);
 * Получение элемента по индексу (get...): в 78000 раз быстрее ArrayList.
 * * Опять же, ArrayList обращается на прямую в массив по индексу O(1), а LinkedList бегает по списку O(n);
 * Операция поиска (contains...): тяжело обоим O(n), но ArrayList в 2 раза быстрее, чем LinkedList -
 * * от поиска не по индексу ArrayList`у плохеет, а LinkedList`у и так плохо;
 * Удаление элемента (remove...): в 3 раза лучше ArrayList. Оба O(n) - опять же ресурсы уходят на поиск,
 * * но если удаление будет с конца или начала, то LinkedList выигрывает, иначе же ArrayList;
 * Удаление элемента по индексу (removeByIndex...): обоим плохо O(n), т.к. удаление не с конца
 * * ArrayList страдает от System.arraycopy, а у LinkedList таже история, что и с поиском
 * Итог: по индексам ArrayList в огромном плюсе, а при работе с началом/концом списка в огромном плюсе LinkedList.
 */
public class Task2
{
    public static void main( String[] args ) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}
