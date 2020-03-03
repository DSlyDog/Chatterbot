package net.whispwriting.mystery_dungeon.event;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.utils.AccountList;
import net.whispwriting.mystery_dungeon.utils.Alias;
import net.whispwriting.mystery_dungeon.utils.IndexRegistry;

import java.io.File;
import java.util.*;

public class TalkAsAlias extends ListenerAdapter {

    private Map<String, Alias> aliases = new HashMap<>();

    public void setAliasSet(Map<String, Alias> aliases){
        this.aliases = aliases;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getMessage().getContentRaw().contains("!alias") || event.getMessage().getContentRaw().contains("!new")) {
            if(event.getAuthor().isBot())
                event.getMessage().delete().queue();
            return;
        }

        if(!event.getMessage().getContentRaw().contains(":"))
            return;

        String message = event.getMessage().getContentRaw();

        List<IndexRegistry> list = new ArrayList<>();
        char character = '\n';
        char character2 = ':';
        for(int i = 0; i < message.length(); i++){
            if (i == 0){
                int index = message.indexOf(":");
                list.add(new IndexRegistry(0, index));
                i = index;
            }else if(message.charAt(i) == character2){
                for (int j=i; j>i-20; j--){
                    if (message.charAt(j) == character){
                        list.add(new IndexRegistry(j, i));
                        break;
                    }
                }
            }
        }
        for (int i=0; i<list.size(); i++){
            IndexRegistry reg = list.get(i);
            String result = message.substring(reg.getStart() == 0 ? reg.getStart() : reg.getStart()+1, reg.getEnd());
            String resText = "";
            if (!(i+1 >= list.size())) {
                resText = message.substring(reg.getEnd()+1, list.get(i + 1).getStart());
            }
            else{
                resText = message.substring(reg.getEnd()+1);
            }

            Alias alias = aliases.get(result+event.getAuthor().getDiscriminator());
            if (alias != null) {
                alias.load(event.getAuthor().getDiscriminator());
                Webhook pmdHook = null;
                List<Webhook> hooks = event.getChannel().retrieveWebhooks().complete();
                for (Webhook hook : hooks) {
                    String hookName = hook.getName();
                    if (hookName.equals("PMDHook")) {
                        pmdHook = hook;
                        break;
                    }
                }
                if (pmdHook == null) {
                    event.getChannel().createWebhook("PMDHook").queue();
                    hooks = event.getChannel().retrieveWebhooks().complete();
                    for (Webhook hook : hooks) {
                        if (hook.getName().equals("PMDHook"))
                            pmdHook = hook;
                    }
                }
                WebhookClientBuilder builder = new WebhookClientBuilder(pmdHook.getUrl()); // or id, token
                builder.setThreadFactory((job) -> {
                    Thread thread = new Thread(job);
                    thread.setName("Hello");
                    thread.setDaemon(true);
                    return thread;
                });
                builder.setWait(true);
                WebhookClient client = builder.build();
                WebhookMessageBuilder mBuilder = new WebhookMessageBuilder();
                mBuilder.setUsername(alias.getName());
                if (!alias.getAvatarURL().equals("."))
                    mBuilder.setAvatarUrl(alias.getAvatarURL());
                mBuilder.setContent(resText);
                client.send(mBuilder.build());
            }else{
                return;
            }
        }
        event.getMessage().delete().queue();
    }

    public void updateTag(String currentTag, String newTag, String delimiter){
        Alias alias = aliases.get(currentTag+delimiter);
        alias.load(delimiter);
        aliases.remove(currentTag+delimiter);
        aliases.put(newTag+delimiter, alias);
    }

    public void addNew(String tag, String delimiter, Alias alias){
        alias.load(delimiter);
        aliases.put(tag+delimiter, alias);
    }

}
