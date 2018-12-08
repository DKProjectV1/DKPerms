package ch.dkrieger.permissionsystem.lib.utils;

import ch.dkrieger.permissionsystem.lib.config.Config;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class NetworkUtil {

    public static final Random RANDOM = new Random();
    public static final Gson GSON = new Gson();
    public static JsonParser PARSER = new JsonParser();

    public static String getRandomString(final int size){
        char data = ' ';
        String dat = "";
        for(int i=0;i<=size;i++) {
            data = (char)(RANDOM.nextInt(25)+97);
            dat = data+dat;
        }
        return dat;
    }
    public static int getMaxPages(int pagesize,Collection<?> collection) {
        return getMaxPages(pagesize,collection.size());
    }
    public static int getMaxPages(int pagesize,int size) {
        int max = pagesize;
        int i = size;
        if (i % max == 0) return i / max;
        double j = i / pagesize;
        int h = (int) Math.floor(j * 100) / 100;
        return h + 1;
    }
    public static String getGroup(String server){
        int cid = 0, creplace = 0;
        for(char c : server.toCharArray()){
            cid++;
            if(c == Config.SERVERGROUP_SPLIT) creplace = cid-1;
        }
        return server.substring(0,creplace);
    }
    public static boolean isNumber(String value){
        try{
            int number = Integer.parseInt(value);
            return true;
        }catch(NumberFormatException e){return false;}
    }
    public static boolean hasTimeOut(Long timeout){
        if(timeout <= 0) return false;
        return System.currentTimeMillis() > timeout;
    }
}
