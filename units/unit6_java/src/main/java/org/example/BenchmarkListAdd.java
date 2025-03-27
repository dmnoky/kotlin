package org.example;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 32, timeUnit = TimeUnit.MILLISECONDS) //прогрев
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS) //замер
@Fork(value = 2, warmups = 1) //повторы Benchmark
@Timeout(time = 30) //30sec
@Threads(4) //потоки
public class BenchmarkListAdd {

    ArrayList<Integer> arrayList = new ArrayList<>();
    LinkedList<Integer> linkedList = new LinkedList<>();

    @TearDown(Level.Invocation)
    public void listTearDown() {
        //System.out.println("arrayList: " + arrayList.size());
        arrayList.clear();
        arrayList.trimToSize();
        linkedList.clear();
    }

    private List<Integer> addList(List<Integer> list, int size) {
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        //System.out.println("linkedListAdd: "+linkedList.size());
        return list;
    }

    private List<Integer> addListMid(List<Integer> list, int size) {
        for (int i = 0; i < size; i++) {
            list.add(list.size()/2, i);
        }
        //System.out.println("linkedListAdd: "+linkedList.size());
        return list;
    }

    /** Adding Items 50_000 */
    @Benchmark
    //@Group("add50_000")
    public List<Integer> addLinkedList50_000() {
        return addList(linkedList, 50_000);
    }

    @Benchmark
    //@Group("add50_000")
    public List<Integer> addArrayList50_000() {
        return addList(arrayList, 50_000);
    }

    @Benchmark
    //@Group("addMid50_000")
    public List<Integer> addMidLinkedList50_000() {
        return addListMid(linkedList, 50_000);
    }

    @Benchmark
    //@Group("addMid50_000")
    public List<Integer> addMidArrayList50_000() {
        return addListMid(arrayList, 50_000);
    }

    /** Adding Items 500_000 */
    //@Benchmark
    public List<Integer> addLinkedList500_000() {
        return addList(linkedList, 500_000);
    }

    //@Benchmark
    public List<Integer> addArrayList500_000() {
        return addList(arrayList, 500_000);
    }

    //@Benchmark
    public List<Integer> addMidLinkedList500_000() {
        return addListMid(linkedList, 500_000);
    }

    //@Benchmark
    public List<Integer> addMidArrayList500_000() {
        return addListMid(arrayList, 500_000);
    }

    /** Adding Items 1_000_000 */
    //@Benchmark
    public List<Integer> addLinkedList1_000_000() {
        return addList(linkedList, 1_000_000);
    }

    //@Benchmark
    public List<Integer> addArrayList1_000_000() {
        return addList(arrayList, 1_000_000);
    }

    //@Benchmark
    public List<Integer> addMidLinkedList1_000_000() {
        return addListMid(linkedList, 1_000_000);
    }

    //@Benchmark
    public List<Integer> addMidArrayList1_000_000() {
        return addListMid(arrayList, 1_000_000);
    }
}