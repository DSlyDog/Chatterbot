package net.whispwriting.mystery_dungeon.commands;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.Chatterbot;
import net.whispwriting.mystery_dungeon.game.GameSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartCommand extends ListenerAdapter {

    private int count = 0;
    private int count2 = 0;
    private TextChannel channel;
    private List<String> players = new ArrayList<>();
    private String dungeonName;
    private String missionType;
    private String objective;
    private String whoOrWhat;
    private int objectiveFloor;
    private String executor;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (count == 0) {
            if (event.getMessage().getContentRaw().contains("!start")) {
                executor = event.getAuthor().getName();
                String[] message = event.getMessage().getContentRaw().split(" ");
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < message.length; i++) {
                    if (i == message.length - 1) {
                        builder.append(message[i]);
                    } else {
                        builder.append(message[i]).append(" ");
                    }
                }
                this.dungeonName = builder.toString();
                String channelNameStr = dungeonName.toLowerCase().replace(" ", "-");
                List<TextChannel> channels = Chatterbot.jda.getTextChannelsByName(channelNameStr, true);
                this.channel = channels.get(0);
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Narrator, please enter a mission type (rescue, find, explore).").queue();
                this.count++;
            }
        }else if (count == 1 && Chatterbot.narrators.contains(event.getAuthor().getName())){
            if (count2 == 0){
                String type = event.getMessage().getContentRaw();
                if (type.equalsIgnoreCase("rescue") || type.equalsIgnoreCase("find") || type.equalsIgnoreCase("explore")){
                    this.missionType = type;
                    if (missionType.equalsIgnoreCase("rescue")){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("Who needs to be rescued?").queue();
                    }else if (missionType.equalsIgnoreCase("find")){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("What needs to be found?").queue();
                    }else if (missionType.equalsIgnoreCase("explore")){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("Until what floor does the dungeon need to be explored?").queue();
                    }
                    this.count2++;
                }else{
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("I'm sorry, I don't recognize that mission type. Please enter" +
                            "rescue, find, or explore.").queue();
                }
            }else if (count2 == 1){
                if (missionType.equalsIgnoreCase("rescue")){
                    whoOrWhat = event.getMessage().getContentRaw();
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("On what floor is the rescue?").queue();
                    count2++;
                }else if (missionType.equalsIgnoreCase("find")){
                    whoOrWhat = event.getMessage().getContentRaw();
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("On what floor is the item?").queue();
                    count2++;
                }else{
                    try{
                        objectiveFloor = Integer.parseInt(event.getMessage().getContentRaw());
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("Everyone who is participating in this instance, please say " +
                                "the name of your character now. Once everyone has queued, type \"ready\" to begin.").queue();
                        count++;
                    }catch(NumberFormatException e){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("I'm sorry, I need a number for the floor.").queue();
                    }
                }
            }else if (count2 == 2){
                if (missionType.equalsIgnoreCase("rescue")){
                    try{
                        objectiveFloor = Integer.parseInt(event.getMessage().getContentRaw());
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("Everyone who is participating in this instance, please say " +
                                "the name of your character now. Once everyone has queued, type \"ready\" to begin.").queue();
                        count++;
                    }catch(NumberFormatException e){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("I'm sorry, I need a number for the floor.").queue();
                    }
                }else if (missionType.equalsIgnoreCase("find")){
                    try{
                        objectiveFloor = Integer.parseInt(event.getMessage().getContentRaw());
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("Everyone who is participating in this instance, please say " +
                                "the name of your character now. Once everyone has queued, type \"ready\" to begin.").queue();
                        count++;
                    }catch(NumberFormatException e){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("I'm sorry, I need a number for the floor.").queue();
                    }
                }
            }
        }else if (count == 2){
            if (!event.getAuthor().getName().equalsIgnoreCase("Mystery Dungeon")) {
                if (event.getMessage().getContentRaw().equalsIgnoreCase("ready")) {
                    count = 0;
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("An instance of Eterna has now started.").queue();
                    try {
                        GameSetup controller = new GameSetup(dungeonName, event.getGuild().getName(), channel, players, executor);
                        controller.launch();
                    } catch (FileLoadException e) {
                        e.printStackTrace();
                        event.getChannel().sendMessage("Sorry, I couldn't find that dungeon.").queue();
                    } catch (IOException e) {
                        event.getChannel().sendMessage("Sorry, I couldn't find that dungeon.").queue();
                    }
                } else {
                    String name = event.getMessage().getContentRaw();
                    ThunderFile character = null;
                    try{
                        try {
                            character = Thunderbolt.load(name, event.getGuild().getName() + "/characters/");
                        }catch(FileLoadException e){
                            Thunderbolt.unload(name);
                            try{
                                character = Thunderbolt.load(name, event.getGuild().getName() + "/characters/");
                            }catch(FileLoadException f){
                                event.getChannel().sendTyping().queue();
                                event.getChannel().sendMessage("I'm sorry, I could not find a character by that name.").queue();
                            }
                        }
                        String owner = character.getString("owner");
                        if (owner.equals(event.getAuthor().getName())){
                            players.add(event.getMessage().getContentRaw());
                        }else{
                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessage("You do not own that character. Please use only characters" +
                                    " you own.").queue();
                        }
                    }catch(IOException e){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("I'm sorry, I could not find a character by that name.").queue();
                    }catch (JSONException e){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("I'm sorry, I could not find a character by that name.").queue();
                    }catch(NullPointerException e){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("I'm sorry, I could not find a character by that name.").queue();
                    }
                }
            }
        }
    }

}
