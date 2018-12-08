package ch.dkrieger.permissionsystem.bukkit.updater;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 16:25
 *
 */

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionGroupCreateEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionGroupDeleteEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionGroupUpdateEvent;
import ch.dkrieger.permissionsystem.bukkit.event.BukkitPermissionPlayerUpdateEvent;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandTeam;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateExecutor;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import ch.dkrieger.permissionsystem.lib.utils.PermissionDocument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.UUID;

public class SpigotPluginMessageUpdateExecutor implements PermissionUpdateExecutor, PluginMessageListener{

    @Override
    public void executePermissionGroupCreate(UUID uuid) {
        sendToBungeeCord(new PermissionDocument("group_create").append("uuid",uuid));
        Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupCreateEvent(uuid,true));
        CommandTeam.forceupdate();
    }
    @Override
    public void executePermissionGroupDelete(PermissionGroup group) {
        sendToBungeeCord(new PermissionDocument("group_delete").append("uuid",group.getUUID()));
        Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupDeleteEvent(group,true));
        CommandTeam.forceupdate();
    }
    @Override
    public void executePermissionUpdate(PermissionType type, UUID uuid, PermissionUpdateData data) {
        sendToBungeeCord(new PermissionDocument("update").append("type",type).append("uuid",uuid).append("data",data));
        if(type == PermissionType.PLAYER){
            Boolean online = false;
            if(Bukkit.getPlayer(uuid) != null) online = true;
            Bukkit.getPluginManager().callEvent(new BukkitPermissionPlayerUpdateEvent(uuid,data,true,online));
        }else{
            Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupUpdateEvent(uuid,data,true));
        }
        CommandTeam.forceupdate();
    }
    public void sendToBungeeCord(PermissionDocument dokument){
        if(Bukkit.getOnlineMode()) return;
        if(!Config.SYNCHRONISE_CHANNEL) return;
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF(dokument.toJson());
            if(Bukkit.getOnlinePlayers().size() == 0){
                System.out.println(Messages.SYSTEM_PREFIX+"Updater: Could not send data to bungeecord.");
                return;
            }
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendPluginMessage(BukkitBootstrap.getInstance(),"dkperms",b.toByteArray());
                return;
            }
        }catch (IOException exception){
            System.out.println(Messages.SYSTEM_PREFIX+"Updater: Could not send data to bungeecord.");
            System.out.println(Messages.SYSTEM_PREFIX+"Updater: Error - "+exception.getMessage());
        }
    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if(!Config.SYNCHRONISE_CHANNEL) return;
        if(channel.equalsIgnoreCase("dkperms")){
            ByteArrayInputStream b = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(b);

            Bukkit.getScheduler().runTaskAsynchronously(BukkitBootstrap.getInstance(),()->{
                try{
                    PermissionDocument dokument = PermissionDocument.get(in.readUTF());
                    if(dokument.getName().equalsIgnoreCase("group_create")){
                        UUID uuid = dokument.getObject("uuid",UUID.class);
                        PermissionUpdater.getInstance().onPermissionGroupCreate(uuid);
                        Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupCreateEvent(uuid,false));
                    }else if(dokument.getName().equalsIgnoreCase("group_delete")){
                        UUID uuid = dokument.getObject("uuid",UUID.class);
                        final PermissionGroup group = PermissionGroupManager.getInstance().getGroup(uuid);
                        PermissionUpdater.getInstance().onPermissionGroupDelete(dokument.getObject("uuid",UUID.class));
                        Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupDeleteEvent(group,false));
                    }else if(dokument.getName().equalsIgnoreCase("update")){
                        Boolean online = false;
                        UUID uuid = dokument.getObject("uuid",UUID.class);
                        PermissionType type = dokument.getObject("type",PermissionType.class);
                        PermissionUpdateData data = dokument.getObject("data",PermissionUpdateData.class);
                        if(type == PermissionType.PLAYER && Bukkit.getPlayer(uuid) != null) online = true;
                        PermissionUpdater.getInstance().onPermissionUpdate(type,uuid,online);
                        if(type == PermissionType.PLAYER){
                            Bukkit.getPluginManager().callEvent(new BukkitPermissionPlayerUpdateEvent(uuid,data,false,online));
                        }else{
                            Bukkit.getPluginManager().callEvent(new BukkitPermissionGroupUpdateEvent(uuid,data,false));
                        }
                    }else if(dokument.getName().equalsIgnoreCase("server")){
                        BukkitBootstrap.getInstance().setServerName(dokument.getString("server"));
                    }else System.out.println(Messages.SYSTEM_PREFIX+"Updater: Invalid update message.");
                }catch (Exception exception){
                    System.out.println(Messages.SYSTEM_PREFIX+"Updater: Error - "+exception.getMessage());
                }
            });
        }
    }
}
