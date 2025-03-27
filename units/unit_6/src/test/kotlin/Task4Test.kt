
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import task4.SearchServiceImpl
import task4.User
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("User SearchServiceTest")
class Task4Test {
    private val searchService = SearchServiceImpl()

    private var arrayOfClient: Array<User> = Array(1000) {
        User(it.toLong(), "User ${it%20}") //User 0 -> User 19
    }

    @BeforeEach
    fun beforeEach() {
        arrayOfClient.map { it.getFriends().clear() }
    }

    @Test
    @DisplayName("Поиск в ширину. 1 искомый юзер")
    fun searchForFriendsInWidthOneUserTest() {
        val me = arrayOfClient[0]
        val forFind = User(1001L, "ForFindUser")
        me.getFriends().addAll(arrayOfClient.slice(1..111)) //110 неискомых юзеров
        me.getFriends().add(33, forFind.copy())
        //arrayOfClient[0].getFriends().forEach{ println(it) }

        val searchForFriendsInWidth: List<User> = searchService.searchForFriendsInWidth(me, forFind.name)
        assertEquals(searchForFriendsInWidth.size, 1, "В результ-списке 1 юзер?!")
        assertEquals(searchForFriendsInWidth[0], forFind, "В результ-списке ожидаемый юзер?!")
    }

    @Test
    @DisplayName("Поиск в ширину. 7 искомых юзеров")
    fun searchForFriendsInWidthMoreUserTest() {
        val defaultUserListSize = 7
        val me = arrayOfClient[0]
        val forFind = User(1001L, "ForFindUser")
        val meFriendList = me.getFriends()
        meFriendList.addAll(arrayOfClient.slice(1..333)) //332 неискомых юзеров
        val forFindUserList = ArrayList<User>(defaultUserListSize).apply {
            for (i in 1..defaultUserListSize) this.add(forFind.copy(1000L + i))
        }
        meFriendList.addAll(forFindUserList) //искомые юзеры
        meFriendList.shuffle()
        //me.getFriends().forEach{ println(it) }

        val searchForFriendsInWidth: List<User> = searchService.searchForFriendsInWidth(me, forFind.name)
        assertEquals(searchForFriendsInWidth.size, defaultUserListSize, "Размеры списков равны $defaultUserListSize?!")
        assertTrue("Результ-список содержит все искомые элементы?!") { searchForFriendsInWidth.containsAll(forFindUserList) }
    }

    @Test
    @DisplayName("Поиск в ширину. Юзеры не должны повторяться в итоговом листе")
    fun searchForFriendsInWidthNotRepeatTest() {
        val me = arrayOfClient[0]
        val forFind = User(1001L, "ForFindUser")
        me.getFriends().addAll(arrayOfClient.slice(1..111)) //110 неискомых юзеров
        me.getFriends().add(11, forFind.copy())
        me.getFriends().add(22, forFind.copy())
        me.getFriends().add(33, forFind.copy())

        val searchForFriendsInWidth: List<User> = searchService.searchForFriendsInWidth(me, forFind.name)
        assertEquals(searchForFriendsInWidth.size, 1, "В результ-списке 1 юзер?!")
        assertEquals(searchForFriendsInWidth[0], forFind, "В результ-списке ожидаемый юзер?!")
    }

    @Test
    @DisplayName("Поиск в ширину. С несуществующем именем в листе")
    fun searchForFriendsInWidthNotFoundTest() {
        val me = arrayOfClient[0]
        val meFriendList = me.getFriends()
        meFriendList.addAll(arrayOfClient.slice(1..111))
        val searchForFriendsInWidth: List<User> = searchService.searchForFriendsInWidth(me, "USER_NOT_FOUND")
        assertTrue { searchForFriendsInWidth.isEmpty() && meFriendList.isNotEmpty() }
    }

    @Test
    @DisplayName("Поиск в глубину. Юзеры не должны повторяться в итоговом листе")
    fun searchForFriendsInDepth3UserNotRepeatTest() {
        val me = arrayOfClient[0]
        val forFind = User(1001L, "ForFindUser")
        val slice = arrayOfClient.slice(1..111)
        slice.forEach {
            val random = (1..3).random()
            //могут ссылаться сами на себя и повторяться на разной глубине
            it.getFriends().apply {
                this.addAll(arrayOfClient.slice(10*random..150/random))
                this.add(forFind.copy(1002))
                this.add(forFind.copy(1003))
                this.add(forFind.copy(1004))
            }.shuffle()
        }
        me.getFriends().addAll(slice)

        val searchForFriendsInDepth: List<User> = searchService.searchForFriendsInDepth(me, forFind.name)
        assertEquals(searchForFriendsInDepth.size, 3, "В результ-списке 3 юзера?!")
    }

    @Test
    @DisplayName("Поиск в глубину. Юзеры ссылаются друг на друга в списке друзей")
    fun searchForFriendsInfiniteRecursiveTest() {
        val me = arrayOfClient[0]
        val forFind = User(1001L, "ForFindUser")
        me.getFriends().add(forFind)
        forFind.getFriends().add(me)
        val searchForFriendsInDepth: List<User> = searchService.searchForFriendsInDepth(me, forFind.name)
        assertEquals(searchForFriendsInDepth.size, 1, "В результ-списке 1 юзер?!")
        assertEquals(searchForFriendsInDepth[0], forFind, "В результ-списке ожидаемый юзер?!")
    }

}