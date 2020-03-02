package net.whispwriting.mystery_dungeon.event;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.utils.IndexRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewLineRead extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String message = event.getMessage().getContentRaw();
        List<IndexRegistry> indexes = new ArrayList<>();
        int firstIndex = message.indexOf(":");
        indexes.add(new IndexRegistry(0, firstIndex));
        List<IndexRegistry> list = new ArrayList<>();
        char character = '\n';
        char character2 = ':';
        for(int i = 0; i < message.length(); i++){
            if (i == 0){
                int index = message.indexOf(":");
                list.add(new IndexRegistry(0, index));
            }else if(message.charAt(i) == character){
                for (int j=i; j<message.length(); j++){
                    if (message.charAt(j) == character2){
                        list.add(new IndexRegistry(i, j));
                    }
                }
            }
        }

        for (int i=0; i<list.size(); i++){
            IndexRegistry reg = list.get(i);
            System.out.println(reg.getStart() == 0 ? reg.getStart() : reg.getStart()+1);
            System.out.println(reg.getEnd());
            System.out.println(" ");
            String result = message.substring(reg.getStart() == 0 ? reg.getStart() : reg.getStart()+1, reg.getEnd());
            String resText = "";
            if (!(i+1 >= list.size())) {
                resText = message.substring(reg.getEnd()+1, list.get(i + 1).getStart());
            }
            else{
                resText = message.substring(reg.getEnd()+1);
            }
            System.out.println(result);
            System.out.println("Message: " + resText);
        }
    }
}
