package net.whispwriting.mystery_dungeon.game;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.whispwriting.mystery_dungeon.moves.Move;
import net.whispwriting.mystery_dungeon.typing.Type;

import java.util.List;

public abstract class Pokemon {

    protected String species;
    protected int level;
    protected int health;
    protected int maxHealth;
    protected List<Move> moveset;
    protected Type type1, type2;
    protected double BASE_HIT = 3;
    protected boolean fainted = false;
    protected MessageChannel channel;

    public Pokemon(String species, int level, int health, int maxHealth, List<Move> moveset, Type type1, Type type2, MessageChannel channel){
        this.species = species;
        this.level = level;
        this.health = health;
        this.moveset = moveset;
        this.type1 = type1;
        this.type2 = type2;
        this.channel = channel;
        this.maxHealth = maxHealth;

        if (this.type1 != null)
            this.type1.init();
        if (this.type2 != null)
            this.type2.init();
    }

    abstract public double attack(Pokemon enemy);
    abstract public String dodge(int roll, int damage);
    abstract public boolean counter();
    abstract public String damage(double damage);
    abstract public void serialize();


}
