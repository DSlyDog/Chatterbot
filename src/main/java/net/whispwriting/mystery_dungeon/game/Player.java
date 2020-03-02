package net.whispwriting.mystery_dungeon.game;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.whispwriting.mystery_dungeon.moves.Move;
import net.whispwriting.mystery_dungeon.typing.Type;

import java.util.List;
import java.util.Random;

public class Player extends Pokemon {

    private String name;
    private double xp;
    private double xpToNextLevel;
    private int lastLevelUp;


    public Player(String name, String species, int level, double xp, double xpToNextLevel, List<Move> movveset, Type type1, Type type2, int maxHealth, int lastLevelUp, MessageChannel channel){
        super(species, level , maxHealth, maxHealth, movveset, type1, type2, channel);
        this.name = name;
        this.xp = xp;
        this.xpToNextLevel = xpToNextLevel;
        this.lastLevelUp = lastLevelUp;
    }

    public void init(){
        int count = 1;
        while (count <= level){
            int even = count % 2;
            if (count != 1) {
                if (even == 0) {
                    BASE_HIT++;
                }
            }
            count++;
        }
    }

    public void addXP(double xp){
        this.xp += xp;
        if (xp >= xpToNextLevel){
            levelUp();
            this.xp = 0;
        }
    }

    private void levelUp(){
        level++;
        if (level > 100) {
            level = 100;
            return;
        }
        if (level - lastLevelUp == 2){
            BASE_HIT++;
        }
        health += 3;
        xpToNextLevel = xpToNextLevel * 0.1;
    }

    public String name(){
        return name;
    }

    public boolean isFainted(){
        return fainted;
    }

    @Override
    public double attack(Pokemon enemy) {
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
            return BASE_HIT *2;
        else if (finalResult == 0)
            return BASE_HIT;
        else if (finalResult == -1)
            return BASE_HIT / 2;
        else
            return 0;
    }

    @Override
    public String dodge(int roll, int damage) {
        if (roll >= 14) {
            return "COMPLETE DODGE";
        }else if (roll >= 7){
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
        return name;
    }
}
