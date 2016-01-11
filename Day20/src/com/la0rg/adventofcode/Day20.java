package com.la0rg.adventofcode;

import java.util.function.BiPredicate;

/**
 * Created by la0rg on 11.01.2016.
 */
public class Day20 {
    final int PUZZLE = 36000000;

    public static void main(String[] args) {
        new Day20();
    }

    // solution is based on prediction of order
    // start from a rough scale, then test result with exact scale
    // BTW wrong rough arguments may lead to a wrong solution
    public Day20() {
        // PART - 1
        // roughly steps
        int roughResult = getHouseNumber(700000, 100, 10, (a, b) -> true);
        // exact steps
        int exactResult = getHouseNumber(roughResult - 100, 1, 10, (a, b) -> true);
        System.out.println("RESULT IS: " + exactResult);
        // PART - 2
        roughResult = getHouseNumber(800000, 10, 11, (house, elf) -> house / elf < 50);
        exactResult = getHouseNumber(roughResult - 100, 1, 11, (house, elf) -> house / elf < 50);
        System.out.println("RESULT 2 IS: " + exactResult);
    }

    // algo is pretty slow
    private int getHouseNumber(int startPoint, int step, int coefficient, BiPredicate<Integer, Integer> predicate) {
        int present = 0;
        // go through numbers of house
        for (; present < PUZZLE; startPoint += step) {
            present = 0;
            // find all Elves that visit this house and append their presents
            for (int j = 1; j <= startPoint; j++) {
                if (startPoint % j == 0 && predicate.test(startPoint, j)) {
                    present += j * coefficient;
                }
            }
            System.out.println(startPoint + " -- " + present);
        }
        return startPoint - 1;
    }
}
