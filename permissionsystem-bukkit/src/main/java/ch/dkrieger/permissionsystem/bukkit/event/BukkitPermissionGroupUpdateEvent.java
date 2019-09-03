package ch.dkrieger.permissionsystem.bukkit.event;

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 13:57
 *
 */

public class BukkitPermissionGroupUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID uuid;
    private final PermissionUpdateData data;
    private final boolean onThisServer;

    public BukkitPermissionGroupUpdateEvent(UUID uuid, PermissionUpdateData data, boolean onThisServer) {
        super(Thread.currentThread().getId() != 1);
        this.uuid = uuid;
        this.data = data;
        this.onThisServer = onThisServer;
    }
    public PermissionGroup getGroup(){
        return PermissionGroupManager.getInstance().getGroup(this.uuid);
    }

    public PermissionUpdateData getData(){
        return this.data;
    }

    public boolean isOnThisServer() {
        return onThisServer;
    }
    @Override

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
