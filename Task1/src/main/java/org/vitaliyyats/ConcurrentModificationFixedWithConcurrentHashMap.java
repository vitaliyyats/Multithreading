package org.vitaliyyats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.vitaliyyats.ThreadUtil.createAndRunThreads;

public class ConcurrentModificationFixedWithConcurrentHashMap {
    private static final Map<Integer, Integer> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // not throwing ConcurrentModificationException
        createAndRunThreads(map);
    }
}