package com.la0rg.adventofcode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by la0rg on 08.01.2016.
 */
public class Day17 {
    public static void main(String[] args) {
        List<Integer> availableSizes = Arrays.asList(43, 3, 4, 10, 21, 44, 4, 6, 47, 41, 34, 17, 17, 44, 36, 31, 46, 9, 27, 38);
        List<List<Integer>> result = new ArrayList<>();
        perm(availableSizes, 150, Collections.emptyList(), result);
        System.out.println("Result first: " + result.size());
        int min = result.stream().mapToInt(List::size).min().getAsInt();
        System.out.println("Result second: " + result.stream().filter(r -> r.size() == min).collect(Collectors.toList()).size());
    }

    static void perm(List<Integer> available, Integer target, List<Integer> rest, List<List<Integer>> perms) {
        Optional<Integer> sum = rest.stream().reduce((a, b) -> a + b);
        if ((sum.isPresent() && sum.get() >= target) || available.size() == 0) { // base case
            if (sum.isPresent() && sum.get().intValue() == target) {
                perms.add(rest);
            }
            return;
        }
        List<Integer> newRest = new ArrayList<>(rest);
        newRest.add(available.get(0));
        List<Integer> newAvailable = new ArrayList<>(available);
        newAvailable.remove(0);
        perm(newAvailable, target, newRest, perms);
        perm(newAvailable, target, new ArrayList<>(rest), perms);
    }
}
