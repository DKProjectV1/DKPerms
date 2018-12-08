package ch.dkrieger.permissionsystem.tools.dkcoinsconnection.bungeecord;

import ch.dkrieger.coinsystem.bungeecord.event.ProxiedCoinPlayerColorSetEvent;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import ch.dkrieger.coinsystem.core.player.CoinPlayerManager;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 29.07.18 12:34
 *
 */

public class BungeeCordDKPermsDKCoinsConnection extends Plugin implements Listener{

    @Override
    public void onEnable() {
        BungeeCord.getInstance().getScheduler().schedule(this,()->{
            if(BungeeCord.getInstance().getPluginManager().getPlugin("DKPerms") == null){
                System.out.println("[DKPermsDKBansConnection] DKPerms not found");
                return;
            }
            if(BungeeCord.getInstance().getPluginManager().getPlugin("DKCoins") == null){
                System.out.println("[DKPermsDKCoinsConnection] DKCoins not found");
                return;
            }
            getProxy().getPluginManager().registerListener(this,this);
        },2L, TimeUnit.SECONDS);
    }
    @EventHandler
    public void onColorSet(ProxiedCoinPlayerColorSetEvent event){
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
            CoinPlayer player = CoinPlayerManager.getInstance().getPlayer(event.getUUID());
            if(player != null) player.setColor(event.getPlayer().getColor());
        });
    }
}
