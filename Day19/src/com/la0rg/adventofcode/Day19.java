package com.la0rg.adventofcode;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by la0rg on 10.01.2016.
 */
public class Day19 {
    Map<String, List<String>> replacements = new HashMap<>();
    Set<String> moleculs = new HashSet<>();
    String formula = "";

    public static void main(String[] args) {
        Day19 d = new Day19();
        System.out.println(d.findMolecules().moleculs.size());
        // part 2
        // I really have no idea why it works
        // Starts from formula, finds pattern from replacements and replaces it at rightmost (not necessary for my input) position
        // Do it while formula doesn't shrink to 'e'
        int count2 = 0;
        while (!d.formula.equals("e")) {
            for (Map.Entry<String, List<String>> entry : d.replacements.entrySet()) {
                for (String value : entry.getValue())
                    if (d.formula.contains(value)) {
                        int index = d.formula.indexOf(value);
                        d.formula = new StringBuilder(d.formula).replace(index, index + value.length(), entry.getKey()).toString();
                        count2++;
                    }
            }
        }
        System.out.println(count2);
    }

    // Fill all replacements and formula
    public Day19() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getSystemClassLoader().getResourceAsStream("Day19data.txt")))) {
            // parse file and fill replacements
            for (String line; (line = br.readLine()) != null; ) {
                if (line.contains("=>")) {
                    String[] parts = line.split(" => ");
                    List<String> values = replacements.get(parts[0]);
                    if (values == null) {
                        values = new ArrayList<>();
                    }
                    values.add(parts[1]);
                    replacements.put(parts[0], values);
                    continue;
                }
                formula = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Day19 findMolecules() {
        // find all entry of key in formula and replace every with possible replacements
        for (String key : replacements.keySet()) {
            for (int i = -1; (i = formula.indexOf(key, i + key.length())) != -1; ) {
                for (String value : replacements.get(key)) {
                    moleculs.add(new StringBuilder(formula).replace(i, i + key.length(), value).toString());
                }
            }
        }
        return this;
    }
}