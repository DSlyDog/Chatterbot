package net.whispwriting.chatterbot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {

    private Map<String, Command> commandMap = new HashMap<>();

    public void registerCommand(String label, Command commandHandler){
        commandMap.put(label, commandHandler);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (!event.getAuthor().isBot()) {
            String[] message = event.getMessage().getContentRaw().split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            for (int i=0; i<message.length; i++){
                if (message[i].startsWith("\"")){
                    message[i] = message[i].substring(1);
                }
                if (message[i].endsWith("\"")){
                    message[i] = message[i].substring(0, message[i].length()-1);
                }
            }
            if (commandMap.containsKey(message[0])){
                Member member = event.getMember();
                String label = message[0];
                String[] finalMessage = new String[message.length-1];
                for (int i=1; i<message.length; i++){
                    finalMessage[i-1] = message[i];
                }
                commandMap.get(message[0]).onCommand(member, label, finalMessage, event.getMessage().getAttachments(),event.getChannel());
            }
        }
    }
}
