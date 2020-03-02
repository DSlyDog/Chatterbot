package net.whispwriting.mystery_dungeon.typing;

public class TypeSelector {

    public Type getType(String name){
        switch (name.toLowerCase()){
            case "bug":
                return new Bug();
            case "dark":
                return new Dark();
            case "dragon":
                return new Dragon();
            case "electric":
                return new Electric();
            case "fairy":
                return new Fairy();
            case "fighting":
                return new Fighting();
            case "fire":
                return new Fire();
            case "flying":
                return new Flying();
            case "ghost":
                return new Ghost();
            case "grass":
                return new Grass();
            case "ground":
                return new Ground();
            case "ice":
                return new Ice();
            case "normal":
                return new Normal();
            case "poison":
                return new Poison();
            case "psychic":
                return new Psychic();
            case "rock":
                return new Rock();
            case "steel":
                return new Steel();
            case "water":
                return new Water();
            default:
                return null;
        }
    }

}
