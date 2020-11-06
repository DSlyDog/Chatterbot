package net.whispwriting.chatterbot.proxy;

import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.chatterbot.utils.JFile;
import net.whispwriting.chatterbot.utils.SQLUtil;

public class Proxy {

    private String name;
    private String avatarURL = ".";
    private JFile aliasFile;
    private String ownerID;
    private String[] tag;
    private SQLUtil sql;

    public Proxy(String name, String ownerID, String avatar, String[] tag, SQLUtil sql){
        this.name = name;
        this.ownerID = ownerID;
        this.avatarURL = avatar;
        this.tag = tag;
        this.sql = sql;
    }

    public Proxy(String name, String ownerID, String[] tag, SQLUtil sql){
        this.name = name;
        this.ownerID = ownerID;
        this.tag = tag;
        this.sql = sql;
    }

    public boolean save(TextChannel channel){
        return sql.insert(name, ownerID, avatarURL, getTagString(), channel);
    }

    public boolean setAvatarURL(String avatarURL, TextChannel channel){
        this.avatarURL = avatarURL;
        return sql.update(ownerID, "avatar", avatarURL, name, channel);
    }

    public boolean setTag(String tag[], TextChannel channel){
        this.tag = tag;
        return sql.update(ownerID, "tag", getTagString(), name, channel);
    }

    public String getName(){
        return name;
    }

    public boolean setName(String name, TextChannel channel){
        String oldName = this.name;
        this.name = name;
        return sql.update(ownerID, "name", name, oldName, channel);
    }

    public String getAvatarURL(){
        return avatarURL;
    }

    public String getOwnerID(){
        return ownerID;
    }

    public String[] getTag(){
        return tag;
    }

    public String getTagString(){
        String tagStr;
        if (tag.length == 2)
            tagStr = tag[0] + "tag" + tag[1];
        else
            tagStr = tag[0] + "tag";
        return tagStr;
    }

    public String tagStart(){
        return tag[0];
    }

    public String tagEnd(){
        if (tag.length == 2)
            return tag[1];
        else
            return "";
    }

    @Override
    public String toString(){
        return "Name: " + name + "\n" + "Owner ID: " + ownerID + "\n";
    }

}
