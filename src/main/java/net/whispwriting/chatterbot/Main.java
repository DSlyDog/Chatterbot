package net.whispwriting.chatterbot;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class Main {

    private static boolean listen = true;

    public static void main(String[] args){
        if (args.length < 1){
            System.err.println("You must give the token as an argument");
            return;
        }

        Chatterbot bot = new Chatterbot();
        try {
            bot.init(args[0]);
        }catch(LoginException e){
            System.err.println("The bot could not log in");
            e.printStackTrace();
            return;
        }
        CommandDelegate.registerCommands(bot);

        listen(bot);
    }

    public static void listen(Chatterbot bot) {
        Scanner sc = new Scanner(System.in);
        while (listen){
            String line = sc.nextLine();
            commandLineCmd(line, bot);
        }
    }

    private static void commandLineCmd(String cmd, Chatterbot bot){
        switch (cmd){
            case "stop":
                System.out.println("Shutting down bot...");
                listen = false;
                bot.shutdown();
                break;
            case "listGuilds":
                System.out.println("Listing guilds...");
                bot.listGuilds();
                break;
        }
    }
}
