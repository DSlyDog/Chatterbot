package net.whispwriting.mystery_dungeon.commands;

import io.bluecube.thunderbolt.io.ThunderFile;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.moves.Move;
import net.whispwriting.mystery_dungeon.moves.MoveSelector;
import net.whispwriting.mystery_dungeon.typing.Type;
import net.whispwriting.mystery_dungeon.typing.TypeSelector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateCharacterCommand extends ListenerAdapter {

    private int count = 0;
    private int movesAdded = 0;
    private String executor;
    private String characterName;
    private String characterSpecies;
    private List<Move> moves = new ArrayList<>();
    private int level;
    private Type type1;
    private Type type2;
    private TypeSelector typeSelector = new TypeSelector();
    private MoveSelector moveSelector = new MoveSelector();
    private double requiredXP = 326;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals("!cchar") && !event.getAuthor().isBot()){
            executor = event.getAuthor().getName();
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("What is your character's name?").queue();
            count++;
        }

        else if (count == 1 && event.getAuthor().getName().equals(executor)){
            characterName = event.getMessage().getContentRaw();
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Your character's name will be " + characterName).queue();
            event.getChannel().sendMessage("What kind of Pokemon is " + characterName + "?").queue();
            count++;
        }

        else if (count == 2 && event.getAuthor().getName().equals(executor)){
            characterSpecies = event.getMessage().getContentRaw();
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(characterName + " will be a(n) " + characterSpecies).queue();
            event.getChannel().sendMessage("What level will " + characterName + " start at?").queue();
            count++;
        }

        else if (count == 3 && event.getAuthor().getName().equals(executor)){
            String levelStr = event.getMessage().getContentRaw();
            try {
                level = Integer.parseInt(levelStr);
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage(characterName + "'s level will be " + level).queue();
                event.getChannel().sendMessage("Please tell me " + characterName + "'s types (for example, rock). " +
                        "If they ony have one, type \"none\" for the second one.").queue();
                count++;
            }catch(NumberFormatException e){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("I'm sorry. Level must be a number. Please try again.").queue();
            }
        }

        else if (count == 4 && event.getAuthor().getName().equals(executor)){
            if (movesAdded < 2) {
                if (event.getMessage().getContentRaw().equals("none")) {
                    if (movesAdded == 0) {
                        type1 = null;
                        movesAdded++;
                    }else {
                        type2 = null;
                        movesAdded++;
                    }
                } else {
                    Type result = typeSelector.getType(event.getMessage().getContentRaw());
                    if (result == null) {
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("Sorry, I could not find a matching type. Please try again.").queue();
                    }else{
                        if (movesAdded == 0) {
                            type1 = result;
                            movesAdded++;
                        }
                        else {
                            type2 = result;
                            movesAdded++;
                        }
                    }
                }
            }
            if (movesAdded > 1){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Please enter " + characterName + "'s moves. When you are done, " +
                        "type \"done\".").queue();
                count++;
                movesAdded = 0;
            }
        }

        else if (count == 5 && event.getAuthor().getName().equals(executor)){
            if (event.getMessage().getContentRaw().equalsIgnoreCase("done")){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Thank you. I will now create your character.").queue();
                count = 0;
                create(event);
            }else{
                Move move = moveSelector.getMove(event.getMessage().getContentRaw());
                if (move == null){
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("Sorry, I could not find a matching move. Please try again.").queue();
                }
                else {
                    moves.add(move);
                    System.out.println("number of moves: " + moves);
                }
            }
        }
    }

    private void create(GuildMessageReceivedEvent event){
        int levelCount = 1;
        int health = 20;
        while (levelCount < level){
            health += 3;
            levelCount++;
        }
        System.out.println(System.getProperty("user.dir"));
        File path = new File(System.getProperty("user.dir") + "/" + event.getGuild().getName() +"/characters/");
        if (!path.exists()){
            boolean successful = path.mkdirs();
            System.out.println(successful);
        }
        ThunderFile characterFile = new ThunderFile(characterName, event.getGuild().getName() +"/characters/");
        for (int i=0; i<level; i++){
            requiredXP = requiredXP + (requiredXP * 0.1);
        }
        characterFile.set("owner", event.getAuthor().getName());
        characterFile.set("name", characterName);
        characterFile.set("species", characterSpecies);
        characterFile.set("level", level);
        double xp = 0.1;
        characterFile.set("xp", xp);
        characterFile.set("health", health);
        characterFile.set("last levelup", level);
        characterFile.set("requiredXP", requiredXP);
        try {
            characterFile.set("type1", type1.toString());
        }catch(NullPointerException e){
            //
        }
        try {
            characterFile.set("type2", type2.toString());
        }catch(NullPointerException e){
            //
        }
        for (int i=0; i<moves.size(); i++){
            characterFile.set("move"+i, moves.get(i).toString());
        }
        try {
            characterFile.save();
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Your character has been created. Type \"!cinfo " + characterName +
                    " \" to view information on your character.").queue();
        }catch(IOException e){
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("I'm sorry. I was unable to save your character. Please try again.").queue();
        }finally {
            moves.clear();
        }
    }
}
