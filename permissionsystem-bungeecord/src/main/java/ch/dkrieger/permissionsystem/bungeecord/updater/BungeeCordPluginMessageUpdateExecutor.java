package ch.dkrieger.permissionsystem.bungeecord.updater;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 17:42
 *
 */

import ch.dkrieger.permissionsystem.bungeecord.BungeeCordBootstrap;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionGroupCreateEvent;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionGroupDeleteEvent;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionGroupUpdateEvent;
import ch.dkrieger.permissionsystem.bungeecord.event.ProxiedPermissionPlayerUpdateEvent;
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
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.UUID;

public class BungeeCordPluginMessageUpdateExecutor implements PermissionUpdateExecutor, Listener, Runnable{

    @Override
    public void executePermissionGroupCreate(UUID uuid) {
        sendToAllSpigotServers(new PermissionDocument("group_create").append("uuid",uuid));
        BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupCreateEvent(uuid,true));
        CommandTeam.forceupdate();
    }
    @Override
    public void executePermissionGroupDelete(PermissionGroup group) {
        sendToAllSpigotServers(new PermissionDocument("group_delete").append("uuid",group.getUUID()));
        BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupDeleteEvent(group,true));
        CommandTeam.forceupdate();
    }
    @Override
    public void executePermissionUpdate(PermissionType type, UUID uuid, PermissionUpdateData data) {
        sendToAllSpigotServers(new PermissionDocument("update").append("type",type).append("uuid",uuid).append("data",data));
        BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupUpdateEvent(uuid,data,true));
        CommandTeam.forceupdate();
    }
    @EventHandler
    public void onPluginMessageReceive(PluginMessageEvent event){
        if(!Config.SYNCHRONISE_CHANNEL) return;
        if(event.getTag().equalsIgnoreCase("dkperms:dkperms") && event.getSender() instanceof Server){
            ByteArrayInputStream b = new ByteArrayInputStream(event.getData());
            DataInputStream in = new DataInputStream(b);

            BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBootstrap.getInstance(),()->{
                try{
                    PermissionDocument document = PermissionDocument.get(in.readUTF());

                    if(document.getName().equalsIgnoreCase("group_create")){
                        UUID uuid = document.getObject("uuid",UUID.class);
                        PermissionUpdater.getInstance().onPermissionGroupCreate(uuid);
                        sendToAllSpigotServers(document,((Server) event.getSender()).getInfo().getName());
                        BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupCreateEvent(uuid,true));
                    }else if(document.getName().equalsIgnoreCase("group_delete")){
                        UUID uuid = document.getObject("uuid",UUID.class);
                        PermissionGroup group = PermissionGroupManager.getInstance().getGroup(uuid);
                        PermissionUpdater.getInstance().onPermissionGroupDelete(uuid);
                        sendToAllSpigotServers(document,((Server) event.getSender()).getInfo().getName());
                        BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupDeleteEvent(group,true));
                    }else if(document.getName().equalsIgnoreCase("update")){
                        Boolean online = false;
                        UUID uuid = document.getObject("uuid",UUID.class);
                        PermissionType type = document.getObject("type",PermissionType.class);
                        PermissionUpdateData data = document.getObject("data",PermissionUpdateData.class);
                        if(type == PermissionType.PLAYER && BungeeCord.getInstance().getPlayer(uuid) != null) online = true;
                        PermissionUpdater.getInstance().onPermissionUpdate(type,uuid,online);
                        sendToAllSpigotServers(document,((Server) event.getSender()).getInfo().getName());
                        if(type == PermissionType.GROUP){
                            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedPermissionGroupUpdateEvent(uuid,data,true));
                        }else{
                            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedPermissionPlayerUpdateEvent(uuid,data,true,online));
                        }
                    }else if(document.getName().equalsIgnoreCase("getserver")){
                        sendServer(((Server)event.getSender()).getInfo());
                    }else System.out.println(Messages.SYSTEM_PREFIX+" Updater: Invalid update message.");
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            });
        }
    }
    @EventHandler
    public void onServerConnected(ServerConnectedEvent event){
        if(event.getServer().getInfo().getPlayers().size() <= 0) sendServer(event.getServer().getInfo());
    }
    @Override
    public void run() {
        for(ServerInfo server : BungeeCord.getInstance().getServers().values()) sendServer(server);
    }
    private void sendToAllSpigotServers(PermissionDocument document){
        sendToAllSpigotServers(document,null);
    }
    private void sendToAllSpigotServers(PermissionDocument document, String from){
        for(ServerInfo server : BungeeCord.getInstance().getServers().values()){
            if(from != null && server.getName().equalsIgnoreCase(from)) return;
            sendToSpigotServer(server,document);
        }
    }
    private void sendToSpigotServer(ServerInfo server, PermissionDocument document){
        if(!Config.SYNCHRONISE_CHANNEL) return;
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF(document.toJson());
            server.sendData("dkperms:dkperms",b.toByteArray());
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
    private void sendServer(ServerInfo server){
        sendToSpigotServer(server,new PermissionDocument("server").append("server",server.getName()));
    }
}
