package task1

/** Рабочее место
 * @param id уникальный номер места
 * @param worker работник, закрепленный за этим местом */
class WorkSpace(
    val id: Long,
    var worker: Worker
) {
    /** Коллекция канцтоваров, где value - кол-во ключей [Stationery] */
    private val stationeryMap = object : HashMap<Stationery, Int>() {
        override fun toString(): String {
            val i = entries.iterator()
            if (!i.hasNext()) return "{}"
            val sb = StringBuilder()
            sb.append('{')
            while (true) {
                val (key, value) = i.next()
                sb.append('{')
                sb.append(key)
                sb.append(", кол-во: ")
                sb.append(value)
                sb.append('}')
                if (!i.hasNext()) return sb.append('}').toString()
                sb.append(", ")
            }
        }
    }

    /** Добавляет канцтовар [stationery] в [stationeryMap] */
    fun addStationery(stationery: Stationery, count: Int = 1) {
        val value = stationeryMap[stationery]
        if (value == null) stationeryMap[stationery] = count
        else stationeryMap[stationery] = value+count
    }

    /** Забирает канцтовар [stationery] из [stationeryMap] */
    fun takeStationery(stationery: Stationery) : Stationery? {
        var value = stationeryMap[stationery]
        if (value === null) return null
        if (--value <= 0) stationeryMap.remove(stationery)
        else stationeryMap[stationery] = value
        return stationery
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkSpace) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Рабочее место #$id, {$worker}, канцтовары: ${stationeryMap};"

}