package org.vitaliyyats;

import java.util.concurrent.atomic.AtomicInteger;

public class Print1to9 {
    static AtomicInteger val = new AtomicInteger(1);

    Runnable r = () -> {
        var name = Thread.currentThread().getName();
        int remainder;
        switch (name) {
            case "t1" -> remainder = 1;
            case "t2" -> remainder = 2;
            case "t3" -> remainder = 0;

            default -> throw new IllegalStateException("Unexpected value: " + name);
        }
        while (val.get() < 10) {
            synchronized (this) {
                while (val.get() % 3 != remainder) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (val.get() < 10) {
                    System.out.println(name + " " + val.getAndIncrement());
                }
                notifyAll();
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        var pr = new Print1to9();
        var t1 = new Thread(pr.r);
        t1.setName("t1");
        t1.start();

        var t2 = new Thread(pr.r);
        t2.setName("t2");
        t2.start();

        var t3 = new Thread(pr.r);
        t3.setName("t3");
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}
