package ch.dkrieger.permissionsystem.bungeecord.event;

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.07.18 17:14
 *
 */

public class ProxiedPermissionGroupUpdateEvent extends Event {

    private final UUID uuid;
    private final PermissionUpdateData data;
    private final Boolean onThisServer;

    public ProxiedPermissionGroupUpdateEvent(UUID uuid, PermissionUpdateData data, boolean onThisServer) {
        this.uuid = uuid;
        this.data = data;
        this.onThisServer = onThisServer;
    }
    public PermissionGroup getGroup(){
        return PermissionGroupManager.getInstance().getGroup(this.uuid);
    }
    public PermissionUpdateData getData() {
        return this.data;
    }
    public boolean isOnThisServer() {
        return onThisServer;
    }
}
