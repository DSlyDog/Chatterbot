package net.whispwriting.chatterbot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.chatterbot.Chatterbot;
import net.whispwriting.chatterbot.ErrorMessages;
import net.whispwriting.chatterbot.proxy.Proxy;

import java.util.List;

public class Tag implements Command{
    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        if (args.length == 1) {
            Proxy proxy = Chatterbot.getProxy(sender, args[0]);
            if (proxy == null){
                ErrorMessages.proxyNotFound(args[0], channel);
                return;
            }
            String[] tag = proxy.getTag();
            String tagStr;
            if (tag.length == 2){
                tagStr = tag[0] + "tag" + tag[1];
            }else{
                tagStr = tag[0] + "tag";
            }
            Chatterbot.sendMessage("Tag for " + proxy.getName() + ": " + tagStr, channel);
        }else if (args.length == 2){
            Proxy proxy = Chatterbot.getProxy(sender, args[0]);
            if (proxy == null){
                ErrorMessages.proxyNotFound(args[0], channel);
                return;
            }
            if (!args[1].contains("tag")) {
                ErrorMessages.noTag(channel);
                return;
            }
            String[] tag = args[1].split("tag");
            if (proxy.setTag(tag, channel)){
                Chatterbot.sendMessage("Tag successfully updated", channel);
            }
        }else{
            //call command help
        }
    }
}
