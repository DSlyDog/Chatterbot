package net.whispwriting.mystery_dungeon.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.game.NPC;
import net.whispwriting.mystery_dungeon.game.Player;
import net.whispwriting.mystery_dungeon.game.Pokemon;

import java.util.*;

public class InGameCommands extends ListenerAdapter {

    private List<Player> players;
    private List<NPC> npcs = new ArrayList<>();
    private String narrator;
    private MessageChannel channel;
    private TimerTask task;
    private Timer timer;
    private Pokemon attackerFinal;
    private Pokemon monFinal;

    public InGameCommands(List<Player> players, String narrator, MessageChannel channel){
        this.players = players;
        this.narrator = narrator;
        this.channel = channel;

        for (Player player : players){
            player.init();
        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getChannel() != channel){
            return;
        }
        boolean isInInstance = false;
        if (event.getAuthor().getName().equals(narrator)){
            isInInstance = true;
        }

        for (Player player : players){
            if (event.getAuthor().getName().equals(player.name())){
                if (player.isFainted()){
                    event.getMessage().delete().queue();
                    channel.sendTyping().queue();
                    channel.sendMessage("Sorry, " + player.name() + ", you are fainted.").queue();
                    return;
                }
                isInInstance = true;
            }
        }

        if (!isInInstance){
            return;
        }else {
            String[] message = event.getMessage().getContentRaw().split(" ");
            if (message[0].equals("!attack")) {
                if (message.length < 3){
                    channel.sendTyping().queue();
                    channel.sendMessage("Sorry, you must tell me an opponent to attack and your move.").queue();
                }else{
                    Pokemon attacker = null;
                    for (Player player : players){
                        if (event.getAuthor().getName().equals(player.name())){
                            attacker = player;
                        }
                    }
                    if (message.length == 4) {
                        for (NPC npc : npcs) {
                            if (message[3].equals(npc.species())) {
                                attacker = npc;
                            }
                        }
                    }
                    Pokemon mon = null;
                    for (Player player : players){
                        if (message[1].equals(player.name())){
                            mon = player;
                        }
                    }
                    for (NPC npc : npcs){
                        if (message[1].equals(npc.species())){
                            mon = npc;
                        }
                    }

                    if (mon == null || attacker == null){
                        channel.sendTyping().queue();
                        channel.sendMessage("Sorry, either the Pokemon being attacked or the attacker could not" +
                                " be found.").queue();
                        return;
                    }else{
                        attackerFinal = attacker;
                        monFinal = mon;
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                double damage = attackerFinal.attack(monFinal);
                                System.out.println(damage);
                                channel.sendTyping().queue();
                                channel.sendMessage("" + attackerFinal + " dealt " + damage + " damage to " + monFinal).queue();
                                String result = monFinal.damage(damage);
                                if (result.equals("FAINTED")){
                                    channel.sendTyping().queue();
                                    if (monFinal instanceof Player){
                                        channel.sendMessage(((Player) monFinal).name() + " has fainted.").queue();
                                    }else{
                                        channel.sendMessage(((NPC) monFinal).species() + " has fainted.").queue();
                                    }
                                }
                            }
                        };
                        event.getMessage().delete().queue();
                        channel.sendTyping().queue();
                        channel.sendMessage(attackerFinal + " used " + message[2] + " on " + monFinal + ".").queue();
                        timer = new Timer();
                        timer.schedule(task, 3000);
                    }
                }
            }else if (message[0].equals("!counter")){
                if (timer != null) {
                    if (monFinal.counter()) {
                        timer.cancel();
                        timer = null;
                        channel.sendTyping().queue();
                        channel.sendMessage(monFinal + " successfully countered.").queue();
                        double damageAttacker = monFinal.attack(attackerFinal);
                        double damageMon = attackerFinal.attack(monFinal);
                        String resultAttacker = attackerFinal.damage(damageAttacker / 2);
                        String resultMon = monFinal.damage(damageMon / 2);
                        channel.sendTyping().queue();
                        channel.sendMessage(monFinal + " took " + (damageMon / 2) + " damage.").queue();
                        if (resultMon.equals("FAINTED"))
                            channel.sendMessage(monFinal + " fainted.").queue();
                        channel.sendMessage(attackerFinal + " took " + (damageAttacker / 2) + " damage.").queue();
                        if (resultAttacker.equals("FAINTED"))
                            channel.sendMessage(attackerFinal + " fainted.").queue();
                    }
                    channel.sendMessage(monFinal + " failed to counter.").queue();
                }
            }
        }
    }

}
