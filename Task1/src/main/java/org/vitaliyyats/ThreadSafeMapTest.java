package org.vitaliyyats;

public class ThreadSafeMapTest {
    public static void main(String[] args) {
        ThreadSafeMap<Integer, Integer> map = new ThreadSafeMap<>();
        Runnable adder = () -> {
            for(int i = 0; i < 1000000; i++) {
                map.put(i,i);
            }
        };

        Runnable summarizer = () -> {
            int sum = 0;
            for(var val: map.values()) {
                sum += val;
            }
            System.out.println("Sum of map elements = " + sum);
        };

        var t1 = new Thread(adder);
        var t2 = new Thread(summarizer);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
