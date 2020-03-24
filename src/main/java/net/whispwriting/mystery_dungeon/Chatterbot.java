package net.whispwriting.mystery_dungeon;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.whispwriting.mystery_dungeon.commands.*;
import net.whispwriting.mystery_dungeon.event.TalkAsAlias;
import net.whispwriting.mystery_dungeon.utils.Alias;
import net.whispwriting.mystery_dungeon.utils.SQLUtil;

import javax.security.auth.login.LoginException;
import java.util.*;

public class Chatterbot {

    public static JDA jda;
    public static String prefix = "!";
    public static List<String> narrators = new ArrayList<>();
    public static List<String> narrating = new ArrayList<>();
    public static TalkAsAlias talkAsAlias = new TalkAsAlias();
    public static SQLUtil sql = new SQLUtil();
    public static Map<String, Alias> aliases = new HashMap<>();

    public static void main(String[] args) throws LoginException {
        jda = new JDABuilder(AccountType.BOT).setToken("NjQxNzUzMTUyNjMwMjkyNTQx.XnqGfw.v4tTzxgUpM6P_5TtECdpPixHHKA").build();
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
        talkAsAlias.setAliasSet(sql.load(aliases));
    }

}
