package net.whispwriting.mystery_dungeon.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.utils.JFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCommand extends ListenerAdapter {

    private JFile file;
    private String executor;
    private int count = -1;

    private String dungeonName;
    private int numberOfFloors;
    private Map<String, List<String>> pokemon = new HashMap<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if (event.getMessage().getContentRaw().equals("!create"))
        {
            executor = event.getAuthor().getName();
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Welcome to the Mystery Dungeon creator!").queue();
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("What is the dungeon name?").queue();
            count++;
        }

        else if (event.getAuthor().getName().equals(executor) && count == 0){
            dungeonName = event.getMessage().getContentRaw();
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("The dungeon will be called " + dungeonName).queue();
            event.getChannel().sendMessage("How many floors will the dungeon have?").queue();
            count ++;
        }

        else if (event.getAuthor().getName().equals(executor) && count == 1){
            String floors = event.getMessage().getContentRaw();
            try{
                numberOfFloors = Integer.parseInt(floors);
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("The dungeon will have " + numberOfFloors + " floors").queue();
                event.getChannel().sendMessage("Please list the Pokemon that can be found in this dungeon. " +
                        "Type \"done\" when you are done.").queue();
                count++;
            }catch(NumberFormatException e)
            {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("That didn't work. Please give me a number.").queue();
            }
        }

        else if (event.getAuthor().getName().equals(executor) && count == 2){
            String message = event.getMessage().getContentRaw();
            if (!message.equalsIgnoreCase("done")) {
                String[] pokeData = event.getMessage().getContentRaw().split(", ");
                String pokemonName = pokeData[0];
                List<String> moves = new ArrayList<>();
                for (int i = 1; i < pokeData.length; i++) {
                    moves.add(pokeData[i]);
                }
                pokemon.put(pokemonName, moves);
            }else{
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("The dungeon will have the following Pokemon:").queue();
                StringBuilder builder = new StringBuilder();
                for (Map.Entry entry : pokemon.entrySet()){
                    String mon = (String) entry.getKey();
                    builder.append(mon + "\n");
                }
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage(builder.toString()).queue();
                event.getChannel().sendMessage("Please type \"confirm\" to finish creation, or \"cancel\" to cancel.").queue();
                count++;
            }
        }

        else if (event.getAuthor().getName().equals(executor) && count == 3){
            if (event.getMessage().getContentRaw().equalsIgnoreCase("confirm")){
                File path = new File(event.getGuild().getName());
                path.mkdir();
                file = new JFile(dungeonName, event.getGuild().getName());
                file.set("name", dungeonName);
                file.set("floors", numberOfFloors);
                List<String> mons = new ArrayList<>();
                for (Map.Entry entry : pokemon.entrySet()){
                    String mon = (String) entry.getKey();
                    mons.add(mon);
                    List<String> moves = (List<String>) entry.getValue();
                    file.set(mon, moves);
                }
                file.set("Pokemon", mons);
                try {
                    file.save();
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("Dungeon created. To start, type \"!start " + dungeonName).queue();
                    count = -1;
                    executor = "";
                }catch(IOException e){
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("There was an error saving the dungeon. Please try again.").queue();
                    count = -1;
                    executor = "";
                }
            }else if (event.getMessage().getContentRaw().equalsIgnoreCase("cancel")){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Dungeon creation canceled. Have a nice day!").queue();
                count = -1;
                executor = "";
            }else{
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("I'm sorry, I didn't understand that. Please type \"confirm\" or \"cancel\".").queue();
            }
        }
    }

}
