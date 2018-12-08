package ch.dkrieger.permissionsystem.bukkit.hook;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 11:52
 *
 */

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceHolderAPIHook extends EZPlaceholderHook {


    public PlaceHolderAPIHook(Plugin plugin) {
        super(plugin, "dkperms");
    }
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if(identifier.equalsIgnoreCase("rank")){
            PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
            if(permissionplayer != null){
                PermissionGroup group = permissionplayer.getHighestGroup();
                if(group != null) return group.getPlayerDesign().getColor()+group.getName();
            }else{
                PermissionGroup group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
                if(group != null) return group.getPlayerDesign().getColor()+group.getName();
            }
        }else if(identifier.equalsIgnoreCase("color")){
            PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
            if(permissionplayer != null){
                PermissionGroup group = permissionplayer.getHighestGroup();
                if(group != null) return group.getPlayerDesign().getColor();
            }else{
                PermissionGroup group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
                if(group != null) return group.getPlayerDesign().getColor();
            }
        }else if(identifier.equalsIgnoreCase("prefix")){
            PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
            if(permissionplayer != null){
                PermissionGroup group = permissionplayer.getHighestGroup();
                if(group != null) return group.getPlayerDesign().getPrefix();
            }else{
                PermissionGroup group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
                if(group != null) return group.getPlayerDesign().getPrefix();
            }
        }else if(identifier.equalsIgnoreCase("suffix")){
            PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
            if(permissionplayer != null){
                PermissionGroup group = permissionplayer.getHighestGroup();
                if(group != null) return group.getPlayerDesign().getSuffix();
            }else{
                PermissionGroup group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
                if(group != null) return group.getPlayerDesign().getSuffix();
            }
        }else if(identifier.equalsIgnoreCase("display")){
            PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
            if(permissionplayer != null){
                PermissionGroup group = permissionplayer.getHighestGroup();
                if(group != null) return group.getPlayerDesign().getColor();
            }else{
                PermissionGroup group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
                if(group != null) return group.getPlayerDesign().getColor();
            }
        }else if(identifier.equalsIgnoreCase("team")){
            PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
            if(permissionplayer != null){
                for(PermissionGroupEntity group : permissionplayer.getGroups()){
                    if(!group.hasTimeOut() && group.getGroup() != null && group.getGroup().isTeam()) return "true";
                }
            }
            return "false";
        }else if(identifier.equalsIgnoreCase("priority")){
            PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
            if(permissionplayer != null){
                PermissionGroup group = permissionplayer.getHighestGroup();
                if(group != null) return ""+group.getPriority();
            }else{
                PermissionGroup group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
                if(group != null) return ""+group.getPriority();
            }
        }else if(identifier.equalsIgnoreCase("description")){
            PermissionPlayer permissionplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());
            if(permissionplayer != null){
                PermissionGroup group = permissionplayer.getHighestGroup();
                if(group != null) return ""+group.getDescription();
            }else{
                PermissionGroup group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
                if(group != null) return ""+group.getDescription();
            }
        }
        return "";
    }
}
