package net.whispwriting.chatterbot.proxy;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.chatterbot.Chatterbot;

import java.util.*;

public class ProxyHandler extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (!event.getAuthor().isBot() && !event.getMessage().getContentRaw().contains(Chatterbot.prefix)) {
            Webhook pmdHook = searchHooks(event);
            WebhookClientBuilder builder = new WebhookClientBuilder(pmdHook.getUrl()); // or id, token
            builder.setWait(true);
            WebhookClient client = builder.build();
            Queue<WebhookMessageBuilder> messages = ProxyBuilder.build(event);
            if (!messages.isEmpty()){
                event.getMessage().delete().queue();
            }
            while (!messages.isEmpty()){
                WebhookMessageBuilder mBuilder = messages.poll();
                client.send(mBuilder.build());
            }

        }
    }

    private Webhook searchHooks(GuildMessageReceivedEvent event){
            Webhook pmdHook = hookIter(event);
            if (pmdHook == null){
                event.getChannel().createWebhook("PMDHook").complete();
                pmdHook = hookIter(event);
            }
            return pmdHook;
    }

    private Webhook hookIter(GuildMessageReceivedEvent event){
        List<Webhook> hooks = event.getChannel().retrieveWebhooks().complete();
        Webhook pmdHook = null;
        for (Webhook hook : hooks) {
            String hookName = hook.getName();
            if (hookName.equals("PMDHook")) {
                pmdHook = hook;
                break;
            }
        }
        return pmdHook;
    }
}
