package net.whispwriting.chatterbot;

import net.whispwriting.chatterbot.commands.*;

public class CommandDelegate {

    public static void registerCommands(Chatterbot bot){
        bot.registerCommand(Chatterbot.prefix + "register", new Register());
        bot.registerCommand(Chatterbot.prefix + "avatar", new Avatar());
        bot.registerCommand(Chatterbot.prefix + "tag", new Tag());
        bot.registerCommand(Chatterbot.prefix + "rename", new Rename());
        //bot.registerCommand(Chatterbot.prefix + "id", new GetID());
    }
}
