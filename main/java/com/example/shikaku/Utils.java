package com.example.shikaku;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static <T> List<T> addToListElementSafe(List<T> list, T el) {
        List<T> newList = new ArrayList<>(List.copyOf(list));
        newList.add(el);
        return newList;
    }

    public static <T> List<T> addToList(List<T> list, List<T> objects) {
        List<T> newList = new ArrayList<>(list);
        newList.addAll(objects);
        return newList;
    }

    public static List<Integer> createFilledList(Supplier<Integer> supplier, int n) {
        return Stream.generate(supplier)
                .limit(n*n)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Function<Integer, Supplier<Integer>> setNum = tag -> () -> tag;


//    public static <T> List<T> partlyCopyList(List<T> list, List<T> ind) {
//        List<T> newList = new ArrayList<>(list);
//        for (int i = 0; i < ind; i++) {
//            str2.add(currRec.str.get(i));
//        }
//        return newList;
//    }

    public static <T> List<T> removeFromListSafe(List<T> list, int index) {
        List<T> newList = new ArrayList<>(List.copyOf(list));
        newList.remove(index);
        return newList;
    }
}

@FunctionalInterface
interface Function3Arity<A, B, C, R>  {
    R apply(A a, B b, C c);
}
