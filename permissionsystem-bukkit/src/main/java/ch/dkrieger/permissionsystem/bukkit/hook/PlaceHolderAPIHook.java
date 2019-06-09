package ch.dkrieger.permissionsystem.bukkit.hook;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 11:52
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceHolderAPIHook {

    public PlaceHolderAPIHook() {
        new RequestHook().register();
    }

    private class RequestHook extends PlaceholderExpansion {

        @Override
        public String getIdentifier() {
            return Messages.SYSTEM_NAME.toLowerCase();
        }

        @Override
        public String getPlugin() {
            return Messages.SYSTEM_NAME;
        }

        @Override
        public String getAuthor() {
            return "Dkrieger";
        }

        @Override
        public String getVersion() {
            return PermissionSystem.getInstance().getVersion();
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

    public String set(Player player, String message){
        return PlaceholderAPI.setPlaceholders(player, message);
    }
}
