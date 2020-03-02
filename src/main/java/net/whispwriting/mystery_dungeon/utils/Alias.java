package net.whispwriting.mystery_dungeon.utils;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import netscape.javascript.JSException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class Alias {

    String name;
    String avatarURL = ".";
    ThunderFile aliasFile;
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
            aliasFile = new ThunderFile(name, System.getProperty("user.dir") + "/" + ownerID + "/aliases/");
            try {
                avatarURL = aliasFile.getString("avatarURL");
            } catch (JSONException e) {
                // alias was not preexisting
            }
            this.name = name;
        }else{
            aliasFile = new ThunderFile(name, System.getProperty("user.dir") + "/" + ownerID + "/aliases/");
            try {
                avatarURL = aliasFile.getString("avatarURL");
                this.ownerID = aliasFile.getString("ownerID");
            } catch (JSONException e) {
                // alias was not preexisting
            }
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

    public void close(){
        Thunderbolt.unload(name);
    }

    public boolean load(String ownerID){
        try {
            aliasFile = Thunderbolt.load(name, System.getProperty("user.dir") + "/" + ownerID + "/aliases/");
            avatarURL = aliasFile.getString("avatarURL");
            name = aliasFile.getString("name");
            tag = aliasFile.getString("tag");
            return true;
        } catch (FileLoadException e) {
            //e.printStackTrace();
            return true;
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }
    }

}
