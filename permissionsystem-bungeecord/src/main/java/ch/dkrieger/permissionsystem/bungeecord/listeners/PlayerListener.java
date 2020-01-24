package ch.dkrieger.permissionsystem.bungeecord.listeners;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 04.07.18 18:27
 *
 */

import ch.dkrieger.permissionsystem.bungeecord.BungeeCordBootstrap;
import ch.dkrieger.permissionsystem.bungeecord.CloudNetV2Extension;
import ch.dkrieger.permissionsystem.bungeecord.CloudNetV3Extension;
import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerListener implements Listener{

    public static final String CONSOLE_COMMAND_SENDER_CLASS = "net.md_5.bungee.command.ConsoleCommandSender";

    @EventHandler(priority=80)
    public void onLogin(LoginEvent event){
        if(ProxyServer.getInstance().getConfig().isOnlineMode() && !(event.getConnection().isOnlineMode())) return;
        PermissionPlayer player = null;
        try{
            player = PermissionPlayerManager.getInstance().getPermissionPlayerSave(event.getConnection().getUniqueId());
        }catch (Exception exception){
            try{
                player = PermissionPlayerManager.getInstance().getPermissionPlayerSave(event.getConnection().getUniqueId());
            }catch (Exception exception2){
                event.setCancelled(true);
                event.setCancelReason(new TextComponent(TextComponent.fromLegacyText(Messages.ERROR)));
                PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.ERROR,null,"Could not load player "+event.getConnection().getName());
                PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.ERROR,null,exception2.getMessage());
                return;
            }
        }
        if(player == null){
            player = PermissionPlayerManager.getInstance().createPermissionPlayer(event.getConnection().getUniqueId()
                    ,event.getConnection().getName());
            if(Config.PLAYER_ADD_DEFAULT_GROUPS_ON_REGISTER){
                PermissionPlayer finalPlayer = player;
                ProxyServer.getInstance().getScheduler().runAsync(BungeeCordBootstrap.getInstance(),()->{
                    for (PermissionGroup group : PermissionGroupManager.getInstance().getDefaultGroups()) {
                        finalPlayer.addGroup(group);
                    }
                });
            }
        }else PermissionPlayerManager.getInstance().checkName(event.getConnection().getUniqueId(),event.getConnection().getName());
        if(ProxyServer.getInstance().getPlayers().size() == 0){
            ProxyServer.getInstance().getScheduler().runAsync(BungeeCordBootstrap.getInstance(),()->{
                PermissionSystem.getInstance().syncGroups();
            });
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxyServer.getInstance().getScheduler().runAsync(BungeeCordBootstrap.getInstance(), ()-> {
            if(event.getPlayer().hasPermission("dkperms.admin") && PermissionSystem.getInstance().getUpdateChecker().hasNewVersion()) {
                event.getPlayer().sendMessage(TextComponent.fromLegacyText(Messages.PREFIX + "§7New version available §e" + PermissionSystem.getInstance().getUpdateChecker().getLatestVersionString()));
            }
        });
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPermissionCheck(PermissionCheckEvent event){
        if(event.getSender() instanceof ProxiedPlayer){
            PermissionPlayer player = PermissionPlayerManager.getInstance()
                    .getPermissionPlayer(((ProxiedPlayer)event.getSender()).getUniqueId());
            if(player != null) event.setHasPermission(player.hasPermission(event.getPermission()));
            else event.setHasPermission(false);
        }else if(event.getSender().getClass().getName().equals(CONSOLE_COMMAND_SENDER_CLASS)) event.setHasPermission(true);
        else if(BungeeCordBootstrap.getInstance().isCloudNetV2() && CloudNetV2Extension.isCloudSender(event.getSender())){
            event.setHasPermission(CloudNetV2Extension.hasPermission(event.getSender(),event.getPermission()));
        }else if(BungeeCordBootstrap.getInstance().isCloudNetV3() && CloudNetV3Extension.isCloudSender(event.getSender())){
            event.setHasPermission(CloudNetV3Extension.hasPermission(event.getSender(),event.getPermission()));
        }else PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.WARN,null,"Command sender "+event.getSender().getClass().toString()+" is not supported.");
    }
}
