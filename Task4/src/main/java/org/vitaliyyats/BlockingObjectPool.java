package org.vitaliyyats;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingObjectPool<T> {
    private final Queue<T> queue;
    private int size;
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    /*** Creates filled pool of passed size * 
     * @param size of pool */
    public BlockingObjectPool(int size) {
        queue = new ArrayDeque<>(size);
        this.size = size;
    }

    /*** Gets object from pool or blocks if pool is empty 
     * @return object from pool */
    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notFull.await();
            }
            T data = queue.remove();
            size--;
            notEmpty.signalAll();
            return data;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    /*** Puts object to pool or blocks if pool is full 
     * @param object to be taken back to pool */
    public void put(T object) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == size) {
                notEmpty.await();
            }
            queue.add(object);
            size++;
            notFull.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
