package ch.dkrieger.permissionsystem.bukkit.updater.cloudnet;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 22:21
 *
 */

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionGroupCreateEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionGroupDeleteEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionGroupUpdateEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateCause;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitCustomChannelMessageReceiveEvent;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitSubChannelMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class SpigotCloudNetV2UpdateExecutor implements Listener{

    public SpigotCloudNetV2UpdateExecutor() {
        BukkitBootstrap.getInstance().setServerName(CloudServer.getInstance().getServerProcessMeta().getServiceId().getServerId());
    }
    @EventHandler
    public void onMessageReceive(BukkitSubChannelMessageEvent event){
        if(!Config.SYNCHRONISE_CHANNEL) return;
        if(event.getChannel().equalsIgnoreCase("dkperms")){
            Bukkit.getScheduler().runTaskAsynchronously(BukkitBootstrap.getInstance(),()->{
                if(event.getMessage().equalsIgnoreCase("group_create")){
                    UUID uuid = event.getDocument().getObject("uuid",UUID.class);
                    PermissionUpdater.getInstance().onPermissionGroupCreate(uuid);
                    Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupCreateEvent(uuid,false));
                }else if(event.getMessage().equalsIgnoreCase("group_delete")){
                    UUID uuid = event.getDocument().getObject("uuid",UUID.class);
                    final PermissionGroup group = PermissionGroupManager.getInstance().getGroup(uuid);
                    PermissionUpdater.getInstance().onPermissionGroupDelete(event.getDocument().getObject("uuid",UUID.class));
                    Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupDeleteEvent(group,false));
                    for(Player players : Bukkit.getOnlinePlayers()) BukkitBootstrap.getInstance().updateDisplayName(players);
                }else if(event.getMessage().equalsIgnoreCase("update")){
                    Boolean online = false;
                    UUID uuid = event.getDocument().getObject("uuid",UUID.class);
                    PermissionType type = event.getDocument().getObject("type",PermissionType.class);
                    PermissionUpdateData data = event.getDocument().getObject("data",PermissionUpdateData.class);
                    if(type == PermissionType.PLAYER && Bukkit.getPlayer(uuid) != null) online = true;
                    PermissionUpdater.getInstance().onPermissionUpdate(type,uuid,online);
                    if(type == PermissionType.PLAYER){
                        Bukkit.getPluginManager().callEvent(new BukkitPermissionPlayerUpdateEvent(uuid,data,false,online));
                        BukkitBootstrap.getInstance().updateDisplayName(uuid);
                    }else{
                        Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupUpdateEvent(uuid,data,false));
                        for(Player players : Bukkit.getOnlinePlayers()) BukkitBootstrap.getInstance().updateDisplayName(players);
                    }
                }else System.out.println(Messages.SYSTEM_PREFIX+"Updater: Invalid update message.");
            });
        }
    }
}
