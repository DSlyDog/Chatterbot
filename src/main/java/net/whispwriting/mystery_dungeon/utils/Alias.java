package net.whispwriting.mystery_dungeon.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class Alias {

    private String name;
    private String avatarURL = ".";
    private JFile aliasFile;
    private String ownerID;
    private String tag;
    private SQLUtil sql;

    public Alias(String name, String ownerID, SQLUtil sql){
        this.name = name;
        this.ownerID = ownerID;
        this.tag = name;
        this.sql = sql;
    }

    public Alias(String name, String ownerID, String avatar, String tag, SQLUtil sql){
        this.name = name;
        this.ownerID = ownerID;
        this.avatarURL = avatar;
        this.tag = tag;
        this.sql = sql;
    }

    public boolean save(TextChannel channel){
        return sql.insert(name, ownerID, avatarURL, tag, channel);
    }

    public boolean setAvatarURL(String avatarURL, TextChannel channel){
        this.avatarURL = avatarURL;
        return sql.update(ownerID, "avatar", avatarURL, name, channel);
    }

    public boolean setTag(String tag, TextChannel channel){
        this.tag = tag;
        return sql.update(ownerID, "tag", tag, name, channel);
    }

    public String getName(){
        return name;
    }

    public String getAvatarURL(){
        return avatarURL;
    }

    public String getOwnerID(){
        return ownerID;
    }

    public String getTag(){
        return tag;
    }

    @Override
    public String toString(){
        return name + "\n" + ownerID + "\n";
    }

}
