package net.whispwriting.chatterbot.commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.chatterbot.Chatterbot;
import net.whispwriting.chatterbot.ErrorMessages;
import net.whispwriting.chatterbot.proxy.Proxy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Avatar implements Command{
    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        if (args.length == 1) {
            Proxy proxy = Chatterbot.getProxy(sender, args[0]);
            if (proxy == null){
                ErrorMessages.proxyNotFound(args[0], channel);
                return;
            }
            if (attachments.size() == 0) {
                try {
                    URL url = new URL(proxy.getAvatarURL());
                    MessageBuilder builder = new MessageBuilder();
                    builder.append(url);
                    Chatterbot.sendMessage(builder, channel);
                    return;
                } catch (MalformedURLException e) {
                    Chatterbot.sendMessage("An avatar has not been set for proxy: " + proxy.getName(), channel);
                    return;
                }

            }
            Message.Attachment attachment = attachments.get(0);
            if (proxy.setAvatarURL(attachment.getUrl(), channel)) {
                Chatterbot.sendMessage("Avatar successfully updated.", channel);
            }
        }else if (args.length == 2){
            Proxy proxy = Chatterbot.getProxy(sender, args[0]);
            if (proxy == null){
                ErrorMessages.proxyNotFound(args[0], channel);
                return;
            }
            if(proxy.setAvatarURL(args[1], channel)){
                Chatterbot.sendMessage("Avatar successfully updated.", channel);
            }
        }else{
            Chatterbot.sendMessage("You must provide the name of a proxy.", channel);
        }
    }
}
