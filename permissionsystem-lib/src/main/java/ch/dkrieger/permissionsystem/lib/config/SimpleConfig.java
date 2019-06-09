package ch.dkrieger.permissionsystem.lib.config;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 13:39
 *
 */

import ch.dkrieger.permissionsystem.lib.platform.DKPermsPlatform;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class SimpleConfig {

    private final File file;
    private DKPermsPlatform platform;
    private Configuration config;

    public SimpleConfig(DKPermsPlatform platform, File file) {
        this.platform = platform;
        this.file = file;

        try{
            file.getParentFile().mkdirs();
            if(!file.exists()) this.file.createNewFile();
        }catch (Exception exception){
            System.out.println(Messages.SYSTEM_PREFIX+"Could not create config file.");
            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
        }
    }
    public DKPermsPlatform getPlatform() {
        return platform;
    }
    public File getFile() {
        return file;
    }
    public void reloadConfig(){
        loadConfig();
    }
    public void loadConfig(){
        load();
        registerDefaults();
        save();
        onLoad();
    }
    public void save(){
        if(file == null || !file.exists()) return;
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config,file);
        }catch (Exception exception) {
            System.out.println(Messages.SYSTEM_PREFIX+"Could not save config file.");
            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
        }
    }
    public void load(){
        if(file == null) return;
        try{
            if(file.exists()) file.createNewFile();
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (Exception exception){
            System.out.println(Messages.SYSTEM_PREFIX+"Could not load config file.");
            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
        }
    }
    public void setValue(String path, Object value){
        this.config.set(path,value);
    }
    public void addValue(String path, Object value){
        if(!this.config.contains(path))this.config.set(path,value);
    }
    public String getStringValue(String path){
        return this.config.getString(path);
    }
    public String getMessageValue(String path){
        return this.platform.translateColorCodes(getStringValue(path));
    }
    public int getIntValue(String path){
        return this.config.getInt(path);
    }
    public double getDoubleValue(String path){
        return this.config.getDouble(path);
    }
    public long getLongValue(String path){
        return this.config.getLong(path);
    }
    public boolean getBooleanValue(String path){
        return this.config.getBoolean(path);
    }
    public List<String> getStringListValue(String path){
        return this.config.getStringList(path);
    }
    public List<Integer> getIntListValue(String path){
        return this.config.getIntList(path);
    }
    public List<Double> getDoubleListValue(String path){
        return this.config.getDoubleList(path);
    }
    public List<Long> getLongListValue(String path){
        return this.config.getLongList(path);
    }
    public List<Boolean> getBooleanListValue(String path){
        return this.config.getBooleanList(path);
    }
    public Object getValue(String path){
        return this.config.getStringList(path);
    }
    public Collection<String> getKeys(String path){
        Configuration config = this.config.getSection(path);
        if(config != null) return config.getKeys();
        else return new LinkedList<>();
    }
    public Boolean contains(String path){
        return this.config.contains(path);
    }
    public abstract void onLoad();
    public abstract void registerDefaults();
}
