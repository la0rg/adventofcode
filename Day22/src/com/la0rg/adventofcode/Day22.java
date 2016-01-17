package com.la0rg.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by la0rg on 13.01.2016.
 */

class Player {
    public int hit;
    public int armor;
    public int damage;
    public int mana;
    public List<Spell> activeEffects = new ArrayList<>();

    public Player(int hit, int armor, int mana, int damage) {
        this.hit = hit;
        this.armor = armor;
        this.mana = mana;
        this.damage = damage;
    }

    public Player(Player object) {
        this.hit = object.hit;
        this.armor = object.armor;
        this.damage = object.damage;
        this.mana = object.mana;
        activeEffects = new ArrayList<>();
        activeEffects.addAll(object.activeEffects.stream().map(Spell::new).collect(Collectors.toList()));
    }

    public void castSpell(Spell spell, Player enemy) throws Exception {
        if (mana < spell.costMana) {
            throw new Exception("Can not cast a spell. Player doesn't have enough mana.");
        }
        mana -= spell.costMana;
        spell.onStart.accept(this);
        // instant effect
        if (spell.instant) {
            if (spell.dealsDamage != 0) {
                enemy.hit -= spell.dealsDamage - enemy.armor > 0 ? spell.dealsDamage - enemy.armor : 1;
            }
            hit += spell.heals;
        } else { // long run effect
            activeEffects.add(spell);
        }
    }

    public void applyEffects(Player enemy) {
        activeEffects.forEach(s -> {
            armor += s.armor;
            if (s.dealsDamage != 0) {
                enemy.hit -= s.dealsDamage - enemy.armor > 0 ? s.dealsDamage - enemy.armor : 1;
            }
            mana += s.manaRestore;
            s.lastsTurns--;
        });
        // call onFinish on an effects that's gonna be expired
        activeEffects.stream().filter(s -> s.lastsTurns == 0).forEach(s -> s.onFinish.accept(this));
        // expire all the activeEffects which timer is 0
        activeEffects = activeEffects.stream().filter(s -> s.lastsTurns > 0).collect(Collectors.toList());
    }

    public void attack(Player enemy) {
        enemy.hit -= damage - enemy.armor > 0 ? damage - enemy.armor : 1;
    }

    static class Spell {
        public final int costMana;
        public final String name;
        private final int dealsDamage;
        private final int heals;
        private final int armor;
        private final int manaRestore;
        private final boolean instant;
        public int lastsTurns;
        private Consumer<Player> onStart; // On cast applies expression to player
        private Consumer<Player> onFinish; // To represent spells like "Shield" (instant effect, but has time to work)

        public Spell(int costMana, String name, int dealsDamage, int heals, int armor, int manaRestore, boolean instant, int lastsTurns, Consumer<Player> onStart, Consumer<Player> onFinish) {
            this.costMana = costMana;
            this.name = name;
            this.dealsDamage = dealsDamage;
            this.heals = heals;
            this.armor = armor;
            this.manaRestore = manaRestore;
            this.instant = instant;
            this.lastsTurns = lastsTurns;
            this.onStart = onStart;
            this.onFinish = onFinish;
        }

        public Spell(Spell obj) {
            costMana = obj.costMana;
            name = obj.name;
            dealsDamage = obj.dealsDamage;
            heals = obj.heals;
            armor = obj.armor;
            manaRestore = obj.manaRestore;
            instant = obj.instant;
            lastsTurns = obj.lastsTurns;
            onFinish = obj.onFinish;
            onStart = obj.onStart;
        }

        // should override hashCode too
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Spell spell = (Spell) o;
            return name.equals(spell.name);
        }
    }
}

public class Day22 {
    List<Player.Spell> spells = new ArrayList<>();
    Player player;
    Player enemy;

    public static void main(String[] args) {
        new Day22();
    }

    public Day22() {
        Consumer<Player> empty = (p) -> {
        };
        spells.add(new Player.Spell(53, "magic missile", 4, 0, 0, 0, true, 0, empty, empty));
        spells.add(new Player.Spell(73, "drain", 2, 2, 0, 0, true, 0, empty, empty));
        spells.add(new Player.Spell(113, "shield", 0, 0, 0, 0, false, 6, p -> p.armor += 7, p -> p.armor -= 7));
        spells.add(new Player.Spell(173, "poison", 3, 0, 0, 0, false, 6, empty, empty));
        spells.add(new Player.Spell(229, "recharge", 0, 0, 0, 101, false, 5, empty, empty));
        player = new Player(50, 0, 500, 0);
        enemy = new Player(71, 0, 0, 10);
        try {
            System.out.println("Result is: " + emu(new Player(player), new Player(enemy), 0, null));
        } catch (Exception e) { // cast spell with no mana not allowed
            System.out.println(e.getMessage());
        }
    }

    int emu(Player player, Player enemy, int sumMana, Player.Spell spell) throws Exception {
        if (spell != null) { // first step is not a game, start emulation from the different spells
            // Player turn
            //hard mode
            player.hit--;
            if (player.hit <= 0) {
                return Integer.MAX_VALUE; // player is dead
            }
            player.applyEffects(enemy);
            if (enemy.hit <= 0) {
                return sumMana; // enemy is dead
            }
            player.castSpell(spell, enemy);
            sumMana += spell.costMana;
            if (enemy.hit <= 0) {
                return sumMana; // enemy is dead
            }
            // Enemy turn
            player.applyEffects(enemy);
            if (enemy.hit <= 0) {
                return sumMana; // enemy is dead
            }
            enemy.attack(player);
            if (player.hit <= 0) {
                return Integer.MAX_VALUE; // player is dead
            }
        }
        List<Integer> manas = new ArrayList<>();
        for (Player.Spell s : spells) {
            if (player.mana - s.costMana > 0) { //must have enough mana to cast a spell
                // cannot cast a spell that would start an effect which is already active
                // effects can be started on the same turn they end
                if (!player.activeEffects.stream().filter(e -> e.lastsTurns > 1).anyMatch(e -> e.equals(s))) {
                    manas.add(emu(new Player(player), new Player(enemy), sumMana, new Player.Spell(s)));
                }
            }
        }
        if (manas.isEmpty()) {
            return Integer.MAX_VALUE; //If you cannot afford to cast any spell, you lose.
        }
        // least amount of mana you can spend and still win the fight
        return manas.stream().min((a, b) -> a - b).get();
    }
}
