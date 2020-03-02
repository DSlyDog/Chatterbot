package net.whispwriting.mystery_dungeon.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.Chatterbot;

import java.util.ArrayList;
import java.util.List;

public class NarrateCommand extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (!(event.getMessage().getContentRaw().equals("!narrator"))){
            return;
        }
        if (event.getAuthor().isBot()){
            return;
        }
        List<Role> roles = event.getGuild().getRolesByName("Narrator", true);
        System.out.println(roles.size());
        List<Member> narrators;
        try {
            Role narrator = roles.get(0);
            narrators = event.getGuild().getMembersWithRoles(narrator);
        }catch(IndexOutOfBoundsException e){
            narrators = new ArrayList<>();
        }
        boolean pass = false;
        for (Member member : narrators){
            if (event.getMember() == member){
                pass = true;
            }
        }
        if (pass) {
            if (event.getMessage().getContentRaw().equals("!narrator")) {
                if (Chatterbot.narrators.contains(event.getAuthor().getName())) {
                    if (Chatterbot.narrating.contains(event.getAuthor().getName())) {
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("You must stop narrating before you can stop being a narrator.").queue();
                    } else {
                        Chatterbot.narrators.remove(event.getAuthor().getName());
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("You are no longer a narrator.").queue();
                    }
                } else {
                    Chatterbot.narrators.add(event.getAuthor().getName());
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("You are now a narrator.").queue();
                }
            }
        }else{
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("You do not have permission to be a narrator.").queue();
        }
    }
}
