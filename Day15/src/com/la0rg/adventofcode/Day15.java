package com.la0rg.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by la0rg on 05.01.2016.
 */
public class Day15 {
    final static int SUM = 100; //

    public static void main(String[] args) {
        // Because of amount of input data it's easier to hard code it
        // BUT solution is absolutely scalable
        final int[][] ingredients = {
                {3, 0, 0, -3, 2},
                {-3, 3, 0, 0, 9},
                {-1, 0, 4, 0, 1},
                {0, 0, -2, 2, 8},
        };

        List<List<Integer>> permutations = new ArrayList<>();
        perm(new ArrayList<>(), permutations, ingredients.length); // get all permutations of ingredients
        System.out.println("Best score is: " + permutations.stream().mapToInt(p -> getScore(p, ingredients, s -> true)).max().getAsInt());
        System.out.println("Best score for Part 2 is: " + permutations.stream().mapToInt(p -> getScore(p, ingredients, s -> s == 500)).max().getAsInt());
    }

    static int getScore(List<Integer> proportions, int[][] ingredients, Predicate<Integer> restriction) {
        int c = 0, d = 0, f = 0, t = 0, s = 0; // This may be transform to array in case of different number of properties
        for (int i = 0; i < ingredients.length; i++) {
            c += proportions.get(i) * ingredients[i][0];
            d += proportions.get(i) * ingredients[i][1];
            f += proportions.get(i) * ingredients[i][2];
            t += proportions.get(i) * ingredients[i][3];
            s += proportions.get(i) * ingredients[i][4];
        }
        if (c < 0 || d < 0 || f < 0 || t < 0 || !restriction.test(s)) {
            return 0;
        }
        return c * d * f * t;
    }

    // Get all permutations of NUM numbers whose sum is equal to SUM
    // Numbers are >= 0
    //
    // https://youtu.be/NdF1QDTRkck - the video is really helped to figure out implementation of this function
    // (about recursion and backtracking)
    static void perm(List<Integer> added, List<List<Integer>> result, int NUM) {
        Optional<Integer> sum = added.stream().reduce((a, b) -> a + b);
        if (added.size() == NUM) { // base case
            if (sum.isPresent() && sum.get() == SUM) {
                result.add(added);
            }
            return;
        }
        // check only combinations that is may be possible to sum to SUM
        for (int i = 0; i <= (sum.isPresent() ? SUM - sum.get() : SUM); i++) {
            List<Integer> copy = new ArrayList<>(added);
            copy.add(i);
            perm(copy, result, NUM);
        }
    }
}
