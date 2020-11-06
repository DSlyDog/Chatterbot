package net.whispwriting.chatterbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.whispwriting.chatterbot.commands.*;
import net.whispwriting.chatterbot.proxy.ProxyHandler;
import net.whispwriting.chatterbot.proxy.Proxy;
import net.whispwriting.chatterbot.utils.SQLUtil;

import javax.security.auth.login.LoginException;
import java.util.*;

public class Chatterbot {

    public static JDA jda;
    private ProxyHandler proxyHandler = new ProxyHandler();
    private CommandHandler handler = new CommandHandler();
    public static String prefix = "c!";
    public static SQLUtil sql = new SQLUtil();
    public static Map<String, List<Proxy>> users;

    public void init(String token)throws LoginException {
        jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_PRESENCES)
                .build();
        jda.getPresence().setActivity(Activity.playing("A Roleplay Game"));
        jda.addEventListener(handler);
        jda.addEventListener(proxyHandler);
        users = sql.load();
    }

    public void registerCommand(String label, Command command){
        handler.registerCommand(label, command);
    }

    public void listGuilds(){
        for (Guild guild : jda.getGuilds()){
            System.out.println(guild.getName());
        }
    }

    public void shutdown(){
        jda.shutdown();
    }

    public static Proxy getProxy(Member member, String name){
        for (Proxy proxy : users.get(member.getUser().getId())){
            if (proxy.getName().equals(name)) {
                return proxy;
            }
        }
        return null;
    }

    private static void sendMessage(Message message, TextChannel channel){
        channel.sendTyping().queue();
        try {
            Thread.sleep(100);
            channel.sendMessage(message).queue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(MessageBuilder builder, TextChannel channel){
        sendMessage(builder.build(), channel);
    }

    public static void sendMessage(String message, TextChannel channel){
        MessageBuilder builder = new MessageBuilder();
        builder.setContent(message);
        sendMessage(builder, channel);
    }

}
