package ch.dkrieger.permissionsystem.bukkit.tools.tablist.utils.tablist;

import ch.dkrieger.permissionsystem.bukkit.tools.tablist.utils.Reflection;
import ch.dkrieger.permissionsystem.lib.utils.NetworkUtil;
import com.sun.org.apache.regexp.internal.RE;
import net.minecraft.server.v1_13_R1.ArgumentScoreboardTeam;
import net.minecraft.server.v1_13_R1.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 17:08
 *
 */

public class TabListStyle extends Reflection {

	private static Map<Player, LinkedList<String>> alreadyinuse = new LinkedHashMap<>();

	public static void setStyle(String prefix,String suffix,String priority,Player player,Player receiver) {
        setStyle(prefix,suffix,priority,player.getName(),receiver);
	}
	public static void setStyle(String prefix,String suffix,String priority, String playername,Player receiver) {


        String team_name = priority+playername.charAt(0)+playername.charAt(1);
        team_name = getFreeString(receiver,team_name);
        try{
            Constructor< ? > constructor = reflectNMSClazz("PacketPlayOutScoreboardTeam").getConstructor();
            Object packet = constructor.newInstance();
            List< String > contents = new LinkedList<>();
            contents.add(playername);
            try {
                setField(packet,"a",team_name);
                setField(packet,"b",team_name);
                setField(packet,"c",prefix);
                setField(packet,"d",suffix);
                setField(packet,"e","ALWAYS");
                setField(packet,"h",0);
                setField(packet,"g",contents);
            }catch(Exception exception){
                setField(packet,"a",team_name);
                setField(packet,"b",team_name);
                setField(packet,"c",prefix);
                setField(packet,"d",suffix);
                setField(packet,"e","ALWAYS");
                setField(packet,"i",0);
                setField(packet,"h",contents);
            }
            sendPacket(receiver, packet);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    public static void setStyle(String prefix,String suffix,String priority, Player player){
        setStyle(prefix,suffix,priority,player.getName());
    }
    public static void setStyle(String prefix,String suffix,String priority, String playername){
        for(Player t : Bukkit.getOnlinePlayers()) setStyle(prefix, suffix, priority, playername, t);
    }
	private static String getFreeString(Player player,String priority){
	    String value = priority+ NetworkUtil.getRandomString(10);
		if(alreadyinuse.containsKey(player)) while(alreadyinuse.get(player).contains(value)) value = NetworkUtil.getRandomString(10);
		else alreadyinuse.put(player, new LinkedList<>());
        if(value.length() > 16) value = value.substring(0,16);
        alreadyinuse.get(player).add(value);
		return value;
	}
}
