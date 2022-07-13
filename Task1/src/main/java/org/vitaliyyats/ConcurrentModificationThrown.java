package org.vitaliyyats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.vitaliyyats.ThreadUtil.createAndRunThreads;

public class ConcurrentModificationThrown {
    private static final Map<Integer, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        // throwing ConcurrentModificationException
        createAndRunThreads(map);

        // also throwing, but why? )
        createAndRunThreads(Collections.synchronizedMap(map));
    }
}