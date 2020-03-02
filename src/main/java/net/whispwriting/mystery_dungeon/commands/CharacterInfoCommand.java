package net.whispwriting.mystery_dungeon.commands;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.moves.Move;
import net.whispwriting.mystery_dungeon.moves.MoveSelector;
import net.whispwriting.mystery_dungeon.typing.Type;
import net.whispwriting.mystery_dungeon.typing.TypeSelector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharacterInfoCommand extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (message[0].equals("!cinfo") && message.length > 1){
            StringBuilder builder = new StringBuilder();
            for (int i=1; i<message.length; i++){
                if (i == message.length -1)
                    builder.append(message[i]);
                else
                    builder.append(message[i]).append(" ");
            }
            String character = builder.toString();
            try {
                event.getChannel().sendTyping().queue();
                ThunderFile characterFile = null;
                try {
                    characterFile = Thunderbolt.load(character, event.getGuild().getName() + "/characters/");
                }catch(FileLoadException e){
                    Thunderbolt.unload(character);
                    try {
                        characterFile = Thunderbolt.load(character, event.getGuild().getName() + "/characters/");
                    }catch(FileLoadException f){
                        //
                    }
                }
                String name = characterFile.getString("name");
                String species = characterFile.getString("species");
                int level = characterFile.getInt("level");
                double requiredXP = characterFile.getDouble("requiredXP");
                double currentXP = characterFile.getDouble("xp");
                String type1 = "";
                try {
                    type1 = characterFile.getString("type1");
                }catch(JSONException e){
                    type1 = "none";
                }

                String type2 = "";
                try {
                    type2 = characterFile.getString("type2");
                }catch(JSONException e){
                    type2 = "none";
                }

                int count = 0;
                List<String> moves = new ArrayList<>();
                try {
                    while (characterFile.get("move" + count) != null) {
                        String move = characterFile.getString("move" + count);
                        moves.add(move);
                        count++;
                    }
                }catch(JSONException e){
                    // all moves added
                }

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Character Information");
                embed.setColor(0x1290a3);
                embed.addField("Name", name, false);
                embed.addField("Pokemon", species, false);
                int typeNumber = 1;
                if (!type1.equals("none")) {
                    embed.addField("Type " + typeNumber, type1, false);
                    typeNumber++;
                }
                if (!type2.equals("none"))
                    embed.addField("Type "+typeNumber, type2.toString(), false);
                embed.addField("Level", ""+level, false);
                embed.addField("Required XP for Level Up", ""+requiredXP, false);
                embed.addField("Current XP", ""+currentXP, false);

                builder = new StringBuilder();
                for (int i=0; i<moves.size(); i++){
                    if (i == moves.size()-1)
                        builder.append(moves.get(i));
                    else
                        builder.append(moves.get(i)).append("\n");
                }

                embed.addField("Moves", builder.toString(), false);
                event.getChannel().sendMessage(embed.build()).queue();
            }catch(IOException e){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Sorry, no character by that name could be found. Please try again.").queue();
            }catch(NullPointerException e){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Sorry, no character by that name could be found. Please try again.").queue();
            }

        }
    }

}
