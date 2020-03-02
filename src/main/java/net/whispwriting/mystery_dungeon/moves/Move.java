package net.whispwriting.mystery_dungeon.moves;

import net.whispwriting.mystery_dungeon.typing.Type;
import org.jetbrains.annotations.NotNull;

public class Move implements Comparable<Type> {

    protected Type type;
    protected String name;

    public Move(Type type, String name){
        this.type = type;
        this.name = name;
    }

    @Override
    public int compareTo(@NotNull Type o) {
        System.out.println(o);
        o.init();
        type.init();
        return type.compareTo(o);
    }

    @Override
    public String toString(){
        return name;
    }

}
