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
    private final Boolean onthisserver;

    public BukkitPermissionGroupDeleteEvent(PermissionGroup group, Boolean onthisserver) {
        super(true);
        this.group = group;
        this.onthisserver = onthisserver;
    }
    public PermissionGroup getGroup(){
        return this.group;
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
