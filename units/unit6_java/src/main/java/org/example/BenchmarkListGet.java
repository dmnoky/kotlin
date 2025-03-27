package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 32, timeUnit = TimeUnit.MILLISECONDS) //прогрев
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS) //замер
@Fork(value = 2, warmups = 1) //повторы Benchmark
@Timeout(time = 30) //30sec
@Threads(4) //потоки
public class BenchmarkListGet {
    ArrayList<Integer> arrayList50_000 = new ArrayList<>(50_000);
    ArrayList<Integer> arrayList500_000 = new ArrayList<>(500_000);
    ArrayList<Integer> arrayList1_000_000 = new ArrayList<>(1_000_000);
    LinkedList<Integer> linkedList50_000 = new LinkedList<>();
    LinkedList<Integer> linkedList500_000 = new LinkedList<>();
    LinkedList<Integer> linkedList1_000_000 = new LinkedList<>();

    @Setup(Level.Trial)
    public void listSetup() {
        System.out.println("listSetup");
        arrayList50_000 = new ArrayList<>(50_000);
        arrayList500_000 = new ArrayList<>(500_000);
        arrayList1_000_000 = new ArrayList<>(1_000_000);
        for (int i = 0; i < 50_000; i++) {
            arrayList50_000.add(i);
            arrayList500_000.add(i);
            arrayList1_000_000.add(i);
        }
        for (int i = 50_000; i < 500_000; i++) {
            arrayList500_000.add(i);
            arrayList1_000_000.add(i);
        }
        for (int i = 500_000; i < 1_000_000; i++) {
            arrayList1_000_000.add(i);
        }
        Collections.shuffle(arrayList50_000);
        Collections.shuffle(arrayList500_000);
        Collections.shuffle(arrayList1_000_000);
        linkedList50_000 = new LinkedList<>(arrayList50_000);
        linkedList500_000 = new LinkedList<>(arrayList500_000);
        linkedList1_000_000 = new LinkedList<>(arrayList1_000_000);
    }

    /*@State(Scope.Thread)
    public static class FillLinkedList {
        LinkedList<Integer> linkedList = new LinkedList<>();

        @Setup(Level.Trial)
        public void linkedListSetup() {
            System.out.println("linkedListSetup");
            linkedList = new LinkedList<>();
            for (int i = 0; i < 256; i++) {
                linkedList.add(i);
            }
        }

        @TearDown(Level.Invocation)
        public void linkedListTearDown() {
            System.out.println("linkedListTearDown"+linkedList.size());
            linkedList.clear();
        }
    }*/

    /** 50_000 */
    @Benchmark
    public void getLinkedList50_000(Blackhole blackhole) {
        for (int i = 0; i < 50_000; i++) {
            blackhole.consume(linkedList50_000.get(i));
        }
    }

    @Benchmark
    public void getArrayList50_000(Blackhole blackhole) {
        for (int i = 0; i < 50_000; i++) {
            blackhole.consume(arrayList50_000.get(i));
        }
    }

    //Contains
    @Benchmark
    public void containsArrayList50_000(Blackhole blackhole) {
        for (int i = 0; i < 50_000; i++) {
            blackhole.consume(arrayList50_000.contains(i));
        }
    }

    @Benchmark
    public void containsLinkedList50_000(Blackhole blackhole) {
        for (int i = 0; i < 50_000; i++) {
            blackhole.consume(linkedList50_000.contains(i));
        }
    }

    //Remove
    @Benchmark
    public void removeByIndexArrayList50_000(Blackhole blackhole) {
        for (int i = 0; i < arrayList50_000.size(); i++) {
            blackhole.consume(arrayList50_000.remove(i));
        }
    }

    @Benchmark
    public void removeArrayList50_000(Blackhole blackhole) {
        for (Integer i = 0; i < arrayList50_000.size(); i++) {
            blackhole.consume(arrayList50_000.remove(i));
        }
    }

    @Benchmark
    public void removeByIndexLinkedList50_000(Blackhole blackhole) {
        for (int i = 0; i < linkedList50_000.size(); i++) {
            blackhole.consume(linkedList50_000.remove(i));
        }
    }

    @Benchmark
    public void removeLinkedList50_000(Blackhole blackhole) {
        for (Integer i = 0; i < linkedList50_000.size(); i++) {
            blackhole.consume(linkedList50_000.remove(i));
        }
    }

    /** 500_000 */
    //@Benchmark
    public void getLinkedList500_000(Blackhole blackhole) {
        for (int i = 0; i < 500_000; i++) {
            blackhole.consume(linkedList500_000.get(i));
        }
    }

    //@Benchmark
    public void getArrayList500_000(Blackhole blackhole) {
        for (int i = 0; i < 500_000; i++) {
            blackhole.consume(arrayList500_000.get(i));
        }
    }

    //Contains
    //@Benchmark
    public void containsArrayList500_000(Blackhole blackhole) {
        for (int i = 0; i < 500_000; i++) {
            blackhole.consume(arrayList500_000.contains(i));
        }
    }

    //@Benchmark
    public void containsLinkedList500_000(Blackhole blackhole) {
        for (int i = 0; i < 500_000; i++) {
            blackhole.consume(linkedList500_000.contains(i));
        }
    }

    //Remove
    //@Benchmark
    public void removeByIndexArrayList500_000(Blackhole blackhole) {
        for (int i = 0; i < arrayList500_000.size(); i++) {
            blackhole.consume(arrayList500_000.remove(i));
        }
    }

    //@Benchmark
    public void removeArrayList500_000(Blackhole blackhole) {
        for (Integer i = 0; i < arrayList500_000.size(); i++) {
            blackhole.consume(arrayList500_000.remove(i));
        }
    }

    //@Benchmark
    public void removeByIndexLinkedList500_000(Blackhole blackhole) {
        for (int i = 0; i < linkedList500_000.size(); i++) {
            blackhole.consume(linkedList500_000.remove(i));
        }
    }

    //@Benchmark
    public void removeLinkedList500_000(Blackhole blackhole) {
        for (Integer i = 0; i < linkedList500_000.size(); i++) {
            blackhole.consume(linkedList500_000.remove(i));
        }
    }

    /** 1_000_000 */
    //@Benchmark
    public void getLinkedList1_000_000(Blackhole blackhole) {
        for (int i = 0; i < 1_000_000; i++) {
            blackhole.consume(linkedList1_000_000.get(i));
        }
    }

    //@Benchmark
    public void getArrayList1_000_000(Blackhole blackhole) {
        for (int i = 0; i < 1_000_000; i++) {
            blackhole.consume(arrayList1_000_000.get(i));
        }
    }

    //Contains
    //@Benchmark
    public void containsArrayList1_000_000(Blackhole blackhole) {
        for (int i = 0; i < 1_000_000; i++) {
            blackhole.consume(arrayList1_000_000.contains(i));
        }
    }

    //@Benchmark
    public void containsLinkedList1_000_000(Blackhole blackhole) {
        for (int i = 0; i < 1_000_000; i++) {
            blackhole.consume(linkedList1_000_000.contains(i));
        }
    }

    //Remove
    //@Benchmark
    public void removeByIndexArrayList1_000_000(Blackhole blackhole) {
        for (int i = 0; i < arrayList1_000_000.size(); i++) {
            blackhole.consume(arrayList1_000_000.remove(i));
        }
    }

    //@Benchmark
    public void removeArrayList1_000_000(Blackhole blackhole) {
        for (Integer i = 0; i < arrayList1_000_000.size(); i++) {
            blackhole.consume(arrayList1_000_000.remove(i));
        }
    }

    //@Benchmark
    public void removeByIndexLinkedList1_000_000(Blackhole blackhole) {
        for (int i = 0; i < linkedList1_000_000.size(); i++) {
            blackhole.consume(linkedList1_000_000.remove(i));
        }
    }

    //@Benchmark
    public void removeLinkedList1_000_000(Blackhole blackhole) {
        for (Integer i = 0; i < linkedList1_000_000.size(); i++) {
            blackhole.consume(linkedList1_000_000.remove(i));
        }
    }

}