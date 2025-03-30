package task2

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 32, timeUnit = TimeUnit.MILLISECONDS) //прогрев
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS) //замер
@Fork(value = 2, warmups = 1) //повторы Benchmark
@Timeout(time = 30) //30sec
@Threads(4) //потоки
class BenchmarkListGet {
    var arrayList50_000: ArrayList<Int?> = ArrayList(50000)
    var arrayList500_000: ArrayList<Int?> = ArrayList(500000)
    var arrayList1_000_000: ArrayList<Int?> = ArrayList(1000000)
    var linkedList50_000: LinkedList<Int?> = LinkedList()
    var linkedList500_000: LinkedList<Int?> = LinkedList()
    var linkedList1_000_000: LinkedList<Int?> = LinkedList()

    @Setup(Level.Trial)
    fun listSetup() {
        //println("listSetup")
        arrayList50_000 = ArrayList(50000)
        arrayList500_000 = ArrayList(500000)
        arrayList1_000_000 = ArrayList(1000000)
        for (i in 0..<50000) {
            arrayList50_000.add(i)
            arrayList500_000.add(i)
            arrayList1_000_000.add(i)
        }
        for (i in 50000..<500000) {
            arrayList500_000.add(i)
            arrayList1_000_000.add(i)
        }
        for (i in 500000..<1000000) {
            arrayList1_000_000.add(i)
        }
        arrayList50_000.shuffle()
        arrayList500_000.shuffle()
        arrayList1_000_000.shuffle()
        linkedList50_000 = LinkedList(arrayList50_000)
        linkedList500_000 = LinkedList(arrayList500_000)
        linkedList1_000_000 = LinkedList(arrayList1_000_000)
    }

    /** 50_000  */
    @Benchmark
    fun getLinkedList50_000(blackhole: Blackhole) {
        for (i in 0..50000 - 1) {
            blackhole.consume(linkedList50_000[i])
        }
    }

    @Benchmark
    fun getArrayList50_000(blackhole: Blackhole) {
        for (i in 0..50000 - 1) {
            blackhole.consume(arrayList50_000[i])
        }
    }

    //Contains
    @Benchmark
    fun containsArrayList50_000(blackhole: Blackhole) {
        for (i in 0..50000 - 1) {
            blackhole.consume(arrayList50_000.contains(i))
        }
    }

    @Benchmark
    fun containsLinkedList50_000(blackhole: Blackhole) {
        for (i in 0..50000 - 1) {
            blackhole.consume(linkedList50_000.contains(i))
        }
    }

    //Remove
    @Benchmark
    fun removeByIndexArrayList50_000(blackhole: Blackhole) {
        var toLeft = arrayList50_000.size
        while (toLeft > 0) {
            blackhole.consume(arrayList50_000.removeAt(--toLeft))
        }
    }

    @Benchmark
    fun removeArrayList50_000(blackhole: Blackhole) {
        for (i in arrayList50_000.indices) {
            blackhole.consume(arrayList50_000.remove(i))
        }
    }

    @Benchmark
    fun removeByIndexLinkedList50_000(blackhole: Blackhole) {
        var toLeft = linkedList50_000.size
        while (toLeft > 0) {
            blackhole.consume(linkedList50_000.removeAt(--toLeft))
        }
    }

    @Benchmark
    fun removeLinkedList50_000(blackhole: Blackhole) {
        for (i in linkedList50_000.indices) {
            blackhole.consume(linkedList50_000.remove(i))
        }
    }

    /** 500_000  */ //@Benchmark
    fun getLinkedList500_000(blackhole: Blackhole) {
        for (i in 0..500000 - 1) {
            blackhole.consume(linkedList500_000[i])
        }
    }

    //@Benchmark
    fun getArrayList500_000(blackhole: Blackhole) {
        for (i in 0..500000 - 1) {
            blackhole.consume(arrayList500_000[i])
        }
    }

    //Contains
    //@Benchmark
    fun containsArrayList500_000(blackhole: Blackhole) {
        for (i in 0..500000 - 1) {
            blackhole.consume(arrayList500_000.contains(i))
        }
    }

    //@Benchmark
    fun containsLinkedList500_000(blackhole: Blackhole) {
        for (i in 0..500000 - 1) {
            blackhole.consume(linkedList500_000.contains(i))
        }
    }

    //Remove
    //@Benchmark
    fun removeByIndexArrayList500_000(blackhole: Blackhole) {
        var toLeft = arrayList500_000.size
        while (toLeft > 0) {
            blackhole.consume(arrayList500_000.removeAt(--toLeft))
        }
    }

    //@Benchmark
    fun removeArrayList500_000(blackhole: Blackhole) {
        for (i in arrayList500_000.indices) {
            blackhole.consume(arrayList500_000.remove(i))
        }
    }

    //@Benchmark
    fun removeByIndexLinkedList500_000(blackhole: Blackhole) {
        var toLeft = linkedList500_000.size
        while (toLeft > 0) {
            blackhole.consume(linkedList500_000.removeAt(--toLeft))
        }
    }

    //@Benchmark
    fun removeLinkedList500_000(blackhole: Blackhole) {
        for (i in linkedList500_000.indices) {
            blackhole.consume(linkedList500_000.remove(i))
        }
    }

    /** 1_000_000  */ //@Benchmark
    fun getLinkedList1_000_000(blackhole: Blackhole) {
        for (i in 0..1000000 - 1) {
            blackhole.consume(linkedList1_000_000[i])
        }
    }

    //@Benchmark
    fun getArrayList1_000_000(blackhole: Blackhole) {
        for (i in 0..1000000 - 1) {
            blackhole.consume(arrayList1_000_000[i])
        }
    }

    //Contains
    //@Benchmark
    fun containsArrayList1_000_000(blackhole: Blackhole) {
        for (i in 0..1000000 - 1) {
            blackhole.consume(arrayList1_000_000.contains(i))
        }
    }

    //@Benchmark
    fun containsLinkedList1_000_000(blackhole: Blackhole) {
        for (i in 0..1000000 - 1) {
            blackhole.consume(linkedList1_000_000.contains(i))
        }
    }

    //Remove
    //@Benchmark
    fun removeByIndexArrayList1_000_000(blackhole: Blackhole) {
        var toLeft = arrayList1_000_000.size
        while (toLeft > 0) {
            blackhole.consume(arrayList1_000_000.removeAt(--toLeft))
        }
    }

    //@Benchmark
    fun removeArrayList1_000_000(blackhole: Blackhole) {
        for (i in arrayList1_000_000.indices) {
            blackhole.consume(arrayList1_000_000.remove(i))
        }
    }

    //@Benchmark
    fun removeByIndexLinkedList1_000_000(blackhole: Blackhole) {
        var toLeft = linkedList1_000_000.size
        while (toLeft > 0) {
            blackhole.consume(linkedList1_000_000.removeAt(--toLeft))
        }
    }

    //@Benchmark
    fun removeLinkedList1_000_000(blackhole: Blackhole) {
        for (i in linkedList1_000_000.indices) {
            blackhole.consume(linkedList1_000_000.remove(i))
        }
    }
}