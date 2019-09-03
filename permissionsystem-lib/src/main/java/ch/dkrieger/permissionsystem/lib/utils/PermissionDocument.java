package ch.dkrieger.permissionsystem.lib.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 16:29
 *  
 */

public class PermissionDocument {

    private static Gson GSON = new Gson();
    private static JsonParser PARSER = new JsonParser();

    private JsonObject data;


    public PermissionDocument(String name){
        this.data = new JsonObject();
        setName(name);
    }
    
    public PermissionDocument(UUID uuid, String name){
        this.data = new JsonObject();
        setName(name);
    }
    
    public PermissionDocument(JsonObject data){
        this.data = data;
    }
    
    public String getName(){
        return getString("data_intern_name");
    }
    
    public PermissionDocument setName(String name){
        if(name == null) return this;
        return append("data_intern_name",name);
    }
    
    public JsonObject getJsonObject(){
        return data;
    }
    
    public String getString(String key){
        if(!this.data.has(key)) return null;
        return this.data.get(key).getAsString();
    }
    
    public int getInt(String key){
        if(!this.data.has(key)) return 0;
        return this.data.get(key).getAsInt();
    }
    
    public long getLong(String key){
        if(!this.data.has(key)) return 0L;
        return this.data.get(key).getAsLong();
    }
    
    public boolean getBoolean(String key){
        if(!this.data.has(key)) return false;
        return this.data.get(key).getAsBoolean();
    }
    
    public <T> T getObject(String key,Class<T> classOF){
        if(!this.data.has(key)) return null;
        return GSON.fromJson(this.data.get(key),classOF);
    }
    
    public PermissionDocument append(String key, String value){
        this.data.addProperty(key,value);
        return this;
    }

    public PermissionDocument append(String key, Boolean value){
        this.data.addProperty(key,value);
        return this;
    }

    public PermissionDocument append(String key, Number value){
        this.data.addProperty(key,value);
        return this;
    }

    public PermissionDocument append(String key, Object value){
        this.data.add(key,GSON.toJsonTree(value));
        return this;
    }

    public PermissionDocument remove(String key){
        this.data.remove(key);
        return this;
    }

    public String toJson(){
        return GSON.toJson(data);
    }

    public static PermissionDocument get(String GSONString){
        try {
            InputStreamReader reader = new InputStreamReader(new StringBufferInputStream(GSONString), "UTF-8");
            return new PermissionDocument(PARSER.parse(new BufferedReader(reader)).getAsJsonObject());
        }catch (IOException exception){
            exception.printStackTrace();
        }
        return null;
    }
}
