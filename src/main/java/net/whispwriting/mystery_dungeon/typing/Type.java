package net.whispwriting.mystery_dungeon.typing;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

public abstract class Type implements Comparable<Type>{

    protected String name;

    public Type(String name){
        this.name = name;
    }

    abstract public void init();

    @Override
    public String toString(){
        return name;
    }
}
