package tasks.task3

import java.io.Serializable

data class Actor(
    val name: String,
    val movies: MutableSet<Movie> = HashSet()
) : Serializable {
    companion object {
        private val serialVersionUid: Long = 1
    }

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is Actor && name == other.name
    }

    /** Проверки на нул, т.к. при сериализации HashSet падает по нулпоинтеру. Дефолт значения не помагают */
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        if (name != null) result = prime * result + name.hashCode()
        return result
    }
}