package net.whispwriting.mystery_dungeon.moves;

import net.whispwriting.mystery_dungeon.typing.Fire;
import net.whispwriting.mystery_dungeon.typing.Grass;

public class MoveSelector {

    public Move getMove(String name){
        switch (name.toLowerCase()){
            case "absorb":
                return new Move(new Grass(), "Absorb");
            case "flamethrower":
                return new Move(new Fire(), "Flamethrower");
            default:
                return null;
        }
    }

}
