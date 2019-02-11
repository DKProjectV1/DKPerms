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

    public static Gson gson = new Gson();
    public static JsonParser parser = new JsonParser();

    private JsonObject datas;


    public PermissionDocument(String name){
        this.datas = new JsonObject();
        setName(name);
    }
    public PermissionDocument(UUID uuid, String name){
        this.datas = new JsonObject();
        setName(name);
    }
    public PermissionDocument(JsonObject data){
        this.datas = data;
    }
    public String getName(){
        return getString("data_intern_name");
    }
    public PermissionDocument setName(String name){
        if(name == null) return this;
        return append("data_intern_name",name);
    }
    public JsonObject getJsonObject(){
        return datas;
    }
    public String getString(String key){
        if(!this.datas.has(key)) return null;
        return this.datas.get(key).getAsString();
    }
    public int getInt(String key){
        if(!this.datas.has(key)) return 0;
        return this.datas.get(key).getAsInt();
    }
    public Long getLong(String key){
        if(!this.datas.has(key)) return 0L;
        return this.datas.get(key).getAsLong();
    }
    public Boolean getBoolean(String key){
        if(!this.datas.has(key)) return false;
        return this.datas.get(key).getAsBoolean();
    }
    public <T> T getObject(String key,Class<T> classOF){
        if(!this.datas.has(key)) return null;
        return this.gson.fromJson(this.datas.get(key),classOF);
    }
    public PermissionDocument append(String key, String value){
        this.datas.addProperty(key,value);
        return this;
    }
    public PermissionDocument append(String key, Boolean value){
        this.datas.addProperty(key,value);
        return this;
    }
    public PermissionDocument append(String key, Number value){
        this.datas.addProperty(key,value);
        return this;
    }
    public PermissionDocument append(String key, Object value){
        this.datas.add(key,gson.toJsonTree(value));
        return this;
    }
    public PermissionDocument remove(String key){
        this.datas.remove(key);
        return this;
    }
    public String toJson(){
        return this.gson.toJson(datas);
    }
    public static PermissionDocument get(String gsonString){
        try {
            InputStreamReader reader = new InputStreamReader(new StringBufferInputStream(gsonString), "UTF-8");
            return new PermissionDocument(parser.parse(new BufferedReader(reader)).getAsJsonObject());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
