package net.whispwriting.chatterbot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public interface Command {

    void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel);
}
