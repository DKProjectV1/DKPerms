package ch.dkrieger.permissionsystem.bukkit.tools.tablist;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 17:06
 *
 */

import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionGroupDeleteEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionGroupUpdateEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.bukkit.tools.tablist.utils.TabListStyle;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandPermission;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TabListExtension extends JavaPlugin implements Listener{

    private TabListManager tablistmanager;

    @Override
    public void onEnable() {
        this.tablistmanager = new TabListManager();
        Bukkit.getScheduler().runTaskLater(this,()->{
            Bukkit.getPluginManager().registerEvents(this,this);
            for(Player player : Bukkit.getOnlinePlayers()) TabListManager.getInstance().loadPlayer(player);
        },12L);
    }
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(this,()->{
            this.tablistmanager.loadPlayer(event.getPlayer());
        });
    }
    @EventHandler
    public void onPlayerUpdate(BukkitPermissionPlayerUpdateEvent event){
        Player player = Bukkit.getPlayer(event.getUUID());
        if(player != null){
            Bukkit.getScheduler().runTaskAsynchronously(this,()->{
                this.tablistmanager.setTab(player);
            });
        }
    }
    @EventHandler
    public void onGroupUpate(BukkitPermissionGroupUpdateEvent event){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player != null){
                Bukkit.getScheduler().runTaskAsynchronously(this,()->{
                    this.tablistmanager.setTab(player);
                });
            }
        }
    }
    @EventHandler
    public void onGroupDelete(BukkitPermissionGroupDeleteEvent event){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player != null){
                Bukkit.getScheduler().runTaskAsynchronously(this,()->{
                    this.tablistmanager.setTab(player);
                });
            }
        }
    }
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(this,()->{
            TabListManager.getInstance().loadPlayer(event.getPlayer());
        });
    }
}
