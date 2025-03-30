package task4

/**
 * Задача 4
 * Василий хочет продать автомобиль. У Василия много друзей и от кого-то когда-то он слышал,
 * что какая-то Наташа хочет купить авто именно его марки. Василий проактивный, хочет побыстрее найти покупателя,
 * однако, Наташа может быть другом одного или нескольких друзей Василия, но может быть и другом Василия через
 * несколько рукопожатий (друг одного друга другого друга ...).
 * Помогите Василию найти всех Наташ и продать машину. Напишите методы поиска по друзьям: поиск в ширину и поиск в глубину,
 * реализовав интерфейс SearchService:
 * * interface SearchService
 * * fun searchForFriendsInWidth (me: User, name: String): List<User>
 * * fun searchForFriendsInDepth (me: User, name: String): List<User>
 *
 * Входные параметры методов:
 * • пользователь, от которого начинаем поиск
 * • имя пользователя, которого хотим найти
 * Методы возвращают список всех пользователей, у которых имя совпадает с запрашиваемым.
 *
 * Класс пользователя:
 * data class User (
 *  val id: Long,
 *  val name: String,
 *  val friends: MutableList<User> = emptyList()
 * )
 * Метод getFriends возвращает список всех друзей пользователя.
 * В этих объектах так же есть списки друзей, и у объектов из этих списков тоже есть списки друзей и т.д.
 * Условимся, что у каждого пользователя есть уникальный идентификатор, но имена у пользователей могут совпадать.
 * Проверить работоспособность алгоритмов тестами.
 */
object Task4

data class User(
    /** уникальный идентификатор */
    val id: Long,
    /** имена у пользователей могут совпадать */
    val name: String,
    private val friends: MutableList<User> = mutableListOf() //emptyList()
) {
    /** @return список всех друзей пользователя */
    fun getFriends() = friends

    override fun equals(other: Any?): Boolean = other != null && other is User && id == other.id

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "{\"id\": \"$id\", \"name\": \"$name\"}"
}

interface SearchService {
    /** Поиск по друзьям в ширину, т.е. пробег только по 1 листу юзера [me]
     * @param me пользователь, от которого начинаем поиск
     * @param name имя пользователя, которого хотим найти
     * @return список всех пользователей, у которых имя совпадает с запрашиваемым */
    fun searchForFriendsInWidth (me: User, name: String): List<User>

    /** Поиск по друзьям в глубину, т.е. пробег по листам друзей юзеров у юзера [me]
     * @param me пользователь, от которого начинаем поиск
     * @param name имя пользователя, которого хотим найти
     * @return список всех пользователей, у которых имя совпадает с запрашиваемым */
    fun searchForFriendsInDepth (me: User, name: String): List<User>
}

class SearchServiceImpl : SearchService {
    private val emptyListRO: List<User> = emptyList()

    override fun searchForFriendsInWidth(me: User, name: String): List<User> {
        return me.getFriends().asSequence().filter { it.name == name }.distinct().toList()
    }

    override fun searchForFriendsInDepth(me: User, name: String): List<User> {
        val alreadyCalledUserSet = HashSet<User>()
        return searchForFriendsInDepth(me, name, alreadyCalledUserSet)
    }

    /** @param alreadyCalledUserSet от бесконечной рекурсии */
    private fun searchForFriendsInDepth(me: User, name: String, alreadyCalledUserSet: HashSet<User>): List<User> {
        return me.getFriends().flatMap {
            if (!alreadyCalledUserSet.contains(it)) {
                alreadyCalledUserSet.add(it)
                return@flatMap searchForFriendsInDepth(it, name, alreadyCalledUserSet) + searchForFriendsInWidth(it, name)
            }
            emptyListRO
        }.distinct().toList()
    }

}