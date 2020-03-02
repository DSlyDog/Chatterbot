package net.whispwriting.mystery_dungeon.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CreateHelpCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if (event.getMessage().getContentRaw().equals("!hcreate")) {
            EmbedBuilder blurp = new EmbedBuilder();
            blurp.setColor(0x1290a3);
            blurp.setTitle("Dungeon Creator Help");
            blurp.addField("", "First type !create. This will start the creator. The creator will prompt " +
                    "you will questions to answer about the dungeon you are creating including the name, number of floors, " +
                    "and Pokemon inside. When entering pokemon, you will need to provide them in a specific format so moves" +
                    "can be registered. It is as follows: \n  Lucario, Close Combat, Extreme Speed, Dragon Pulse, Bone Rush" +
                    "\n It is a list of comma separated words starting with the name of the Pokemon. Do this wrong, and you" +
                    "might wind up seeing, \"A wild Close Combat has appeared.\"", false);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(blurp.build()).queue();;
        }
    }

}
