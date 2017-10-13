package com.xukangfeng.genericity;

import java.util.Arrays;
import java.util.List;

public class GenericReading {
    static List<Fruit> fruits = Arrays.asList(new Fruit());
    static List<Apple> apples = Arrays.asList(new Apple());

    static class Reader<T>{
        T readExact(List<T> list){
            return list.get(0);
        }
    }

    static void f1() {
        Reader<Fruit> fruitReader = new Reader<Fruit>();
        // Errors: List<Fruit> cannot be applied to List<Apple>.
//         Fruit f = fruitReader.readExact(apples);
    }
//    public static void main(String[] args) {
//        f1();
//    }




    static class CovariantReader<T> {
        T readCovariant(List<? extends T> list) {
            return list.get(0);
        }
    }
    static void f2() {
        CovariantReader<Fruit> fruitReader = new CovariantReader<Fruit>();
        Fruit f = fruitReader.readCovariant(fruits);
        Fruit a = fruitReader.readCovariant(apples);
    }
    public static void main(String[] args) {
        f2();
    }

}

class Fruit {}
class Apple extends Fruit {}
class Orange extends Fruit {}
