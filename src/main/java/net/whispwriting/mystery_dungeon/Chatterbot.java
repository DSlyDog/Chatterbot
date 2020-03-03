package net.whispwriting.mystery_dungeon;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.whispwriting.mystery_dungeon.commands.*;
import net.whispwriting.mystery_dungeon.event.TalkAsAlias;
import net.whispwriting.mystery_dungeon.utils.AccountList;
import net.whispwriting.mystery_dungeon.utils.Alias;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.*;

public class Chatterbot {

    public static JDA jda;
    public static String prefix = "!";
    public static List<String> narrators = new ArrayList<>();
    public static List<String> narrating = new ArrayList<>();
    public static AccountList accountList = new AccountList();
    public static TalkAsAlias talkAsAlias = new TalkAsAlias();

    public static void main(String[] args) throws LoginException {
        jda = new JDABuilder(AccountType.BOT).setToken("NjQxNzUzMTUyNjMwMjkyNTQx.Xl3cNQ.2e5epkyzKgYexWFMqSKnKPAL8d8").build();
        jda.getPresence().setActivity(Activity.playing("A Roleplay Game"));
        //jda.addEventListener(new HelpCommand());
        //jda.addEventListener(new CreateHelpCommand());
        //jda.addEventListener(new CreateCommand());
        //jda.addEventListener(new StartCommand());
        jda.addEventListener(new NarrateCommand());
        jda.addEventListener(new NarratingCommand());
        //jda.addEventListener(new CreateCharacterCommand());
        //jda.addEventListener(new CharacterInfoCommand());
        jda.addEventListener(new AliasCommand());
        jda.addEventListener(talkAsAlias);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                accountList.load();
                List<String> delimiters = accountList.getAccounts();
                Map<String, Alias> aliases = new HashMap<>();
                for (String delimiter : delimiters) {
                    File path = new File(System.getProperty("user.dir") + "/"+delimiter+"/aliases");
                    if (path.isDirectory()) {
                        File[] files = path.listFiles();
                        for (File file : files) {
                            String aliasStr = file.getName().substring(0, file.getName().indexOf("."));
                            Alias alias = new Alias(aliasStr, delimiter, false);
                            alias.load(delimiter);
                            aliases.put(alias.getTag()+delimiter, alias);
                        }
                    }
                }
                talkAsAlias.setAliasSet(aliases);
            }
        };
        timer.schedule(task, 3000);
    }

}
