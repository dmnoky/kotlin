package ru.tbank.learn.kotlin

/**
 * Справочник [book] абонентов [Subscriber]
 * Абоненты в справочнике укикальны по телефону [Subscriber.phone]
 * */
class PhoneBook {
    private var book = arrayOfNulls<Subscriber>(0) //я б мапу использовал, но по задаче массив, а не коллекция
    /**
     * Ищет абонента по телефону в текущем массиве
     *
     * @param subscriber искомый абонент
     * @return пара из значений,
     * где fst == индексу в массиве (-1, если не найден),
     * а snd == найденному абоненту по equals ([Subscriber.phone]) (null, если не найден)
     *  */
    private fun hasSubscriber(subscriber: Subscriber) : Pair<Int, Subscriber?> {
        for (i in book.indices) {
            if (book[i] == subscriber) return Pair(i, book[i])
        }
        return Pair(-1, null)
    }

    /**
     * Добавляет абонента в массив [book]
     *
     * @param subscriberNew, добавляемый абонент
     * @throws RuntimeException, если абонент уже в массиве
     * */
    fun addSubscriber(subscriberNew: Subscriber) {
        val indexAndSubscriber: Pair<Int, Subscriber?> = hasSubscriber(subscriberNew)
        if (indexAndSubscriber.second != null) throw RuntimeException("Абобнент уже в списке")
        book += subscriberNew
    }

    /**
     * Редактирует абонента (перезаписывает в массиве найденного абонента на переданного в аргументы)
     *
     * @param subscriberNew, изменяемый абонент
     * @throws RuntimeException, если абонента нет в массиве
     * */
    fun editSubscriber(subscriberNew: Subscriber) {
        val indexAndSubscriber: Pair<Int, Subscriber?> = hasSubscriber(subscriberNew)
        if (indexAndSubscriber.second == null) throw RuntimeException("Абобнент не найден")
        book[indexAndSubscriber.first] = subscriberNew
    }

    /**
     * Возращает всех абонентов с фамилией, начинающейся с указанной буквы
     *
     * @param FIOStart первая буква фамилии (ФИО)
     * @return новый массив, отфильтрованный по ФИО первой буквой
     * */
    fun getSubscriberList(FIOStart: Char) : Array<Subscriber?> {
        var i = 0
        val filterArray = arrayOfNulls<Subscriber>(book.size)
        for (el in book) {
            if (el != null && el.FIO.startsWith(FIOStart, true)) filterArray[i++] = el
        }
        return filterArray.copyOf(i)
    }

}