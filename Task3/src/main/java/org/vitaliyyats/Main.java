package org.vitaliyyats;

import java.util.concurrent.ThreadLocalRandom;

public class Main {
    
    public static void main(String[] args) {
        var broker = new MessageBroker<Integer>();
        
        Runnable producer = () -> {
            while(true) {
                try {
                    broker.produce(ThreadLocalRandom.current().nextInt(1000));
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        Runnable consumer = () -> {
            while (true) {
                try {
                    var data = broker.consume();
                    System.out.println("Consumed data: " + data);
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}