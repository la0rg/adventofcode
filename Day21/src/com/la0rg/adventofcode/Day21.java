package com.la0rg.adventofcode;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

/**
 * Created by la0rg on 12.01.2016.
 */

class Player {
    private int hits = 100;
    private int damage = 0;
    private int armor = 0;
    public List<Ammunition> ammunition = new ArrayList<>();

    public Player() {
    }

    public Player(int h, int d, int a) {
        hits = h;
        damage = d;
        armor = a;
    }

    public int getArmor() {
        OptionalInt amArmor = ammunition.stream().mapToInt(a -> a.armor).reduce((a, b) -> a + b);
        return amArmor.isPresent() ? armor + amArmor.getAsInt() : armor;
    }

    public int getDamage() {
        OptionalInt amDamage = ammunition.stream().mapToInt(a -> a.damage).reduce((a, b) -> a + b);
        return amDamage.isPresent() ? damage + amDamage.getAsInt() : damage;
    }

    public boolean isPlayerWin(Player enemy) {
        int myDealDamage = getDamage() - enemy.getArmor() > 0 ? getDamage() - enemy.getArmor() : 1;
        int enemyDealDamage = enemy.getDamage() - getArmor() > 0 ? enemy.getDamage() - getArmor() : 1;
        int myAttacksToWin = enemy.hits % myDealDamage == 0 ? enemy.hits / myDealDamage : enemy.hits / myDealDamage + 1;
        int enemyAttacksToWin = hits % enemyDealDamage == 0 ? hits / enemyDealDamage : hits / enemyDealDamage + 1;
        return myAttacksToWin <= enemyAttacksToWin;
    }
}

class Ammunition {
    public int cost;
    public int damage;
    public int armor;

    public Ammunition(int c, int d, int a) {
        cost = c;
        damage = d;
        armor = a;
    }
}

public class Day21 {
    public static void main(String[] args) {
        Player enemy = new Player(103, 9, 2);
        //Player enemy = new Player(12, 7, 2);
        //Player player = new Player(8, 5, 5);
        Player player = new Player();

        // Shop
        ImmutableSet<Ammunition> weapons = ImmutableSet.of(
                new Ammunition(8, 4, 0),
                new Ammunition(10, 5, 0),
                new Ammunition(25, 6, 0),
                new Ammunition(40, 7, 0),
                new Ammunition(74, 8, 0));
        ImmutableSet<Ammunition> armors = ImmutableSet.of(
                new Ammunition(13, 0, 1),
                new Ammunition(31, 0, 2),
                new Ammunition(53, 0, 3),
                new Ammunition(75, 0, 4),
                new Ammunition(102, 0, 5),
                new Ammunition(0, 0, 0));
        ImmutableSet<Ammunition> rings = ImmutableSet.of(
                new Ammunition(25, 1, 0),
                new Ammunition(50, 2, 0),
                new Ammunition(100, 3, 0),
                new Ammunition(20, 0, 1),
                new Ammunition(40, 0, 2),
                new Ammunition(80, 0, 3),
                new Ammunition(0, 0, 0),
                new Ammunition(0, 0, 0));

        Set<List<Ammunition>> perm = Sets.cartesianProduct(weapons, armors, rings, rings);
        List<Integer> sumsWin = new ArrayList<>();
        List<Integer> sumsLose = new ArrayList<>();
        for (List<Ammunition> list : perm) {
            if (list.get(2) != list.get(3)) { // rings are not the same
                player.ammunition = list;
                int sum = list.stream().mapToInt(a -> a.cost).reduce((a, b) -> a + b).getAsInt();
                if (player.isPlayerWin(enemy)) {
                    sumsWin.add(sum);
                } else {
                    sumsLose.add(sum);
                }
            }
        }
        System.out.println(sumsWin.stream().min((a, b) -> a - b).get().intValue());
        System.out.println(sumsLose.stream().max((a, b) -> a - b).get().intValue());
    }
}
