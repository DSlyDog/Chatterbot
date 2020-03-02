package net.whispwriting.mystery_dungeon.utils;

public class IndexRegistry {

    private int start;
    private int end;

    public IndexRegistry(int start, int end){
        this.start = start;
        this.end = end;
    }

    public int getStart(){
        return start;
    }

    public int getEnd(){
        return end;
    }

}
