package net.whispwriting.mystery_dungeon.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountList {

    private JFile file;
    private List<String> delimiters;

    public void load(){
        file = new JFile("accounts", System.getProperty("user.dir")+"/accounts/");
        delimiters = file.getStringList("accounts");
        if (delimiters == null) {
            List<String> newList = new ArrayList<>();
            newList.add("0000");
            file.set("accounts", newList);
            delimiters = newList;
            try {
                file.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
