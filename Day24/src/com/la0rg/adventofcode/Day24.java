package com.la0rg.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by la0rg on 26.01.2016.
 * <p>
 * Solution doesn't check all groups.
 * Found a group that summed to shoudBe, the rest of the packages
 * had to be dividable into two(three) more groups that sum to shoudBe each
 */
public class Day24 {
    private static List<Integer> input = Arrays.asList(1, 2, 3, 7, 11, 13, 17, 19, 23, 31, 37, 41, 43, 47, 53, 59,
            61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113);

    public static void main(String[] args) {
        final int shouldBe = input.stream().mapToInt(Integer::intValue).sum() / 4;
        List<List<Integer>> list = Collections.emptyList();
        for (int i = 1; i < input.size(); i++) {
            list = permutations(input, i).stream()
                    .filter(p -> p.stream().mapToInt(Integer::intValue).sum() == shouldBe)
                    .collect(Collectors.toList()); // all perms that sum is equals to shouldBe
            if (list.size() > 0) {
                break;
            }
        }
        long min = list.stream()
                .mapToLong(p -> p.stream()
                        .mapToLong(Integer::longValue)
                        .reduce((a, b) -> a * b).getAsLong())
                .min().getAsLong();
        System.out.println("MIN: " + min);
    }

    static List<List<Integer>> permutations(List<Integer> input, int n) {
        List<List<Integer>> perms = new ArrayList<>();
        perm(input, new ArrayList<>(), perms, n);
        return perms;
    }

    // permutation without repetition
    // based on positions, not on values
    static void perm(List<Integer> alph, List<Integer> rest, List<List<Integer>> perms, int n) {
        if (rest.size() == n) {
            perms.add(rest);
            return;
        }
        if (alph.size() == 0) return;
        int a = alph.get(0);
        List<Integer> copyRest = new ArrayList<>(rest);
        copyRest.add(a);
        List<Integer> copyAlph = new ArrayList<>(alph);
        copyAlph.remove(0);
        perm(copyAlph, copyRest, perms, n);
        perm(copyAlph, rest, perms, n);
    }
}
