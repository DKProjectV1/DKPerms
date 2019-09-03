package ch.dkrieger.permissionsystem.lib.permission;

import ch.dkrieger.permissionsystem.lib.entity.SimpleEntity;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 04.07.18 18:38
 *
 */

public class PermissionEntity extends SimpleEntity {

    private final String permission;

    public PermissionEntity(Long timeout, String permission) {
        super(timeout);
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
