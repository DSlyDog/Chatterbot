package net.whispwriting.mystery_dungeon.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.Chatterbot;

import java.util.ArrayList;
import java.util.List;

public class NarratingCommand extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getAuthor().isBot()){
            return;
        }
        List<Role> roles = event.getGuild().getRolesByName("Narrator", true);
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
            if (Chatterbot.narrating.contains(event.getAuthor().getName())){
               if (event.getMessage().getContentRaw().equals("!narrating")){
                   Chatterbot.narrating.remove(event.getAuthor().getName());
                   event.getChannel().sendTyping().queue();
                   event.getChannel().sendMessage("You are no longer narrating.").queue();
               }else{
                   String message = event.getMessage().getContentRaw();
                   event.getMessage().delete().queue();
                   event.getChannel().sendMessage(message).queue();
               }
            }else{
               if (event.getMessage().getContentRaw().equals("!narrating")){
                   if (Chatterbot.narrators.contains(event.getAuthor().getName())) {
                       Chatterbot.narrating.add(event.getAuthor().getName());
                       event.getChannel().sendTyping().queue();
                       event.getChannel().sendMessage("You are now narrating.").queue();
                   }else{
                       event.getChannel().sendTyping().queue();
                       event.getChannel().sendMessage("Sorry, you must be a narrator to narrate.").queue();
                   }
               }
            }
        }else{
            if ((event.getMessage().getContentRaw().equals("!narrating"))) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("You do not have permission to be a narrator.").queue();
            }
        }
    }
}
