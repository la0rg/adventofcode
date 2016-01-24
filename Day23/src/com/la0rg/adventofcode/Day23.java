package com.la0rg.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by la0rg on 24.01.2016.
 */
// Turing machine
class TM {
    private int position = 0;
    private HashMap<String, Integer> variables = new HashMap<>();
    private List<Instruction> instructions = new ArrayList<>();

    public TM(int a, int b) {
        variables.put("a", a);
        variables.put("b", b);
    }

    public void addInstruction(Instruction i) {
        instructions.add(i);
    }

    /*
        @return true if position is out of instructions list
     */
    public boolean step() {
        Instruction ci = instructions.get(position);
        switch (ci.name) {
            case "hlf":
                apply(ci.variable, v -> v / 2);
                position++;
                break;
            case "tpl":
                apply(ci.variable, v -> v * 3);
                position++;
                break;
            case "inc":
                apply(ci.variable, v -> v + 1);
                position++;
                break;
            case "jmp":
                position += ci.offset;
                break;
            case "jie":
                if (variables.get(ci.variable) % 2 == 0) {
                    position += ci.offset;
                } else {
                    position++;
                }
                break;
            case "jio":
                if (variables.get(ci.variable) == 1) {
                    position += ci.offset;
                } else {
                    position++;
                }
                break;
        }
        return position < 0 || position >= instructions.size();
    }

    private void apply(String v, Function<Integer, Integer> f) {
        variables.put(v, f.apply(variables.get(v)));
    }

    public int getValue(String variableName) {
        return variables.get(variableName);
    }

    static class Instruction {
        String name;
        String variable;
        int offset;

        public Instruction(String name, String variable, int offset) {
            this.name = name;
            this.variable = variable;
            this.offset = offset;
        }
    }
}

public class Day23 {
    public static void main(String[] args) {
        TM tm = new TM(0, 0);
        TM tmp2 = new TM(1, 0);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getSystemClassLoader().getResourceAsStream("day23data.txt")))) {
            // parse instructions and add them to TM
            for (String line; (line = br.readLine()) != null; ) {
                String[] parts = line.split("[ ,]+");
                String v = "";
                int off = 0;
                if (parts[0].equals("jmp")) {
                    off = Integer.parseInt(parts[1]);
                } else {
                    v = parts[1];
                    if (parts.length == 3) {
                        off = Integer.parseInt(parts[2]);
                    }
                }
                tm.addInstruction(new TM.Instruction(parts[0], v, off));
                tmp2.addInstruction(new TM.Instruction(parts[0], v, off));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        while (!tm.step()) ;
        while (!tmp2.step()) ;
        System.out.println("b: " + tm.getValue("b"));
        System.out.println("part2 b: " + tmp2.getValue("b"));
    }
}
