package ch.dkrieger.permissionsystem.bungeecord.event;

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import net.md_5.bungee.api.plugin.Event;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.07.18 17:14
 *
 */

public class ProxiedPermissionGroupDeleteEvent extends Event {

    private final PermissionGroup group;
    private final boolean onThisServer;

    public ProxiedPermissionGroupDeleteEvent(PermissionGroup group, Boolean onThisServer) {
        this.group = group;
        this.onThisServer = onThisServer;
    }
    public PermissionGroup getGroup(){
        return this.group;
    }
    public boolean isOnThisServer() {
        return onThisServer;
    }
}
