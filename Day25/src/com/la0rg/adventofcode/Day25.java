package com.la0rg.adventofcode;

/**
 * Created by la0rg on 29.01.2016.
 */
public class Day25 {
    public static void main(String[] args) {
        int i, j; // indexes j - row, i - column
        long value = 20151125;
        Main:
        for (int n = 1; ; n++) { // number of elements in diagonal
            for (i = 0, j = n - 1; i < n; i++, j--) { // go through diagonal
                if (j == 3009 && i == 3018) {
                    System.out.println("Value: " + value);
                    break Main;
                }
                value = (value * 252533) % 33554393;
            }
        }
    }
}
