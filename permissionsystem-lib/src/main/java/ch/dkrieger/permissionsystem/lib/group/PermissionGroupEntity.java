package ch.dkrieger.permissionsystem.lib.group;

import ch.dkrieger.permissionsystem.lib.entity.SimpleEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.utils.NetworkUtil;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 04.07.18 18:38
 *
 */

public class PermissionGroupEntity extends SimpleEntity {

    private final UUID group;

    public PermissionGroupEntity(Long timeout, UUID group) {
        super(timeout);
        this.group = group;
    }
    public UUID getGroupUUID() {
        return group;
    }
    public PermissionGroup getGroup(){
        return PermissionGroupManager.getInstance().getGroup(this.group);
    }
}
