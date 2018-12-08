package ch.dkrieger.permissionsystem.bukkit.event;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 13:53
 *
 */

import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateCause;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class BukkitPermissionPlayerUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID uuid;
    private final PermissionUpdateData data;
    private final Boolean onthisserver;
    private final Boolean online;

    public BukkitPermissionPlayerUpdateEvent(UUID uuid, PermissionUpdateData data, Boolean onthisserver, Boolean online) {
        this.uuid = uuid;
        this.data = data;
        this.onthisserver = onthisserver;
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
        return onthisserver;
    }
    public Boolean isOnline() {
        return online;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
