package ch.dkrieger.permissionsystem.lib.updater;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 15:56
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;

import java.util.UUID;

public interface PermissionUpdateExecutor {

    public void executePermissionGroupCreate(UUID uuid);

    public void executePermissionGroupDelete(PermissionGroup group);

    public void executePermissionUpdate(PermissionType type, UUID uuid, PermissionUpdateData data);
}
