package com.la0rg.adventofcode;

import com.google.common.collect.Collections2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by la0rg on 02.01.2016.
 */
public class Day13 {
    public static void main(String[] args) {
        try {
            new Day13();
        } catch (FileNotFoundException e) {
            System.err.println("File cannot be found. " + e.getMessage());
        }
    }

    public Day13() throws FileNotFoundException {
        class Rule {
            String person = "";
            int happiness = 0;

            public Rule(String person, int happiness) {
                this.person = person;
                this.happiness = happiness;
            }
        }

        Set<String> people = new HashSet<>();
        Map<String, List<Rule>> rules = new HashMap<>();

        InputStream fileStream = getSystemClassLoader().getResourceAsStream("Day13data.txt");
        if (fileStream == null) {
            throw new FileNotFoundException();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileStream))) {
            // Parse strings and fill set and map
            for (String line; (line = br.readLine()) != null; ) {
                String[] parts = line.split(" ");
                people.add(parts[0]);
                List<Rule> listofrules = rules.get(parts[0]);
                if (listofrules == null) {
                    listofrules = new ArrayList<>();
                    rules.put(parts[0], listofrules);
                }
                int value = Integer.parseInt(parts[3]);
                listofrules.add(new Rule(parts[10].substring(0, parts[10].length() - 1), parts[2].equals("lose") ? value * -1 : value));
            }

            // Add yourself for Part2
            rules.forEach((s, v) -> v.add(new Rule("myself", 0)));
            rules.put("myself", people.stream().map(name -> new Rule(name, 0)).collect(Collectors.toList()));
            people.add("myself");

            // Check all permutations and find optimal seating arrangement (in terms of total change of happiness)
            int best = Integer.MIN_VALUE;
            for (List<String> perm : Collections2.orderedPermutations(people)) {
                List<String> permC = new ArrayList<>(perm);
                permC.add(0, perm.get(perm.size() - 1));//add last to the begin
                permC.add(permC.size(), perm.get(0));//add first to the end
                int total = 0;
                for (int i = 1; i < permC.size() - 1; i++) {
                    final Integer j = i;
                    total += rules.get(permC.get(i)).
                            stream().
                            filter(rule -> rule.person.equals(permC.get(j + 1)) || rule.person.equals(permC.get(j - 1))).
                            reduce(0, (sum, r) -> sum += r.happiness, (sum1, sum2) -> sum1 + sum2);
                }
                if (total > best) {
                    best = total;
                }
            }
            System.out.println("THE BEST IS: " + best);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
