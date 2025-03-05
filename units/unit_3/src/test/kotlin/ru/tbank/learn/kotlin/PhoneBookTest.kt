package ru.tbank.learn.kotlin

import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertContains
import kotlin.test.assertFailsWith

class PhoneBookTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun init() {
            println("Begin")
        }

        @JvmStatic
        fun okSubscriberProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(Subscriber("8231213211", "Subscriber1"), true),
            Arguments.of(Subscriber("8231213212", "Subscriber2"), true),
            Arguments.of(Subscriber("8231213213", "Subscriber3"), true),
            Arguments.of(Subscriber("8231213214", "subscriber4"), true)
        )

        @AfterAll
        @JvmStatic
        fun end() {
            println("End")
        }
    }

    private var testPhoneBook = PhoneBook()

    @BeforeEach
    @DisplayName("Обнуление PhoneBook")
    fun preTest() {
        testPhoneBook = PhoneBook()
    }

    @ParameterizedTest
    @MethodSource("okSubscriberProvider")
    @DisplayName("Проверка метода addSubscriber и getSubscriberList")
    fun addSubscriber_And_getSubscriberList_Ok(argument: Subscriber, excepted: Boolean) {
        testPhoneBook.addSubscriber(argument)
        assertContains(testPhoneBook.getSubscriberList(argument.FIO[0]), argument)
    }

    @Test
    @DisplayName("RuntimeException по уникальности Subscriber")
    fun addSubscriber_uniqueError() {
        val subscriber = Subscriber("8231213211", "Subscriber1")
        testPhoneBook.addSubscriber(subscriber)
        assertFailsWith(RuntimeException::class, "RuntimeException по уникальности Subscriber")
            { testPhoneBook.addSubscriber(subscriber) }
    }

    @Test
    @DisplayName("Проверка метода editSubscriber")
    fun editSubscriber() {
        val subscriber = Subscriber("8231213211", "Subscriber1")
        val subscriberCopy = subscriber.copy(FIO = "SubscriberCopy")
        testPhoneBook.addSubscriber(subscriber)
        //новый subscriber есть
        assertContains(testPhoneBook.getSubscriberList(subscriber.FIO[0]), subscriber, "новый subscriber есть")
        testPhoneBook.editSubscriber(subscriberCopy)
        //нового subscriber уже нет
        assertThrows<NoSuchElementException>("нового subscriber уже нет")
            { testPhoneBook.getSubscriberList(subscriber.FIO[0]).first { it?.FIO == subscriber.FIO } }
        //обновленный subscriber есть
        assertContains(testPhoneBook.getSubscriberList(subscriberCopy.FIO[0]), subscriberCopy, "обновленный subscriber есть")
    }

    @Test
    @DisplayName("RuntimeException при редактировании Subscriber, т.к. его не существует в списке")
    fun editSubscriber_notExist() {
        assertThrows<java.lang.RuntimeException> ("RuntimeException, т.к. Subscriber не существует в списке")
            { testPhoneBook.editSubscriber(Subscriber("8231213211", "Subscriber1")) }
    }

    /*@ParameterizedTest
    @NullSource
    @DisplayName("Проверка на нулпоинтер при добавлении (addSubscriber не ожидает null), но чет junit не вывозит её")
    fun addSubscriber_null(argument: Subscriber) {
        try {
            assertFails { testPhoneBook.addSubscriber(argument) }
            //assertThrows<NullPointerException>("Parameter specified as non-null is null", fun () = testPhoneBook.addSubscriber(argument))
            //assertFailsWith(NullPointerException::class, fun () = testPhoneBook.addSubscriber(argument))
        } catch (e: java.lang.NullPointerException) {
            assert(true)
        }
    }*/
}