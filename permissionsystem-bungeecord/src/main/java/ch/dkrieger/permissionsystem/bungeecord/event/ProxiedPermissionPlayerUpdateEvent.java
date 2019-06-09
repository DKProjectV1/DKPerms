package ch.dkrieger.permissionsystem.bungeecord.event;

import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.07.18 17:14
 *
 */

public class ProxiedPermissionPlayerUpdateEvent extends Event {

    private final UUID uuid;
    private final PermissionUpdateData data;
    private final boolean onThisServer, online;

    public ProxiedPermissionPlayerUpdateEvent(UUID uuid, PermissionUpdateData data, Boolean onThisServer, Boolean online) {
        this.uuid = uuid;
        this.data = data;
        this.onThisServer = onThisServer;
        this.online = online;
    }
    public UUID getUUID() {
        return uuid;
    }
    public PermissionPlayer getPlayer() {
        return PermissionPlayerManager.getInstance().getPermissionPlayer(this.uuid);
    }
    public PermissionUpdateData getData(){
        return this.data;
    }
    public Boolean isOnThisServer() {
        return onThisServer;
    }
    public Boolean isOnline() {
        return online;
    }
}
