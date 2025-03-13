package tasks.task3

import tasks.ActorService
import tasks.Main
import tasks.MovieService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Reader {
    private val sysIn = Scanner(System.`in`)

    fun readString(message: String = "") : String {
        println(message)
        return sysIn.next()
    }

    fun readInt(message: String = "") : Int {
        println(message)
        return try {
            sysIn.nextInt()
        } catch (e: InputMismatchException) {
            println("Oghidaetsya chislo")
            readInt(message)
        }
    }

    fun readDate(message: String = "", datePattern: String = "yyyy-MM-dd") : Date {
        println(message)
        return try {
            val dateStr = sysIn.next()
            return SimpleDateFormat(datePattern).parse(dateStr)
        } catch (e: ParseException) {
            println("Oghidaetsya data $datePattern")
            readDate(message)
        }
    }

    fun checkSystemMessage(message: String): Boolean {
        when (message) {
            "/exit" -> {
                Main.running = false
                return false
            }
            "/help" -> {
                println("/add_actor\n" +
                        "/add_movie\n" +
                        "/list_actor\n" +
                        "/list_movie\n" +
                        "/link\n" +
                        "/exit")
                return false
            }
            "/add_movie" -> {
                MovieService.add()
                return false
            }
            "/add_actor" -> {
                ActorService.add()
                return false
            }
            "/list_actor" -> {
                Main.movieLibrary.printActors()
                return false
            }
            "/list_movie" -> {
                Main.movieLibrary.printMovies()
                return false
            }
            "/link" -> {
                Main.movieLibrary.linkMovieActor()
                return false
            }
        }
        return true
    }
}