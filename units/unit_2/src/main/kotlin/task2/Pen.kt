package task2

data class Pen(
    override val name: String,
    override var price: Double
) : Stationery() {
    override fun use() {
        println("Использутеся $name")
    }
}
