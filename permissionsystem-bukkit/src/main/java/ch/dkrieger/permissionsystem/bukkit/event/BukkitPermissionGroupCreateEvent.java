package ch.dkrieger.permissionsystem.bukkit.event;

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 13:58
 *
 */

public class BukkitPermissionGroupCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID uuid;
    private final Boolean onthisserver;

    public BukkitPermissionGroupCreateEvent(UUID uuid, Boolean onthisserver) {
        super(Thread.currentThread().getId() != 1);
        this.uuid = uuid;
        this.onthisserver = onthisserver;
    }
    public PermissionGroup getGroup(){
        return PermissionGroupManager.getInstance().getGroup(this.uuid);
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
