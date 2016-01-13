package com.la0rg.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by la0rg on 13.01.2016.
 */

class Player {
    private int hit;
    private int armor;
    private int damage;
    private int mana;
    private List<Spell> effects = new ArrayList<>();


    public Player(int hit, int armor, int mana, int damage) {
        this.hit = hit;
        this.armor = armor;
        this.mana = mana;
        this.damage = damage;
    }

    public boolean castSpell(Spell spell, Player enemy) {
        if (mana < spell.costMana) {
            return false;
        }
        mana -= spell.costMana;
        // instant effect
        if (spell.instant) {
            enemy.hit -= spell.dealsDamage - enemy.armor > 0 ? spell.dealsDamage - enemy.armor : 1;
            hit += spell.heals;
        } else {
            effects.add(spell);
        }
        return true;
    }

    public void applyEffects(Player enemy) {
        effects.forEach(s -> {
            armor += s.armor;
            enemy.hit -= s.dealsDamage - enemy.armor > 0 ? s.dealsDamage - enemy.armor : 1;
            mana += s.manaRestore;
            s.lastsTurns--;
        });
        // expire all the effects which timer is 0
        effects = effects.stream().filter(s -> s.lastsTurns > 0).collect(Collectors.toList());
    }

    public void attack(Player enemy) {
        enemy.hit -= damage - enemy.armor > 0 ? damage - enemy.armor : 1;
    }

    static class Spell {
        public final int costMana;
        private final int dealsDamage;
        private final int heals;
        private final int armor;
        private final int manaRestore;
        private final boolean instant;
        private int lastsTurns;

        public Spell(int costMana, int dealsDamage, int heals, int armor, int manaRestore, boolean instant, int lastsTurns) {
            this.costMana = costMana;
            this.dealsDamage = dealsDamage;
            this.heals = heals;
            this.armor = armor;
            this.manaRestore = manaRestore;
            this.instant = instant;
            this.lastsTurns = lastsTurns;
        }
    }
}

public class Day22 {
    public static void main(String[] args) {
        List<Player.Spell> spells = new ArrayList<>();
        spells.add(new Player.Spell(53, 4, 0, 0, 0, true, 0))// magic missile
    }
}
