package org.vitaliyyats;

import java.util.*;

public class MessageBroker<T> {
    private static final int MAX_ELEMENTS = 10;
    private final Queue<T> queue = new ArrayDeque<>(MAX_ELEMENTS) {
    };

    public synchronized void produce(T data) throws InterruptedException {
        while (queue.size() == MAX_ELEMENTS) {
            System.out.println("Queue is full. Waiting for consumer");
            wait();
        }
        queue.add(data);
        notifyAll();
    }
    
    public synchronized T consume() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("Queue is empty. Waiting for producer");
            wait();
        }
        T data = queue.remove();
        notifyAll();
        return data;
    }
}
