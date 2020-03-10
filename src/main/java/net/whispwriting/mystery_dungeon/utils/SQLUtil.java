package net.whispwriting.mystery_dungeon.utils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.mystery_dungeon.Chatterbot;

import java.nio.channels.Channel;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQLUtil {

    private Connection connection;

    public SQLUtil(){
        connect();
    }

    private void connect(){
        try{
            synchronized (this){
                if (connection == null || connection.isClosed()) {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://whispwriting.net:3306/wynracom_aliases", "wynracom_Whisp", "Whisp#1Fox");
                    System.out.println("Connected!");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Failed to connect to database");
        }catch (ClassNotFoundException f){
            f.printStackTrace();
            System.err.println("Failed to connect to database");
        }
    }

    public boolean insert(String name, String owner, String avatar, String tag, TextChannel channel){
        connect();
        if (!tableExists(owner))
            createTable(owner);
        try {
            PreparedStatement statement = connection.prepareStatement("insert into t_" + owner + " values('" + name + "', '" + avatar + "', '" + tag + "')");
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            channel.sendMessage("Failed to save alias. Please inform a Chatterbot dev.").queue();
            return false;
        }
    }

    public Map<String, Alias> load(Map<String, Alias> aliases){
        connect();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from aliases");
            ResultSet results = statement.executeQuery();
            while (results.next()){
                String owner = results.getString("id");
                PreparedStatement statement2 = connection.prepareStatement("select * from t_" + owner);
                ResultSet results2 = statement2.executeQuery();
                while (results2.next()) {
                    String name = results2.getString("name");
                    String avatar = results2.getString("avatar");
                    String tag = results2.getString("tag");
                    Alias alias = new Alias(name, owner, avatar, tag, Chatterbot.sql);
                    aliases.put(tag + owner, alias);
                    aliases.put(name + owner, alias);
                }
            }
            return aliases;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to load aliases");
            return null;
        }
    }

    public boolean tableExists(String name){
        connect();
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "t_" + name, null);
            if (tables.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createTable(String name){
        connect();
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE t_" + name + " (name VARCHAR(255), avatar VARCHAR(255), tag VARCHAR(255), " +
                    "constraint t_pk PRIMARY KEY (name))");
            statement.execute();
            statement = connection.prepareStatement("insert into aliases values('" + name + "')");
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void convert(Map<String, Alias> aliases){
        connect();
        for (Map.Entry<String, Alias> alias : aliases.entrySet()) {
            Alias a = alias.getValue();
            if (!tableExists(a.getOwnerID()))
                createTable(a.getOwnerID());
            PreparedStatement statement;
            try {
                statement = connection.prepareStatement("insert into t_" + a.getOwnerID() + " values('" + a.getName() + "', '" + a.getAvatarURL() + "', '" + a.getTag() + "')");
                System.out.println(statement);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean update(String owner, String field, String newFieldData, String name, TextChannel channel){
        connect();
        if (!tableExists(owner))
            createTable(owner);
        try {
            PreparedStatement statement = connection.prepareStatement("update t_" + owner + " set " + field + "='" +
                    newFieldData + "' where name='" + name + "'");
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            channel.sendMessage("Failed to update alias. Please inform a Chatterbot dev.").queue();
            return false;
        }
    }
}
