package task2

import org.openjdk.jmh.annotations.*
import java.util.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 32, timeUnit = TimeUnit.MILLISECONDS) //прогрев
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS) //замер
@Fork(value = 2, warmups = 1) //повторы Benchmark
@Timeout(time = 30) //30sec
@Threads(4) //потоки
class BenchmarkListAdd {
    var arrayList: ArrayList<Int> = ArrayList()
    var linkedList: LinkedList<Int> = LinkedList()

    @TearDown(Level.Invocation)
    fun listTearDown() {
        //System.out.println("arrayList: " + arrayList.size());
        arrayList.clear()
        arrayList.trimToSize()
        linkedList.clear()
    }

    private fun addList(list: MutableList<Int>, size: Int): List<Int> {
        for (i in 0..<size) {
            list.add(i)
        }
        //System.out.println("linkedListAdd: "+linkedList.size());
        return list
    }

    private fun addListMid(list: MutableList<Int>, size: Int): List<Int> {
        for (i in 0..<size) {
            list.add(list.size / 2, i)
        }
        //System.out.println("linkedListAdd: "+linkedList.size());
        return list
    }

    /** Adding Items 50_000  */
    @Benchmark //@Group("add50_000")
    fun addLinkedList50_000(): List<Int> {
        return addList(linkedList, 50000)
    }

    @Benchmark //@Group("add50_000")
    fun addArrayList50_000(): List<Int> {
        return addList(arrayList, 50000)
    }

    @Benchmark //@Group("addMid50_000")
    fun addMidLinkedList50_000(): List<Int> {
        return addListMid(linkedList, 50000)
    }

    @Benchmark //@Group("addMid50_000")
    fun addMidArrayList50_000(): List<Int> {
        return addListMid(arrayList, 50000)
    }

    /** Adding Items 500_000  */ //@Benchmark
    fun addLinkedList500_000(): List<Int> {
        return addList(linkedList, 500000)
    }

    //@Benchmark
    fun addArrayList500_000(): List<Int> {
        return addList(arrayList, 500000)
    }

    //@Benchmark
    fun addMidLinkedList500_000(): List<Int> {
        return addListMid(linkedList, 500000)
    }

    //@Benchmark
    fun addMidArrayList500_000(): List<Int> {
        return addListMid(arrayList, 500000)
    }

    /** Adding Items 1_000_000  */ //@Benchmark
    fun addLinkedList1_000_000(): List<Int> {
        return addList(linkedList, 1000000)
    }

    //@Benchmark
    fun addArrayList1_000_000(): List<Int> {
        return addList(arrayList, 1000000)
    }

    //@Benchmark
    fun addMidLinkedList1_000_000(): List<Int> {
        return addListMid(linkedList, 1000000)
    }

    //@Benchmark
    fun addMidArrayList1_000_000(): List<Int> {
        return addListMid(arrayList, 1000000)
    }
}