package ch.dkrieger.permissionsystem.bukkit.listeners;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 14:38
 *
 */

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.bukkit.DKPermissable;
import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.lang.reflect.Field;

public class PlayerListener implements Listener{

    @EventHandler(priority=EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event){
        PermissionPlayer player = null;
        try{
            player = PermissionPlayerManager.getInstance().getPermissionPlayerSave(event.getPlayer().getUniqueId());
        }catch (Exception exception){
            try{
                player = PermissionPlayerManager.getInstance().getPermissionPlayerSave(event.getPlayer().getUniqueId());
            }catch (Exception exception2){
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED,Messages.ERROR);
                PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.ERROR,null,"Could not load player "+event.getPlayer().getName());
                PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.ERROR,null,exception2.getMessage());
                return;
            }
        }
        if(player == null){
            player = PermissionPlayerManager.getInstance().createPermissionPlayer(event.getPlayer().getUniqueId(),event.getPlayer().getName());
        }else PermissionPlayerManager.getInstance().checkName(event.getPlayer().getUniqueId(),event.getPlayer().getName());
        try {
            Class<?> clazz = reflectCraftClazz(".entity.CraftHumanEntity");
            Field field = null;
            if(clazz != null) field = clazz.getDeclaredField("perm");
            else field = Class.forName("net.glowstone.entity.GlowHumanEntity").getDeclaredField("permissions");
            field.setAccessible(true);
            field.set(event.getPlayer(),new DKPermissable(event.getPlayer()));
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        if(Bukkit.getOnlinePlayers().size() == 0){
            Bukkit.getScheduler().runTaskAsynchronously(BukkitBootstrap.getInstance(),()->{
                PermissionSystem.getInstance().syncGroups();
            });
        }
        Bukkit.getScheduler().runTaskLater(BukkitBootstrap.getInstance(),()->{
            BukkitBootstrap.getInstance().updateDisplayName(event.getPlayer());
        },6);
    }
    public static Class<?> reflectCraftClazz(String suffix){
        try{
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            return Class.forName("org.bukkit.craftbukkit."+version+suffix);
        }catch (Exception ex){
            try{
                return Class.forName("org.bukkit.craftbukkit."+suffix);
            }catch (ClassNotFoundException localClassNotFoundException){}
        }
        return null;
    }
}
