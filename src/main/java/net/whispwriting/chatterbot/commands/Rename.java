package net.whispwriting.chatterbot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.chatterbot.Chatterbot;
import net.whispwriting.chatterbot.ErrorMessages;
import net.whispwriting.chatterbot.proxy.Proxy;

import java.util.List;

public class Rename implements Command{
    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        if (args.length == 2){
            Proxy proxy = Chatterbot.getProxy(sender, args[0]);
            if (proxy == null){
                ErrorMessages.proxyNotFound(args[0], channel);
                return;
            }
            if (proxy.setName(args[1], channel)){
                Chatterbot.sendMessage("Name successfully updated", channel);
            }
        }else if (args.length == 1){
            ErrorMessages.notEnoughArguments("new name", "c!rename help", channel);
        }else{
            //call command help
        }
    }
}
