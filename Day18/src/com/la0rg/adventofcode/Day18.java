package com.la0rg.adventofcode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by la0rg on 09.01.2016.
 */
public class Day18 {
    public static void main(String[] args) throws FileNotFoundException {
        boolean[][] lights = new boolean[100][100];

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getSystemClassLoader().getResourceAsStream("Day18data.txt")))) {
            // parse file and fill array of lights
            int i = 0; // position in array
            for (String line; (line = br.readLine()) != null; i++) {
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '#') {
                        lights[i][j] = true;
                    }
                }
            }
            for (int k = 0; k < 100; k++) { // do 100 steps of animation
                lights = animation(lights);
            }
            int numberOfLights = 0;
            for (int k = 0; k < 100; k++) { // check how many lights on the grid are on
                for (int j = 0; j < 100; j++) {
                    if (checkPos(lights, k, j)) numberOfLights++;
                }
            }
            System.out.println("Number: " + numberOfLights);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean[][] animation(boolean[][] lights) {
        boolean[][] result = new boolean[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                // find how many neighbors are on
                int numberOfNeighbors = 0;
                if (checkPos(lights, i, j + 1)) numberOfNeighbors++;
                if (checkPos(lights, i, j - 1)) numberOfNeighbors++;
                if (checkPos(lights, i + 1, j)) numberOfNeighbors++;
                if (checkPos(lights, i - 1, j)) numberOfNeighbors++;
                if (checkPos(lights, i + 1, j + 1)) numberOfNeighbors++;
                if (checkPos(lights, i + 1, j - 1)) numberOfNeighbors++;
                if (checkPos(lights, i - 1, j + 1)) numberOfNeighbors++;
                if (checkPos(lights, i - 1, j - 1)) numberOfNeighbors++;

                // apply rules
                if (checkPos(lights, i, j) && (numberOfNeighbors == 2 || numberOfNeighbors == 3)) {
                    result[i][j] = true;
                }
                if (!checkPos(lights, i, j) && numberOfNeighbors == 3) {
                    result[i][j] = true;
                }
            }
        }
        return result;
    }

    static boolean checkPos(boolean[][] lights, int i, int j) {
        if ((i == 0 || i == 99) && (j == 99 || j == 0)) return true; // for part 2
        return !(i >= 100 || i < 0 || j >= 100 || j < 0) && lights[i][j];
    }
}
