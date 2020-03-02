package net.whispwriting.mystery_dungeon.game;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.whispwriting.mystery_dungeon.Chatterbot;
import net.whispwriting.mystery_dungeon.commands.InGameCommands;
import net.whispwriting.mystery_dungeon.moves.Move;
import net.whispwriting.mystery_dungeon.moves.MoveSelector;
import net.whispwriting.mystery_dungeon.typing.TypeSelector;

import java.io.IOException;
import java.util.*;

public class Game {

    private MessageChannel channel;
    private String serverName;
    private String narrator;
    private Map<String, List<Move>> moeMap = new HashMap<>();
    private List<Player> players = new ArrayList<>();
    private MoveSelector moveSelector = new MoveSelector();
    private TypeSelector typeSelector = new TypeSelector();

    public Game(Map<String, List<Move>> moveMap, MessageChannel channel, String serverName, List<String> playerStrings, String narrator){
        this.moeMap = moveMap;
        this.channel = channel;
        this.serverName = serverName;
        this.narrator = narrator;
        init(playerStrings);
    }

    private boolean init(List<String> playerString){
        for (String character : playerString) {
            try {
                ThunderFile characterFile = null;
                try {
                    characterFile = Thunderbolt.load(character, serverName + "/characters/");
                }catch(FileLoadException e){
                    Thunderbolt.unload(character);
                    try {
                        characterFile = Thunderbolt.load(character, serverName + "/characters/");
                    }catch(FileLoadException f){
                        //
                    }
                }
                String name = characterFile.getString("name");
                String species = characterFile.getString("species");
                int level = characterFile.getInt("level");
                double requiredXP = characterFile.getDouble("requiredXP");
                double currentXP = characterFile.getDouble("xp");
                int lastLevelUp = characterFile.getInt("last levelup");
                int health = characterFile.getInt("health");
                String type1 = "";
                try {
                    type1 = characterFile.getString("type1");
                } catch (JSONException e) {
                    type1 = "none";
                }

                String type2 = "";
                try {
                    type2 = characterFile.getString("type2");
                } catch (JSONException e) {
                    type2 = "none";
                }

                int count = 0;
                List<Move> moves = new ArrayList<>();
                try {
                    while (characterFile.get("move" + count) != null) {
                        Move move = moveSelector.getMove(characterFile.getString("move" + count));
                        moves.add(move);
                        count++;
                    }
                } catch (JSONException e) {
                    // all moves added
                }
                players.add(new Player(name, species, level, currentXP, requiredXP, moves, typeSelector.getType(type1), typeSelector.getType(type2), health, lastLevelUp, channel));
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public void start(){
        Chatterbot.jda.addEventListener(new InGameCommands(players, narrator, channel));
    }

}
