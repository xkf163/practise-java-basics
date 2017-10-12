package com.xukangfeng.genericity;

/**
 * 泛型Demo
 */
public class GenericityDemo1 {

    public static <T extends Comparable<T>> int countGreaterThan(T[] anArray, T elem) {
        int count = 0;
        for (T e : anArray)
            if (e.compareTo(elem) > 0)  // compiler error
                ++count;
        return count;
    }
}

class Box {
    private String object;
    public void set(String object) { this.object = object; }
    public String get() { return object; }
}

