package ch.dkrieger.permissionsystem.bungeecord.event;

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.07.18 17:14
 *
 */

public class ProxiedPermissionGroupCreateEvent extends Event {

    private final UUID uuid;
    private final boolean onThisServer;

    public ProxiedPermissionGroupCreateEvent(UUID uuid, Boolean onThisServer) {
        this.uuid = uuid;
        this.onThisServer = onThisServer;
    }
    public PermissionGroup getGroup(){
        return PermissionGroupManager.getInstance().getGroup(this.uuid);
    }
    public boolean isOnThisServer() {
        return onThisServer;
    }
}
