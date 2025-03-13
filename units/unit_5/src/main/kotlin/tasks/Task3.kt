package tasks

import tasks.task3.Actor
import tasks.task3.Movie
import tasks.task3.MovieLibrary
import tasks.task3.Reader
import java.io.*

/**
 * Задание 3. Сериализация
 * Дана коллекция фильмов, содержащая информацию об актерах, снимавшихся в главных ролях (один актер мог сниматься и в нескольких фильмах).
 * Необходимо написать консольное приложение, позволяющее при запуске восстанавливать коллекцию фильмов, позволять ее модифицировать, а по завершении работы приложения - сохранять в файл.
 * Для восстановления/сохранения коллекции использовать сериализацию/десериализацию.
 * */
object Main {
    const val DAT_FILE_PATH_MOVIE = "src/main/resources/task3/Task3Movie.dat"
    lateinit var movieLibrary: MovieLibrary
    var running = true

    fun save() {
        ObjectOutputStream(File(DAT_FILE_PATH_MOVIE).outputStream().buffered()).use {
            it.writeObject(movieLibrary)
        }
    }

    fun load() : MovieLibrary {
        val file = File(DAT_FILE_PATH_MOVIE)
        if (!file.exists()) return MovieLibrary()
        return ObjectInputStream(file.inputStream().buffered()).use {
            val readObject = it.readObject() as MovieLibrary
            readObject
        }
    }
}

fun main() {
    println("run")
    Main.movieLibrary = Main.load()
    try {
        while (Main.running) {
            Reader.checkSystemMessage(Reader.readString("Type command /help..."))
        }
        println("The end!")
        for (count in 3 downTo 1) {
            println(count)
            try {
                Thread.sleep(1000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    } finally {
        Main.save()
    }
}

object MovieService {
    fun add() {
        val name = Reader.readString("Type name: ")
        val date = Reader.readDate("Type release date: ")
        Main.movieLibrary.movies.add(Movie(name, date))
    }
}

object ActorService {
    fun add() {
        val name = Reader.readString("Type name: ")
        Main.movieLibrary.actors.add(Actor(name))
    }
}