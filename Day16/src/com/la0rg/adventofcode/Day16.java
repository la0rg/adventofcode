package com.la0rg.adventofcode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by la0rg on 07.01.2016.
 */

class Aunt {
    public int ID;
    private Map<String, Integer> props = new HashMap<>();

    // Check if this could be my aunt
    // return false if param is different otherwise true
    public boolean isMyAnt(String param, Integer value) {
        return !(props.get(param) != null && props.get(param).intValue() != value);
    }

    // part 2
    public boolean isMyAnt2(String param, Integer value) {
        if (props.get(param) == null)
            return true;
        if (param.equals("cats") || param.equals("trees")) {
            return props.get(param) > value;
        }
        if (param.equals("pomeranians") || param.equals("goldfish")) {
            return props.get(param) < value;
        }
        return props.get(param).intValue() == value;
    }

    public void parseParams(String str) {
        str = str.replace(":", "").replace(",", "");
        String[] parts = str.split(" ");
        ID = Integer.parseInt(parts[1]);
        // params even - name, odd - value
        for (int i = 2; i < parts.length - 1; i += 2) {
            props.put(parts[i], Integer.parseInt(parts[i + 1]));
        }

    }
}

public class Day16 {
    public static void main(String[] args) throws FileNotFoundException {
        List<Aunt> aunts = new ArrayList<>();
        InputStream fileStream = getSystemClassLoader().getResourceAsStream("Day16data.txt");
        if (fileStream == null) {
            throw new FileNotFoundException();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileStream))) {
            // parse params and create Aunts
            for (String line; (line = br.readLine()) != null; ) {
                Aunt newAunt = new Aunt();
                newAunt.parseParams(line);
                aunts.add(newAunt);
            }

            Map<String, Integer> fromMFCSAM = new HashMap<>();
            fromMFCSAM.put("children", 3);
            fromMFCSAM.put("cats", 7);
            fromMFCSAM.put("samoyeds", 2);
            fromMFCSAM.put("pomeranians", 3);
            fromMFCSAM.put("akitas", 0);
            fromMFCSAM.put("vizslas", 0);
            fromMFCSAM.put("goldfish", 5);
            fromMFCSAM.put("trees", 3);
            fromMFCSAM.put("cars", 2);
            fromMFCSAM.put("perfumes", 1);

            Predicate<Aunt> compositeFilter = a -> true;
            Predicate<Aunt> compositeFilter2 = a -> true;
            for (Map.Entry<String, Integer> aunt : fromMFCSAM.entrySet()) {
                compositeFilter = compositeFilter.and(a -> a.isMyAnt(aunt.getKey(), aunt.getValue()));
                compositeFilter2 = compositeFilter2.and(a -> a.isMyAnt2(aunt.getKey(), aunt.getValue()));
            }
            System.out.println(aunts.stream().filter(compositeFilter).findFirst().get().ID);
            System.out.println(aunts.stream().filter(compositeFilter2).findFirst().get().ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
