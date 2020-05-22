package ch.dkrieger.permissionsystem.bukkit.hook;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 14.06.19 22:34
 *
 */

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.bukkit.utils.Reflection;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import com.sk89q.wepif.PermissionsResolver;
import com.sk89q.wepif.PermissionsResolverManager;
import org.bukkit.OfflinePlayer;

import java.util.Iterator;
import java.util.List;

public class WorldEditPermissionResolver implements PermissionsResolver {

    public WorldEditPermissionResolver(){
        try {
            Reflection.setField(PermissionsResolverManager.getInstance(),"permissionResolver",this);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Could not hook into WorldEdit.");
        }
    }

    @Override
    public void load() {}//Unused

    @Override
    public String getDetectionMessage() {
        return "DKPerms detected!";
    }

    @Override
    public boolean hasPermission(OfflinePlayer player, String permission) {
        return hasPermission(player.getName(),permission);
    }

    @Override
    public boolean hasPermission(String name, String permission) {
        PermissionPlayer player = getPlayer(name);
        return player != null && player.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(String worldName, OfflinePlayer player, String permission) {
        return hasPermission(worldName,player.getName(),permission);
    }

    @Override
    public boolean hasPermission(String worldName, String name, String permission) {
        PermissionPlayer player = getPlayer(name);
        return player != null && player.hasPermission(permission, BukkitBootstrap.getInstance().getServerName(),worldName);
    }

    @Override
    public boolean inGroup(OfflinePlayer player, String group) {
        return inGroup(player.getName(),group);
    }

    @Override
    public boolean inGroup(String name, String group) {
        PermissionPlayer player = getPlayer(name);
        return player!=null&& player.isInGroup(group);
    }

    @Override
    public String[] getGroups(OfflinePlayer player) {
        return getGroups(player.getName());
    }

    @Override
    public String[] getGroups(String name) {
        PermissionPlayer player = getPlayer(name);
        if(player == null) return new String[]{};

        List<PermissionGroupEntity> groups = player.getGroups();
        String[] names = new String[groups.size()];
        for(int i = 0;i<groups.size();i++){
            PermissionGroupEntity entity = groups.get(i);
            if(!entity.hasTimeOut()) names[i] = entity.getGroup().getName();
        }
        return names;
    }

    public PermissionPlayer getPlayer(String name){
        return PermissionPlayerManager.getInstance().getPermissionPlayer(name);
    }
}
