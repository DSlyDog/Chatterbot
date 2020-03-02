package net.whispwriting.mystery_dungeon.game;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.moves.Move;
import net.whispwriting.mystery_dungeon.moves.MoveSelector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameSetup extends ListenerAdapter {

    private int floors;
    private String dungeonName;
    private List<String> players = new ArrayList<>();
    private ThunderFile file;
    private Map<String, List<String>> pokemon = new HashMap<>();
    private MessageChannel channel;
    private String serverName;
    private String narrator;
    private MoveSelector moveSelector = new MoveSelector();

    public GameSetup(String dungeonName, String serverName, MessageChannel channel, List<String> players, String narrator) throws FileLoadException, IOException {
        this.dungeonName = dungeonName;
        this.channel = channel;
        this.serverName = serverName;
        this.narrator = narrator;
        this.players.addAll(players);
        File path = new File(serverName);
        path.mkdir();
        try {
            this.file = Thunderbolt.load(dungeonName, serverName);
        }catch(FileLoadException e){
            Thunderbolt.unload(dungeonName);
            try{
                Thunderbolt.load(dungeonName, serverName);
            }catch(FileLoadException f){
                //
            }
        }
        this.floors = file.getInt("floors");
        List<String> mons = file.getStringList("Pokemon");
        for (String mon : mons){
            List<String> moves = file.getStringList(mon);
            this.pokemon.put(mon, moves);
        }
    }

    public void launch() throws IOException{
        channel.sendTyping().queue();
        channel.sendMessage("Welcome to the " + this.dungeonName + " dungeon.").queue();
        channel.sendMessage("This is a " + this.floors + " floor dungeon containing the following Pokemon:").queue();
        StringBuilder builder = new StringBuilder();
        for (Map.Entry entry : this.pokemon.entrySet()){
            builder.append(entry.getKey()).append("\n");
        }
        channel.sendMessage(builder.toString()).queue();
        channel.sendMessage("This instance will contain the following players:").queue();
        builder = new StringBuilder();
        for (String player : this.players){
            builder.append(player).append("\n");
        }
        if (builder.length() > 0)
            channel.sendMessage(builder.toString()).queue();

        Map<String, List<Move>> moveMap = new HashMap<>();
        for (String player : players){
            ThunderFile characterFile = null;
            try {
                characterFile = Thunderbolt.load(player, serverName + "/characters/");
            }catch(FileLoadException e){
                Thunderbolt.unload(player);
                try {
                    characterFile = Thunderbolt.load(player, serverName + "/characters/");
                }catch(FileLoadException f){
                    //
                }
            }
            int count = 0;
            List<Move> moves = new ArrayList<>();
            try {
                while (characterFile.get("move" + count) != null) {
                    String moveString = characterFile.getString("move" + count);
                    Move move = moveSelector.getMove(moveString);
                    moves.add(move);
                    count++;
                }
            }catch(JSONException e){
                moveMap.put(player, moves);
            }
        }
        Game game = new Game(moveMap, channel, serverName, players, narrator);
        game.start();
    }

}
