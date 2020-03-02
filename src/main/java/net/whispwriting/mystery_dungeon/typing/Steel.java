package net.whispwriting.mystery_dungeon.typing;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Steel extends Type {

    private List<String> weakTo = new ArrayList<>();
    private List<String> noDamage = new ArrayList<>();
    private List<String> strongTo = new ArrayList<>();

    public Steel(){
        super("Steel");
    }

    public void init(){

    }

    @Override
    public int compareTo(@NotNull Type o) {
        if (weakTo.contains(o.name)) {
            return -1;
        } else if (noDamage.contains(o.name)) {
            return -2;
        } else if (strongTo.contains(o.name)) {
            return 1;
        } else {
            return 0;
        }
    }
}
