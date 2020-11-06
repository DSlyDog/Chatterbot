package net.whispwriting.chatterbot.proxy;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.whispwriting.chatterbot.Chatterbot;

import java.util.ArrayDeque;
import java.util.Queue;

public class ProxyBuilder {

    public static Queue<WebhookMessageBuilder> build(GuildMessageReceivedEvent event){
        Queue<WebhookMessageBuilder> hookMessages = new ArrayDeque<>();
        String[] message = event.getMessage().getContentRaw().split("\n");
        int tagStart = 0;
        int tagEnd = 0;
        boolean startFound = false;
        boolean foundSecondStart = false;
        Proxy proxyToUse = null;
        for (int i=0; i < message.length; i++){
            for (Proxy proxy : Chatterbot.users.get(event.getAuthor().getId())){
                if (message[i].startsWith(proxy.tagStart())){
                    if (!startFound) {
                        startFound = true;
                        proxyToUse = proxy;
                        tagStart = i;
                    }else{
                        tagEnd = i-1;
                        foundSecondStart = true;
                        startFound = false;
                        break;
                    }
                }if (message[i].endsWith(proxy.tagEnd()) && startFound){
                    tagEnd = i;
                    startFound = false;
                    break;
                }else if (i == message.length - 1 && startFound){
                    tagEnd = i;
                    startFound = false;
                    break;
                }
            }
            if (i == 0 && proxyToUse == null){
                return new ArrayDeque<>();
            }
            StringBuilder builder = new StringBuilder();
            if (tagStart == tagEnd){
                builder.append(message[i]);
            }
            for (int j=tagStart; j< tagEnd; j++){
                builder.append(message[i]).append("\n");
            }
            String currentMessage = builder.toString();
            currentMessage = currentMessage.replace(proxyToUse.tagStart(), "");
            currentMessage = currentMessage.replace(proxyToUse.tagEnd(), "");
            WebhookMessageBuilder mBuilder = new WebhookMessageBuilder();
            mBuilder.setUsername(proxyToUse.getName());
            if (!proxyToUse.getAvatarURL().equals("."))
                mBuilder.setAvatarUrl(proxyToUse.getAvatarURL());
            mBuilder.setContent(currentMessage);
            hookMessages.add(mBuilder);
            if (foundSecondStart) {
                i--;
            }
        }
        return hookMessages;
    }
}
