package net.whispwriting.mystery_dungeon.game;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.whispwriting.mystery_dungeon.moves.Move;
import net.whispwriting.mystery_dungeon.typing.Type;

import java.util.List;
import java.util.Random;

public class NPC extends Pokemon {

    public NPC(String species, int level, List<Move> movveset, Type type1, Type type2, int maxHealth, MessageChannel channel){
        super(species, level , maxHealth, maxHealth, movveset, type1, type2, channel);
    }

    public String species(){
        return species;
    }

    @Override
    public double attack(Pokemon enemy) {
        double damage = BASE_HIT * level;
        int finalResult = -3;
        int result1 = moveset.get(0).compareTo(enemy.type1);
        int result2 = moveset.get(0).compareTo(enemy.type2);

        if (result1 == -1 || result2 == -1)
            finalResult = -1;
        if (result1 == -2  || result2 == -2)
            finalResult = -2;
        if (result1 == 0 || result2 == 0)
            finalResult = 0;
        if (result1 == 1 || result2 == 1)
            finalResult = 1;

        if (finalResult == 1)
            return damage *2;
        else if (finalResult == 0)
            return damage;
        else if (finalResult == -1)
            return damage * (damage / 2);
        else
            return 0;
    }

    @Override
    public String dodge(int roll, int damage) {
        if (roll >= 18) {
            return "COMPLETE DODGE";
        }else if (roll >= 15) {
            if (damage(damage / 3).equals("FAINTED"))
                return "THIRD DODGE - FAINT";
            return "THIRD DODGE";
        }else if (roll >= 13){
            if (damage(damage / 2).equals("FAINTED"))
                return "HALF DODGE - FAINT";
            return "HALF DAMAGE";
        }else{
            if (damage(damage).equals("FAINTED"))
                return "NO DODGE - FAINT";
            return "NO DODGE";
        }

    }

    @Override
    public boolean counter() {
        Random random = new Random();
        int result = random.nextInt(100) + 1;
        System.out.println(result);
        if (result < 50)
            return false;
        else
            return true;
    }

    @Override
    public String damage(double damage){
        health -= damage;
        if (health <= 0){
            fainted = true;
            health = 0;
            return "FAINTED";
        }
        return "HIT";
    }

    @Override
    public void serialize() {

    }

    @Override
    public String toString(){
        return species;
    }
}
