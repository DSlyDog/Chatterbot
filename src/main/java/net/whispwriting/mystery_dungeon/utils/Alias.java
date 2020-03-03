package net.whispwriting.mystery_dungeon.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class Alias {

    String name;
    String avatarURL = ".";
    JFile aliasFile;
    String ownerID;
    String tag;

    public Alias(String name, String ownerID, boolean registering){
        File path = new File(System.getProperty("user.dir") + "/" + ownerID + "/aliases/");
        if (!path.exists()) {
            boolean successful = path.mkdirs();
            System.out.println(successful);
        }
        File file = new File(System.getProperty("user.dir") + "/" + ownerID + "/aliases/" + name+".json");
        if (file.exists()) {
            aliasFile = new JFile(name, System.getProperty("user.dir") + "/" + ownerID + "/aliases/");
            avatarURL = aliasFile.getString("avatarURL");
            this.name = name;
        }else{
            aliasFile = new JFile(name, System.getProperty("user.dir") + "/" + ownerID + "/aliases/");
            avatarURL = aliasFile.getString("avatarURL");
            this.ownerID = aliasFile.getString("ownerID");
            this.name = name;
            this.ownerID = ownerID;
            System.out.println("built");
        }
    }

    public void save(){
        aliasFile.set("name", name);
        aliasFile.set("avatarURL", avatarURL);
        aliasFile.set("tag", tag);
        try {
            aliasFile.save();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAvatarURL(String avatarURL){
        this.avatarURL = avatarURL;
    }

    public void setOwnerID(String ownerID){
        this.ownerID = ownerID;
    }

    public void setTag(String tag){
        this.tag = tag;
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

    public boolean load(String ownerID){
        aliasFile = new JFile(name, System.getProperty("user.dir") + "/" + ownerID + "/aliases/");
        avatarURL = aliasFile.getString("avatarURL");
        name = aliasFile.getString("name");
        tag = aliasFile.getString("tag");
        if (name.equals("") || tag.equals(""))
            return false;
        return true;
    }

}
