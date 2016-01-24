package com.la0rg.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by la0rg on 24.01.2016.
 */
// Turing machine
class TM {
    private int position = 0;
    private HashMap<String, Integer> variables = new HashMap<>();
    private List<Instruction> instructions = new ArrayList<>();

    public TM() {
        variables.put("a", 0);
        variables.put("b", 0);
    }

    public void addInstruction(Instruction i) {
        instructions.add(i);
    }

    class Instruction {
        String name;
        String varibale;
        int offset;

        public Instruction(String name, String varibale, int offset) {
            this.name = name;
            this.varibale = varibale;
            this.offset = offset;
        }
    }
}

public class Day23 {
    public static void main(String[] args) {
        TM tm = new TM();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getSystemClassLoader().getResourceAsStream("Day19data.txt")))) {
            // parse file and fill replacements
            for (String line; (line = br.readLine()) != null; ) {
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
