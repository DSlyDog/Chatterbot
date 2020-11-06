package net.whispwriting.chatterbot;

import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.chatterbot.Chatterbot;

public class ErrorMessages {

    public static void noTag(TextChannel channel){
        Chatterbot.sendMessage("No 'tag' found. Please enter the word 'tag' surrounded by any other characters " +
                "in the last part of the command. \nThis is how the bot knows when it should replace a message.", channel);
    }

    public static void proxyNotFound(String name, TextChannel channel){
        Chatterbot.sendMessage("You do not have a proxy named '" + name + "' registered.", channel);
    }

    public static void notEnoughArguments(String missingArgument, String objectOfFailure, TextChannel channel){
        Chatterbot.sendMessage(missingArgument + " is missing. try ``" + objectOfFailure + "`` for usage help", channel);
    }
}
