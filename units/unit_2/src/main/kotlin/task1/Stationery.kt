package task1

/** Канцтовар */
data class Stationery(
    val name: String,
    private var price: Double
) {
    constructor(name: String, price: Int) : this(name, price.toDouble())
    constructor(name: String, price: Long) : this(name, price.toDouble())

    fun getPrice() : Double = price

    fun setPrice(price: Double) {
        this.price = price
    }

    fun setPrice(price: Int) = setPrice(price.toDouble())

    fun setPrice(price: Long) = setPrice(price.toDouble())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Stationery) return false
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "Канцтовар '$name', цена = $price"

}