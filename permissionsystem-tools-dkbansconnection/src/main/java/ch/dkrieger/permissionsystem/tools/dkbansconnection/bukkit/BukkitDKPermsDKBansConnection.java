package ch.dkrieger.permissionsystem.tools.dkbansconnection.bukkit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 22:11
 *
 */

import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerColorSetEvent;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerOfflinePermissionCheckEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.tools.dkbansconnection.DKBansPlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitDKPermsDKBansConnection extends JavaPlugin implements Listener{

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this,()->{
            if(!Bukkit.getPluginManager().isPluginEnabled("DKPerms")){
                System.out.println("[DKPermsDKBansConnection] DKPerms not found");
                return;
            }
            if(!Bukkit.getPluginManager().isPluginEnabled("DKBans")){
                System.out.println("[DKPermsDKBansConnection] DKBans not found");
                return;
            }
            if(PermissionPlayerManager.getInstance() == null){
                System.out.println("[DKPermsDKBansConnection] Could not setup player storage connection");
                return;
            }
            PermissionPlayerManager.getInstance().setStorage(new DKBansPlayerStorage());
            Bukkit.getPluginManager().registerEvents(this,this);
        },8L);
    }

    @EventHandler
    public void onColorSet(BukkitNetworkPlayerColorSetEvent event){
        PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(event.getUUID());
        if(player != null) event.setColor(player.getColor());
    }

    @EventHandler
    public void onUpdate(BukkitPermissionPlayerUpdateEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(this,()->{
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(event.getUUID());
            if(player != null) player.setColor(event.getPlayer().getColor());
        });
    }

    @EventHandler
    public void onOfflinePermissionCheck(BukkitNetworkPlayerOfflinePermissionCheckEvent event){
        PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(event.getUUID());
        if(player != null) event.setHasPermission(player.hasPermission(event.getPermission()));
    }
}
