package ch.dkrieger.permissionsystem.tools.dkcoinsconnection.bukkit;

import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.core.player.CoinPlayerManager;
import ch.dkrieger.coinsystem.spigot.event.BukkitCoinPlayerColorSetEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 29.07.18 12:33
 *
 */

public class BukkitDKPermsDKCoinsConnection extends JavaPlugin implements Listener{

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this,()->{
            if(!Bukkit.getPluginManager().isPluginEnabled("DKPerms")){
                System.out.println("[DKPermsDKCoinsConnection] DKPerms not found");
                return;
            }
            if(!Bukkit.getPluginManager().isPluginEnabled("DKCoins")){
                System.out.println("[DKPermsDKCoinsConnection] DKCoins not found");
                return;
            }
            Bukkit.getPluginManager().registerEvents(this,this);
        },8L);
    }
    @EventHandler
    public void onColorSet(BukkitCoinPlayerColorSetEvent event){
        PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(event.getPlayer().getUUID());
        PermissionGroup group = null;
        if(player != null) group = player.getHighestGroup();
        else group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
        if(group != null && !(group.getPlayerDesign().getColor().equalsIgnoreCase("-1")))
            event.setColor(group.getPlayerDesign().getColor());
    }
    @EventHandler
    public void onUpdate(BukkitPermissionPlayerUpdateEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(this,()->{
            CoinPlayer player = CoinPlayerManager.getInstance().getPlayer(event.getUUID());
            if(player != null) player.setColor(event.getPlayer().getColor());
        });
    }
}
