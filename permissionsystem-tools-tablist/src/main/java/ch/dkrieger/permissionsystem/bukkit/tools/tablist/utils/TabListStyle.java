package ch.dkrieger.permissionsystem.bukkit.tools.tablist.utils;

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static ch.dkrieger.permissionsystem.bukkit.utils.Reflection.*;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.12.18 16:14
 *
 */

public class TabListStyle {

	private static Map<Player, LinkedList<String>> alreadyinuse = new LinkedHashMap<>();

	public static void setStyle(String prefix,String suffix,String priority,Player player,Player receiver) {
        setStyle(prefix,suffix,priority,player.getName(),receiver);
	}
	public static void setStyle(String prefix,String suffix,String priority, String playerName,Player receiver) {
        String team_name = priority+playerName.charAt(0)+playerName.charAt(1);

        if(BukkitBootstrap.getInstance().getPlaceHolderAPI() != null){
            Player player = Bukkit.getPlayer(playerName);
            if(player != null){
                prefix = BukkitBootstrap.getInstance().getPlaceHolderAPI().set(player,prefix);
                suffix = BukkitBootstrap.getInstance().getPlaceHolderAPI().set(player,suffix);
            }
        }

        if(prefix.length() > 16) prefix = prefix.substring(0,16);
        if(suffix.length() > 16) suffix = suffix.substring(0,16);

        team_name = getFreeString(receiver,team_name);
        try{
            Constructor< ? > constructor = getMinecraftClass("PacketPlayOutScoreboardTeam").getConstructor();
            Object packet = constructor.newInstance();
            List< String > contents = new LinkedList<>();
            contents.add(playerName);
            setField(packet,"a",team_name);
            if(hasField(packet.getClass(),"j")){
                setField(packet,"e","always");
                setField(packet,"f","always");
                setField(packet,"h",contents);
                if(getField(packet.getClass(),"b").getType() == String.class){
                    /*
                    Minecraft 1.9 - 1.12
                    */
                    setField(packet,"b",team_name);
                    setField(packet,"c",prefix);
                    setField(packet,"d",suffix);
                    setField(packet,"g",-1);
                }else{
                    /*
                    Minecraft >1.13
                    */
                    Constructor< ? > component = getMinecraftClass("ChatComponentText").getConstructor(String.class);
                    setField(packet,"b",component.newInstance(team_name));
                    setField(packet,"c",component.newInstance(prefix));
                    setField(packet,"d",component.newInstance(suffix));

                    String color = "GRAY";
                    if(prefix.length() >= 2 && prefix.charAt(prefix.length()-2) == '§'){
                        ChatColor chatColor = ChatColor.getByChar(prefix.charAt(prefix.length()-1));
                        if(chatColor != null) color = chatColor.name().toUpperCase();
                    }
                    setField(packet,"g",getMinecraftClass("EnumChatFormat").getField(color).get(null));
                }
                setField(packet,"i",0);
                setField(packet,"j",0);
            }else{
                /*
                Minecraft 1.8
                 */
                setField(packet,"b",team_name);
                setField(packet,"c",prefix);
                setField(packet,"d",suffix);
                setField(packet,"e","ALWAYS");
                setField(packet,"h",0);
                setField(packet,"g",contents);
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
	    String value = priority+ GeneralUtil.getRandomString(10);
		if(alreadyinuse.containsKey(player)) while(alreadyinuse.get(player).contains(value)) value = GeneralUtil.getRandomString(10);
		else alreadyinuse.put(player, new LinkedList<>());
        if(value.length() > 16) value = value.substring(0,16);
        alreadyinuse.get(player).add(value);
		return value;
	}
}
