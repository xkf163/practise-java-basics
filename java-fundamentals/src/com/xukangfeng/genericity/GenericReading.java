package com.xukangfeng.genericity;

import java.util.Arrays;
import java.util.List;

public class GenericReading {
    static List<Fruit> fruits = Arrays.asList(new Fruit());
    static List<Apple> apples = Arrays.asList(new Apple());
}

class Fruit {}
class Apple extends Fruit {}
class Orange extends Fruit {}
