package ch.dkrieger.permissionsystem.bukkit.hook.vault;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 12:28
 *
 */

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.util.List;

public class VaultPermissionHook extends Permission {

    public VaultPermissionHook(Plugin plugin) {
        Bukkit.getServicesManager().register(Permission.class,this, plugin, ServicePriority.Highest);
        Bukkit.getServicesManager().register(Chat.class,new VaultChatHook(this), plugin, ServicePriority.Highest);
    }
    @Override
    public String getName() {
        return BukkitBootstrap.getInstance().getName();
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean hasSuperPermsCompat() {
        return false;
    }
    @Override
    public boolean playerHas(String world, String name, String permission) {
        PermissionPlayer player = getPlayer(name);
        if(player != null) return player.hasPermission(permission,BukkitBootstrap.getInstance().getServerName(),world);
        return false;
    }
    @Override
    public boolean playerAdd(String world, String name, String permission) {
        PermissionPlayer player = getPlayer(name);
        if(player != null){
            player.addPermission(permission,world);
            return true;
        }
        return false;
    }
    @Override
    public boolean playerRemove(String world, String name, String permission) {
        PermissionPlayer player = getPlayer(name);
        if(player != null){
            player.removePermission(permission,world);
            return true;
        }
        return false;
    }
    @Override
    public boolean groupHas(String world, String name, String permission) {
        PermissionGroup group = getGroup(name);
        if(group != null) return group.hasPermission(permission,BukkitBootstrap.getInstance().getServerName(),world);
        return false;
    }
    @Override
    public boolean groupAdd(String world, String name, String permission) {
        PermissionGroup group = getGroup(name);
        if(group != null){
            group.addPermission(permission,world);
        }
        return false;
    }
    @Override
    public boolean groupRemove(String world, String name, String permission) {
        PermissionGroup group = getGroup(name);
        if(group != null){
            group.removePermission(permission,world);
        }
        return false;
    }
    @Override
    public boolean playerInGroup(String world, String name, String group) {
        PermissionPlayer player = getPlayer(name);
        if(player != null) player.isInGroup(group);
        return false;
    }
    @Override
    public boolean playerAddGroup(String world, String name, String groupname) {
        PermissionPlayer player = getPlayer(name);
        if(player != null){
            PermissionGroup group = PermissionGroupManager.getInstance().getGroup(groupname);
            if(group != null) player.addGroup(group);
        }
        return false;
    }
    @Override
    public boolean playerRemoveGroup(String world, String name, String groupname) {
        PermissionPlayer player = getPlayer(name);
        if(player != null){
            PermissionGroup group = PermissionGroupManager.getInstance().getGroup(groupname);
            if(group != null) player.removeGroup(group);
        }
        return false;
    }
    @Override
    public String[] getPlayerGroups(String world, String name) {
        String list = "";
        PermissionPlayer player = getPlayer(name);
        if(player != null){
            for(PermissionGroupEntity entity : player.getGroups()){
                if(!entity.hasTimeOut() && entity.getGroup() != null) list += entity.getGroup().getName()+";";
            }
        }
        if(list.length() > 0) list = list.substring(0,list.length()-1);
        return list.split(";");
    }
    @Override
    public String getPrimaryGroup(String world, String name) {
        PermissionPlayer player = getPlayer(name);
        if(player != null){
            PermissionGroup group = player.getHighestGroup();
            if(group != null) return group.getName();
        }
        return null;
    }
    @Override
    public String[] getGroups() {
        List<PermissionGroup> groups = PermissionGroupManager.getInstance().getGroups();
        String[] names = new String[groups.size()];
        for(int i = 0;i<groups.size();i++) names[i] = groups.get(i).getName();
        return names;
    }
    @Override
    public boolean hasGroupSupport() {
        return true;
    }
    public PermissionPlayer getPlayer(String name){
        return PermissionPlayerManager.getInstance().getPermissionPlayer(name);
    }
    public PermissionGroup getGroup(String name){
        return PermissionGroupManager.getInstance().getGroup(name);
    }
}
