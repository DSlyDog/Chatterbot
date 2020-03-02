package net.whispwriting.mystery_dungeon.game;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class ChatterEvent extends ListenerAdapter {

    private TextChannel channel;
    private List<String> players;

    public ChatterEvent(TextChannel channel, List<String> players){
        this.channel = channel;
        this.players = players;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getChannel() == channel){
            if (players.contains(event.getAuthor().getName())){

            }
        }
    }

}
