package ch.dkrieger.permissionsystem.lib.utils;

import ch.dkrieger.permissionsystem.lib.config.Config;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.*;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class GeneralUtil {

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

    public static <U> U iterateOne(Iterable<U> list, GeneralUtil.AcceptAble<U> acceptAble) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) return result;
        return null;
    }
    public static <U> void iterateForEach(Iterable<U> list, GeneralUtil.ForEach<U> forEach){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) forEach.forEach(result);
    }
    public static <U> void iterateAcceptedForEach(Iterable<U> list, GeneralUtil.AcceptAble<U> acceptAble, GeneralUtil.ForEach<U> forEach) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) forEach.forEach(result);
    }
    public static <U> List<U> iterateAcceptedReturn(Iterable<U> list, GeneralUtil.AcceptAble<U> acceptAble){
        List<U> result = new ArrayList<>();
        iterateAcceptedForEach(list,acceptAble,result::add);
        return result;
    }
    public static <U> void iterateAndRemove(Iterable<U> list, GeneralUtil.AcceptAble<U> acceptAble){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) iterator.remove();
    }
    public interface AcceptAble<T> {
        boolean accept(T object);
    }
    public interface ForEach<T> {
        void forEach(T object);
    }
}