package net.whispwriting.mystery_dungeon.utils;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountList {

    private ThunderFile file;
    private List<String> delimiters;

    public void load(){
        try {
            file = Thunderbolt.load("accounts", System.getProperty("user.dir")+"/accounts/");
        } catch (FileLoadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            delimiters = file.getStringList("accounts");
        }catch(JSONException e){
            e.printStackTrace();
            List<String> newList = new ArrayList<>();
            newList.add("0000");
            file.set("accounts", newList);
            try {
                file.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            delimiters = newList;
        }
    }

    public void addAccount(String delimiter){
        if (!delimiters.contains(delimiter))
            delimiters.add(delimiter);
        file.set("accounts", delimiters);
        try {
            file.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAccounts(){
        return delimiters;
    }

}
