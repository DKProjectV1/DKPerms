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
    private final Boolean onthisserver;

    public BukkitPermissionGroupUpdateEvent(UUID uuid, PermissionUpdateData data, Boolean onthisserver) {
        super(Thread.currentThread().getId() != 1);
        this.uuid = uuid;
        this.data = data;
        this.onthisserver = onthisserver;
    }
    public PermissionGroup getGroup(){
        return PermissionGroupManager.getInstance().getGroup(this.uuid);
    }
    public PermissionUpdateData getData(){
        return this.data;
    }
    public Boolean isOnThisServer() {
        return onthisserver;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
