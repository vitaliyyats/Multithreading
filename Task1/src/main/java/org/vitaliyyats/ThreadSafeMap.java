package org.vitaliyyats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeMap<K, V> {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<K, V> map = new HashMap<>();

    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            map.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V get(K key) {
        lock.readLock().lock();
        try {
            return map.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    // TODO ???
    public Collection<V> values() {
        return map.values();
    }
    
}
