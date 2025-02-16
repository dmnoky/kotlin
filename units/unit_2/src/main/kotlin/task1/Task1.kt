package task1

import java.io.PrintStream
import kotlin.text.Charsets.UTF_8

/**
 * Задание 1. Классы, объекты, перегрузка
 * Напишите приложение, позволяющее вести учет канцелярских товаров на рабочем месте сотрудника.
 * Определите стоимость канцтоваров у определенного сотрудника. Поле стоимости сделать типом double.
 * Перегрузить метод установки значения для поля стоимости, чтобы была возможность передавать стоимость с типом integer и long.
 * B каждом классе определить методы equals(), hashCode(), toString().
 * */
object Task1 {
    private val stationeryArr = arrayOf(
        Stationery("Степлер", 200.25),
        Stationery("Скрепки", 44.22),
        Stationery("Ручка", 22L),
        Stationery("Карандаш", 11),
        Stationery("Ластик", 5))

    private val workers = arrayOf(
        Worker(1, "Петр", "Петров"),
        Worker(2, "Иван", "Иванов"),
        Worker(3, "Коля", "Николаев")
    )

    private val workSpaces = arrayOf(
        WorkSpace(1, workers[0]),
        WorkSpace(2, workers[1])
    )

    fun start() {
        workSpaces[0].addStationery(stationeryArr[0])
        workSpaces[0].addStationery(stationeryArr[1])
        workSpaces[0].addStationery(stationeryArr[2])
        workSpaces[0].addStationery(stationeryArr[2])
        println(workSpaces[0])
        workSpaces[0].takeStationery(stationeryArr[2])
        println(workSpaces[0])
        workSpaces[0].takeStationery(stationeryArr[1])
        println(workSpaces[0])
    }
}