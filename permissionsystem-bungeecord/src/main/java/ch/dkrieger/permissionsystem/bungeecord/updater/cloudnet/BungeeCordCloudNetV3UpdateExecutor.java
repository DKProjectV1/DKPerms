package ch.dkrieger.permissionsystem.bungeecord.updater.cloudnet;

import ch.dkrieger.permissionsystem.bungeecord.BungeeCordBootstrap;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionGroupCreateEvent;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionGroupDeleteEvent;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionGroupUpdateEvent;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import de.dytanic.cloudnet.ext.bridge.bungee.event.BungeeChannelMessageReceiveEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 08.12.18 16:55
 *
 */

public class BungeeCordCloudNetV3UpdateExecutor implements Listener{

    @EventHandler
    public void onMessageReceive(BungeeChannelMessageReceiveEvent event){
        if(!Config.SYNCHRONISE_CHANNEL) return;
        if(event.getChannel().equalsIgnoreCase("dkperms")){
            ProxyServer.getInstance().getScheduler().runAsync(BungeeCordBootstrap.getInstance(),()->{
                if(event.getMessage().equalsIgnoreCase("group_create")){
                    UUID uuid = event.getData().get("uuid",UUID.class);
                    PermissionUpdater.getInstance().onPermissionGroupCreate(uuid);
                    ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupCreateEvent(uuid,true));
                }else if(event.getMessage().equalsIgnoreCase("group_delete")){
                    UUID uuid = event.getData().get("uuid",UUID.class);
                    PermissionGroup group = PermissionGroupManager.getInstance().getGroup(uuid);
                    PermissionUpdater.getInstance().onPermissionGroupDelete(uuid);
                    ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupDeleteEvent(group,true));
                }else if(event.getMessage().equalsIgnoreCase("update")){
                    boolean online = false;
                    UUID uuid = event.getData().get("uuid",UUID.class);
                    PermissionUpdateData data = event.getData().get("data",PermissionUpdateData.class);
                    PermissionType type = event.getData().get("type",PermissionType.class);
                    if(type == PermissionType.PLAYER && ProxyServer.getInstance().getPlayer(uuid) != null) online = true;
                    PermissionUpdater.getInstance().onPermissionUpdate(type,uuid,online);
                    if(type == PermissionType.GROUP){
                        ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupUpdateEvent(uuid,data,true));
                    }else{
                        ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedPermissionPlayerUpdateEvent(uuid,data,true,online));
                    }
                }else System.out.println(Messages.SYSTEM_PREFIX+"Updater: Invalid update message.");
            });
        }
    }
}
