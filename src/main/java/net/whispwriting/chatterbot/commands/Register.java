package net.whispwriting.chatterbot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.chatterbot.Chatterbot;
import net.whispwriting.chatterbot.ErrorMessages;
import net.whispwriting.chatterbot.proxy.Proxy;

import java.util.ArrayList;
import java.util.List;

public class Register implements Command{

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        if (args.length >= 2){
            if (!args[1].contains("tag")){
                ErrorMessages.noTag(channel);
                return;
            }
            if (Chatterbot.getProxy(sender, args[0]) != null){
                Chatterbot.sendMessage("You already have a proxy with that name.", channel);
                return;
            }
            String[] tag = args[1].split("tag");
            Proxy proxy;
            if (!attachments.isEmpty()) {
                Message.Attachment avatar = attachments.get(0);
                String imageUrl = avatar.getUrl();
                proxy = new Proxy(args[0], sender.getUser().getId(), imageUrl, tag, Chatterbot.sql);
            }else{
                proxy = new Proxy(args[0], sender.getUser().getId(), tag, Chatterbot.sql);
            }
            if (proxy.save(channel)) {
                if (Chatterbot.users.containsKey(sender.getUser().getId())) {
                    Chatterbot.users.get(sender.getUser().getId()).add(proxy);
                }else{
                    List<Proxy> proxies = new ArrayList<>();
                    proxies.add(proxy);
                    Chatterbot.users.put(sender.getUser().getId(), proxies);
                }
                Chatterbot.sendMessage("New proxy, " + args[0] + ", successfully created.", channel);
            }
        }else if (args.length == 1){
            ErrorMessages.notEnoughArguments("Tag", "c!register help", channel);
        }else{
            //call command help
        }
    }
}
