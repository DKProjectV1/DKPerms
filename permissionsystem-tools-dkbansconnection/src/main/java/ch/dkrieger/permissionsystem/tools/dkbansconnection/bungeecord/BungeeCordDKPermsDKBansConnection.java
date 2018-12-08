package ch.dkrieger.permissionsystem.tools.dkbansconnection.bungeecord;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.07.18 08:07
 *
 */

import ch.dkrieger.bansystem.bungeecord.api.event.ProxiedNetworkPlayerColorSetEvent;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerManager;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.tools.dkbansconnection.DKBansPlayerStorage;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class BungeeCordDKPermsDKBansConnection extends Plugin implements Listener{

    @Override
    public void onEnable() {
        BungeeCord.getInstance().getScheduler().schedule(this,()->{
            if(BungeeCord.getInstance().getPluginManager().getPlugin("DKPerms") == null){
                System.out.println("[DKPermsDKBansConnection] DKPerms not found");
                return;
            }
            if(BungeeCord.getInstance().getPluginManager().getPlugin("DKBans") == null){
                System.out.println("[DKPermsDKBansConnection] DKBans not found");
                return;
            }
            if(PermissionPlayerManager.getInstance() == null){
                System.out.println("[DKPermsDKBansConnection] Could not setup player storage connection");
                return;
            }
            PermissionPlayerManager.getInstance().setStorage(new DKBansPlayerStorage());
            getProxy().getPluginManager().registerListener(this,this);
        },2L, TimeUnit.SECONDS);
    }
    @EventHandler
    public void onColorSet(ProxiedNetworkPlayerColorSetEvent event){
        PermissionPlayer player = PermissionPlayerManager.getInstance().getPermissionPlayer(event.getPlayer().getUniqueId());
        PermissionGroup group = null;
        if(player != null) group = player.getHighestGroup();
        else group = PermissionGroupManager.getInstance().getHighestDefaultGroup();
        if(group != null && !(group.getPlayerDesign().getColor().equalsIgnoreCase("-1")))
            event.setColor(group.getPlayerDesign().getColor());
    }
    @EventHandler
    public void onUpdate(ProxiedPermissionPlayerUpdateEvent event){
        BungeeCord.getInstance().getScheduler().runAsync(this,()->{
            NetworkPlayer player = NetworkPlayerManager.getInstance().getNetworkPlayer(event.getUUID());
            if(player != null) NetworkPlayerManager.getInstance().setSetting(player.getUUID(),"color",event.getPlayer().getColor());
        });
    }
}
