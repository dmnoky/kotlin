package ru.tbank

fun main(args: Array<String>) {
    val manyLega = ManyLega()
    manyLega.move()
    manyLega.move()
    manyLega.move()
}

data class ManyLega(
    val legsNum: Int = 40
) {
    private val lock = this as Object
    private var lastLegIndex: Int = legsNum

    init {
        var i = 1
        while (i <= legsNum) {
            Leg(i++, LegType.LEFT).start()
            Leg(i++, LegType.RIGHT).start()
        }
    }

    fun move() {
        synchronized(lock) {
            while (lastLegIndex != legsNum) {
                lock.wait() // ждем последнюю ногу
            }
            println("$this moving")
            lastLegIndex = 0
            lock.notifyAll()
        }
    }

    inner class Leg(
        val index: Int,
        val legType: LegType
    ) : Thread() {

        override fun run() {
            while (true) step()
        }

        fun step() {
            synchronized(lock) {
                try {
                    val waitingLegNum = index - 1
                    while (lastLegIndex != waitingLegNum) {
                        lock.wait() // ждем предыдущую ногу
                    }
                    println(this)
                } catch (e: Exception) {
                    println("$this сломалась\n${e.message}")
                    throw e
                } finally {
                    lastLegIndex = index
                    lock.notifyAll()
                }
            }
        }

        override fun toString(): String =
            "Noga #$index: ${legType.text}"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Leg) return false

            return index == other.index
        }

        override fun hashCode(): Int = index
    }

    enum class LegType(val text: String) {
        LEFT("Left step"),
        RIGHT("Right step")
    }

}
