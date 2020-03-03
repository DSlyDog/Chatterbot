package net.whispwriting.mystery_dungeon.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JFile {

    private FileWriter writer;
    private FileReader reader;
    private File file;
    private JSONParser parser = new JSONParser();
    private JSONObject out = new JSONObject();

    public JFile(String name, String path) {
        File fPath = new File(path);
        if (!fPath.exists())
            fPath.mkdirs();
        file = new File(fPath, name + ".json");
    }

    public void set(String path, Object object){
        out.put(path, object);
    }

    public String getString(String path){
        try {
            reader = new FileReader(file);
            JSONObject object = (JSONObject) parser.parse(reader);
            String string = (String) object.get(path);
            reader.close();
            return string;
        }catch (IOException e){
            System.err.println("File read error");
            e.printStackTrace();
            return "";
        }catch(ParseException e){
            System.err.println("parse failed");
            e.printStackTrace();
            return "";
        }
    }

    public int getInt(String path){
        try {
            reader = new FileReader(file);
            JSONObject object = (JSONObject) parser.parse(reader);
            int num = (int) object.get(path);
            reader.close();
            return num;
        }catch (Exception e){
            System.err.println("File read error");
            e.printStackTrace();
            return 0;
        }
    }

    public double getDouble(String path){
        try {
            reader = new FileReader(file);
            JSONObject object = (JSONObject) parser.parse(reader);
            double num = (double) object.get(path);
            reader.close();
            return num;
        }catch (Exception e){
            System.err.println("File read error");
            e.printStackTrace();
            return 0;
        }
    }

    public List<String> getStringList(String path){
        try {
            reader = new FileReader(file);
            JSONObject object = (JSONObject) parser.parse(reader);
            JSONArray list = (JSONArray) object.get(path);
            List<String> strings = new ArrayList<>();
            for (Object obj : list){
                String str = (String) obj;
                strings.add(str);
            }
            reader.close();
            return strings;
        }catch(NullPointerException e){
            return null;
        } catch (Exception e){
            System.err.println("File read error");
            e.printStackTrace();
            return null;
        }
    }

    public void save() throws IOException{
        writer = new FileWriter(file);
        writer.write(out.toString());
        writer.flush();
        writer.close();
    }

}
