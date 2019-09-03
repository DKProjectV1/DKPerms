package ch.dkrieger.permissionsystem.lib.entity;

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;

import java.util.List;
import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public interface PermissionEntityStorage {

    List<PermissionGroupEntity> getPermissionEntities(PermissionType type, UUID uuid);

    void setEntity(PermissionType type, UUID uuid, UUID group, Long timeout);

    void addEntity(PermissionType type, UUID uuid, UUID group, Long timeout);

    void removeEntity(PermissionType type, UUID uuid, UUID group);

    void clearEntity(PermissionType type, UUID uuid);

}
