package task1

/** Работник */
data class Worker(
    val id: Long,
    var fstName: String,
    var sndName: String
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Worker) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Работник #$id, '$sndName' '$fstName'"

}