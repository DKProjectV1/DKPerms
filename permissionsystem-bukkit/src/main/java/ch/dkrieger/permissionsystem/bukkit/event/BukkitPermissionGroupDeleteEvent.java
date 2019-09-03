package ch.dkrieger.permissionsystem.bukkit.event;

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 13:58
 *
 */

public class BukkitPermissionGroupDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PermissionGroup group;
    private final boolean onThisServer;

    public BukkitPermissionGroupDeleteEvent(PermissionGroup group, boolean onThisServer) {
        super(Thread.currentThread().getId() != 1);
        this.group = group;
        this.onThisServer = onThisServer;
    }
    public PermissionGroup getGroup(){
        return this.group;
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
