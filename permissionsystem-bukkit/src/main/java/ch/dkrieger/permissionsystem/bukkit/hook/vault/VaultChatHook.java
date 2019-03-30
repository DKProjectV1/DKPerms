package ch.dkrieger.permissionsystem.bukkit.hook.vault;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 12:28
 *
 */

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class VaultChatHook extends Chat{

    public VaultChatHook(Permission perms) {
        super(perms);
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
    public String getPlayerPrefix(String s, String name) {
        PermissionPlayer player = getPlayer(name);
        if(player != null){
            PermissionGroup group = player.getHighestGroup();
            if(group != null) return group.getPlayerDesign().getPrefix();
        }
        return "";
    }
    @Override
    public void setPlayerPrefix(String s, String s1, String s2) {
        throw new UnsupportedOperationException("DKPerms does not support extra player prefix.");
    }
    @Override
    public String getPlayerSuffix(String s, String name) {
        PermissionPlayer player = getPlayer(name);
        if(player != null){
            PermissionGroup group = player.getHighestGroup();
            if(group != null) return group.getPlayerDesign().getSuffix();
        }
        return "";
    }
    @Override
    public void setPlayerSuffix(String s, String s1, String s2) {
        throw new UnsupportedOperationException("DKPerms does not support extra player suffix.");
    }
    @Override
    public String getGroupPrefix(String s, String name) {
        PermissionGroup group = getGroup(name);
        if(group != null) return group.getPlayerDesign().getPrefix();
        return "";
    }
    @Override
    public void setGroupPrefix(String s, String name, String prefix) {
        PermissionGroup group = getGroup(name);
        if(group != null) group.setPrefix(prefix);
    }
    @Override
    public String getGroupSuffix(String s, String name) {
        PermissionGroup group = getGroup(name);
        if(group != null) return group.getPlayerDesign().getPrefix();
        return "";
    }
    @Override
    public void setGroupSuffix(String s, String name, String suffix) {
        PermissionGroup group = getGroup(name);
        if(group != null) group.setSuffix(suffix);
    }
    @Override
    public int getPlayerInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }
    @Override
    public void setPlayerInfoInteger(String s, String s1, String s2, int i) {

    }
    @Override
    public int getGroupInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }
    @Override
    public void setGroupInfoInteger(String s, String s1, String s2, int i) {

    }
    @Override
    public double getPlayerInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }
    @Override
    public void setPlayerInfoDouble(String s, String s1, String s2, double v) {

    }
    @Override
    public double getGroupInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }
    @Override
    public void setGroupInfoDouble(String s, String s1, String s2, double v) {

    }
    @Override
    public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }
    @Override
    public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b) {

    }
    @Override
    public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }
    @Override
    public void setGroupInfoBoolean(String s, String s1, String s2, boolean b) {

    }
    @Override
    public String getPlayerInfoString(String s, String s1, String s2, String s3) {
        return null;
    }
    @Override
    public void setPlayerInfoString(String s, String s1, String s2, String s3) {

    }
    @Override
    public String getGroupInfoString(String s, String s1, String s2, String s3) {
        return null;
    }
    @Override
    public void setGroupInfoString(String s, String s1, String s2, String s3) {

    }



    public PermissionPlayer getPlayer(String name){
        return PermissionPlayerManager.getInstance().getPermissionPlayer(name);
    }
    public PermissionGroup getGroup(String name){
        return PermissionGroupManager.getInstance().getGroup(name);
    }
}
