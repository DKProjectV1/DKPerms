package ch.dkrieger.permissionsystem.tools.dkbansconnection.bukkit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 22:11
 *
 */

import ch.dkrieger.bansystem.spigot.event.SpigotCoinPlayerColorSetEvent;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
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
    public void onColorSet(SpigotCoinPlayerColorSetEvent event){
        PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(event.getPlayer().getUniqueId());
        PermissionGroup group = null;
        if(player != null) group = player.getHighestGroup();
        else group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
        if(group != null && !(group.getPlayerDesign().getColor().equalsIgnoreCase("-1")))
            event.setColor(group.getPlayerDesign().getColor());
    }
}
