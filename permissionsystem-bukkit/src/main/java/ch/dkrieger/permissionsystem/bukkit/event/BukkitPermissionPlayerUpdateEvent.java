package ch.dkrieger.permissionsystem.bukkit.event;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 13:53
 *
 */

import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class BukkitPermissionPlayerUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID uuid;
    private final PermissionUpdateData data;
    private final boolean onThisServer;
    private final boolean online;

    public BukkitPermissionPlayerUpdateEvent(UUID uuid, PermissionUpdateData data, boolean onThisServer, boolean online) {
        super(Thread.currentThread().getId() != 1);
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

    public boolean isOnThisServer() {
        return onThisServer;
    }

    public boolean isOnline() {
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
