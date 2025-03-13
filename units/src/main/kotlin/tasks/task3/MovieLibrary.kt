package tasks.task3

import java.io.Serializable
import kotlin.NoSuchElementException
import kotlin.collections.HashSet

class MovieLibrary : Serializable {
    companion object {
        private val serialVersionUid: Long = 1
    }

    val movies: MutableSet<Movie> = HashSet()
    val actors: MutableSet<Actor> = HashSet()

    /** Связывает фильм и актера (добавляет актера в фильм, фильм в актера) */
    fun linkMovieActor() {
        var name = Reader.readString("Type movie name: ")
        try {
            val movie = movies.first { it.name == name }
            name = Reader.readString("Type actor name: ")
            val actor = actors.first { it.name == name }
            movie.actors.add(actor)
            actor.movies.add(movie)
        } catch (e: NoSuchElementException) {
            println("Not found $name")
        }
    }

    fun printMovies() {
        movies.forEach { println(it) }
    }

    fun printActors() {
        actors.forEach { println(it) }
    }
}