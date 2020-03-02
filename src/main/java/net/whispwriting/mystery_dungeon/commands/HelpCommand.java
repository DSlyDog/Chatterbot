package net.whispwriting.mystery_dungeon.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.Chatterbot;

public class HelpCommand extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].contains(Chatterbot.prefix + "help")){
            EmbedBuilder blurp = new EmbedBuilder();
            blurp.setTitle("Mystery Dungeon Help");
            blurp.addField("!hcreate", "Shows creator help.", false);
            blurp.addField("!create", "Starts the dungeon creator dialog.", false);
            blurp.addField("!start <dungeon name> <narrator name>", "Starts the named dungeon if it exists for the given players.", false);
            blurp.addField("!newalias <name>", "Create a new alias.", false);
            blurp.addField("!aliastag <name> <tag>", "Create or change a tag for your alias.", false);
            blurp.addField("!aliasavatar <name>", "Change the profile image of the alias (requires attaching an image).", false);
            blurp.setColor(0x1290a3);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(blurp.build()).queue();
            blurp.clear();
        }
    }

}
