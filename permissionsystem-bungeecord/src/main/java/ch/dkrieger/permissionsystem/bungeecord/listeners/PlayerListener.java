package ch.dkrieger.permissionsystem.bungeecord.listeners;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 04.07.18 18:27
 *
 */

import ch.dkrieger.permissionsystem.bungeecord.BungeeCordBootstrap;
import ch.dkrieger.permissionsystem.bungeecord.CloudNetV2Extension;
import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.command.ConsoleCommandSender;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerListener implements Listener{

    @EventHandler(priority=80)
    public void onLogin(LoginEvent event){
        if(BungeeCord.getInstance().getConfig().isOnlineMode() && !(event.getConnection().isOnlineMode())) return;
        PermissionPlayer player = null;
        try{
            player = PermissionPlayerManager.getInstance().getPermissionPlayerSave(event.getConnection().getUniqueId());
        }catch (Exception exception){
            try{
                player = PermissionPlayerManager.getInstance().getPermissionPlayerSave(event.getConnection().getUniqueId());
            }catch (Exception exception2){
                event.setCancelled(true);
                event.setCancelReason(new TextComponent(Messages.ERROR));
                PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.ERROR,null,"Could not load player "+event.getConnection().getName());
                PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.ERROR,null,exception2.getMessage());
                return;
            }
        }
        if(player == null){
            player = PermissionPlayerManager.getInstance().createPermissionPlayer(event.getConnection().getUniqueId()
                    ,event.getConnection().getName());
        }else PermissionPlayerManager.getInstance().checkName(event.getConnection().getUniqueId(),event.getConnection().getName());
        if(BungeeCord.getInstance().getPlayers().size() == 0){
            BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBootstrap.getInstance(),()->{
                PermissionSystem.getInstance().syncGroups();
            });
        }
    }
    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPermissionCheck(PermissionCheckEvent event){
        if(event.getSender() instanceof ProxiedPlayer){
            PermissionPlayer player = PermissionPlayerManager.getInstance()
                    .getPermissionPlayer(((ProxiedPlayer)event.getSender()).getUniqueId());
            if(player != null) event.setHasPermission(player.hasPermission(event.getPermission()));
            else event.setHasPermission(false);
        }else if(event.getSender() instanceof ConsoleCommandSender) event.setHasPermission(true);
        else if(BungeeCordBootstrap.getInstance().isCloudNetV2() && CloudNetV2Extension.isCloudSender(event.getSender())){
            event.setHasPermission(CloudNetV2Extension.hasPermission(event.getSender(),event.getPermission()));
        }else PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.WARN,null,"Command sender "+event.getSender().getClass().toString()+" is not supported.");
    }
}
