package net.whispwriting.chatterbot.utils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.chatterbot.Chatterbot;
import net.whispwriting.chatterbot.proxy.Proxy;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://whispwriting.net:3306/wynracom_chatterbot", "wynracom_Whisp", "Whisp#1Fox");
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
            Chatterbot.sendMessage("Failed to save proxy. Please inform a Chatterbot dev.", channel);
            return false;
        }
    }

    public Map<String, List<Proxy>> load(){
        connect();
        try {
            Map<String, List<Proxy>> users = new HashMap<>();
            PreparedStatement statement = connection.prepareStatement("select * from users");
            ResultSet results = statement.executeQuery();
            while (results.next()){
                String owner = results.getString("id");
                PreparedStatement statement2 = connection.prepareStatement("select * from t_" + owner);
                ResultSet results2 = statement2.executeQuery();
                List<Proxy> proxies = new ArrayList<>();
                while (results2.next()) {
                    String name = results2.getString("name");
                    String avatar = results2.getString("avatar");
                    String tagStr = results2.getString("tag");
                    String[] tag = tagStr.split("tag");
                    Proxy proxy = new Proxy(name, owner, avatar, tag, Chatterbot.sql);
                    proxies.add(proxy);
                }
                users.put(owner, proxies);
            }
            return users;
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
            statement = connection.prepareStatement("insert into users values('" + name + "')");
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void convert(Map<String, Proxy> aliases){
        connect();
        for (Map.Entry<String, Proxy> alias : aliases.entrySet()) {
            Proxy a = alias.getValue();
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
            Chatterbot.sendMessage("Failed to update proxy. Please inform a Chatterbot dev.", channel);
            return false;
        }
    }
}
