package net.whispwriting.mystery_dungeon.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.mystery_dungeon.Chatterbot;
import net.whispwriting.mystery_dungeon.utils.Alias;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AliasCommand extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String message = event.getMessage().getContentRaw();
        String[] messageParts = message.split(" ");

        if (messageParts[0].equals("!newalias")){
            StringBuilder nameBuilder = new StringBuilder();
            for (int i=1;i<messageParts.length;i++){
                if (i == messageParts.length-1)
                    nameBuilder.append(messageParts[i]);
                else
                    nameBuilder.append(messageParts[i]).append(" ");
            }
            String name = nameBuilder.toString();
            if (name.equals("")){
                event.getChannel().sendMessage("Sorry, I could not create an alias for you. You must give it a name.").queue();
                return;
            }
            Alias alias;
            List<Message.Attachment> images = event.getMessage().getAttachments();
            if (!images.isEmpty()) {
                Message.Attachment avatar = images.get(0);
                String imageUrl = avatar.getUrl();
                alias = new Alias(name, event.getAuthor().getDiscriminator(), imageUrl, name, Chatterbot.sql);
            }else{
                alias = new Alias(name, event.getAuthor().getDiscriminator(), Chatterbot.sql);
            }
            if (alias.save(event.getChannel())) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Chatterbot.talkAsAlias.addNew(name, event.getAuthor().getDiscriminator(), alias);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("New alias, " + name + ", successfully created.").queue();
            }
        }

        else if (messageParts[0].equals("!aliasavatar")){
            StringBuilder nameBuilder = new StringBuilder();
            for (int i=1;i<messageParts.length;i++){
                if (i == messageParts.length-1)
                    nameBuilder.append(messageParts[i]);
                else
                    nameBuilder.append(messageParts[i]).append(" ");
            }
            String name = nameBuilder.toString();
            List<Message.Attachment> attachments = event.getMessage().getAttachments();
            if (attachments.isEmpty()){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("An image must uploaded along with this command.").queue();
                return;
            }
            Message.Attachment avatarImg = attachments.get(0);
            Alias alias = Chatterbot.talkAsAlias.getAliases().get(name + event.getAuthor().getDiscriminator());
            if (alias != null) {
                if (alias.setAvatarURL(avatarImg.getUrl(), event.getChannel())) {
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("Avatar for " + name + " has been updated.").queue();
                }
            }else{
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("You do not have an alias by the name, " + name + ".").queue();
            }

        }else if (messageParts[0].equals("!alias")){
            StringBuilder nameBuilder = new StringBuilder();
            for (int i=1;i<messageParts.length;i++){
                if (i == messageParts.length-1)
                    nameBuilder.append(messageParts[i]);
                else
                    nameBuilder.append(messageParts[i]).append(" ");
            }
            String name = nameBuilder.toString();
            Alias alias = new Alias(name, event.getAuthor().getDiscriminator(), Chatterbot.sql);
            try {
                alias.getName().equals("");
                //MysteryDungeon.talkAsAlias.addAlias(event.getAuthor().getDiscriminator(), alias, event.getChannel());
            }catch(NullPointerException e){
                //
            }

        }

        else if (messageParts[0].equals("!unalias")){
            //MysteryDungeon.talkAsAlias.removeAlias(event.getAuthor().getDiscriminator(), event.getChannel());
        }

        else if (messageParts[0].equals("!aliastag")){
            StringBuilder nameBuilder = new StringBuilder();
            String newTag = "";
            for (int i=1;i<messageParts.length;i++){
                if (i == messageParts.length-1)
                    newTag = messageParts[i];
                else
                    nameBuilder.append(messageParts[i]).append(" ");
            }
            String name = nameBuilder.toString();
            name = name.substring(0, name.length()-1);
            Alias alias = Chatterbot.talkAsAlias.getAliases().get(name + event.getAuthor().getDiscriminator());
            if (alias != null) {
                String oldTag = alias.getTag();
                if (alias.setTag(newTag, event.getChannel())) {
                    Chatterbot.talkAsAlias.updateTag(oldTag, newTag, event.getAuthor().getDiscriminator());
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("Tag for " + name + " has been updated to " + newTag + ".").queue();
                }
            }else{
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("You do not have an alias by the name, " + name + ".").queue();
            }

        }
    }

}
