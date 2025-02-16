package task2

data class Pencil(override val name: String,
                  override var price: Double
) : Stationery() {
    override fun use() {
        println("Использутеся $name")
    }
}