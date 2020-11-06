package net.whispwriting.chatterbot.commands;

import net.dv8tion.jda.api.entities.*;
import net.whispwriting.chatterbot.Chatterbot;

import java.util.List;

public class GetID implements Command{
    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        String[] tag = args[0].split("#");
        System.out.println(tag[0]);
        System.out.println(tag[1]);
        User user = Chatterbot.jda.getUserByTag(tag[0], tag[1]);
        System.out.println(user.getId());
    }
}
