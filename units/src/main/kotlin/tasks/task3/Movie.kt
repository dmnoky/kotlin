package tasks.task3

import java.io.Serializable
import java.util.*
import kotlin.collections.HashSet

data class Movie(
    val name: String,
    val releaseDate: Date,
    val actors: MutableSet<Actor> = HashSet()
) : Serializable {
    companion object {
        private val serialVersionUid: Long = 1
    }

    /*override fun toString(): String {
        return "$name $releaseDate"
    }*/

    override fun equals(other: Any?): Boolean {
        return other != null && other is Movie && name == other.name && releaseDate == other.releaseDate
    }

    /** Проверки на нул, т.к. при сериализации HashSet падает по нулпоинтеру. Дефолт значения не помагают */
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        if (name != null) result = prime * result + name.hashCode()
        if (releaseDate != null) result = prime * result + releaseDate.hashCode()
        return result
    }
}