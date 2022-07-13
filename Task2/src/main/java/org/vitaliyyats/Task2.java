package org.vitaliyyats;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Task2 {
    private final List<Integer> randomList = new ArrayList<>();

    public static void main(String[] args) {
        var task2 = new Task2();
        new Thread(task2.populator()).start();
        new Thread(task2.adder()).start();
        new Thread(task2.squaresAdder()).start();
    }

    private Runnable populator() {
        return () -> {
            var rand = new Random();
            while (true) {
                synchronized (randomList) {
                    randomList.add(Math.abs(rand.nextInt(100)));
                }
            }
        };
    }

    private Runnable adder() {
        return () -> {
            while (true) {
                int sum;
                synchronized (randomList) {
                    sum = randomList.stream().reduce(Integer::sum).orElse(0);
                }
                System.out.println("Sum of " + randomList.size() + " elements of array = " + sum);
                
            }
        };
    }

    private Runnable squaresAdder() {
        return () -> {
            while (true) {
                int sum;
                synchronized (randomList) {
                    sum = randomList.stream()
                            .map(num -> num * num)
                            .reduce(Integer::sum).orElse(0);
                }
                System.out.println("Square root of sum of squares of " + randomList.size() + " elements of array = " + Math.sqrt(sum));
            }
        };
    }

}