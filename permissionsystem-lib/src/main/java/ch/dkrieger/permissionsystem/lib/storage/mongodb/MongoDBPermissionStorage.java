package ch.dkrieger.permissionsystem.lib.storage.mongodb;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.01.19 09:33
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.permission.PermissionStorage;
import ch.dkrieger.permissionsystem.lib.permission.data.PermissionData;

import java.util.UUID;

public class MongoDBPermissionStorage implements PermissionStorage {

    private MongoDriver driver;

    @Override
    public PermissionData getPermissions(PermissionType type, UUID uuid) {
        return null;
    }

    @Override
    public void addPermission(PermissionType type, UUID uuid, String permission, String world, Long timeout) {

    }

    @Override
    public void addServerPermission(PermissionType type, UUID uuid, String server, String permission, String world, Long timeout) {
        if(type == PermissionType.GROUP){
        }else{

        }
    }

    @Override
    public void addServerGroupPermission(PermissionType type, UUID uuid, String group, String permission, String world, Long timeout) {

    }

    @Override
    public void removePermission(PermissionType type, UUID uuid, String permission, String world) {

    }

    @Override
    public void removeServerPermission(PermissionType type, UUID uuid, String server, String permission, String world) {

    }

    @Override
    public void removeServerGroupPermission(PermissionType type, UUID uuid, String group, String permission, String world) {

    }

    @Override
    public void clearAllPermissions(PermissionType type, UUID uuid) {

    }

    @Override
    public void clearPermissions(PermissionType type, UUID uuid, String world) {

    }

    @Override
    public void clearServerPermission(PermissionType type, UUID uuid, String server, String world) {

    }

    @Override
    public void clearServerGroupPermission(PermissionType type, UUID uuid, String group, String world) {

    }

    @Override
    public void onTimeOutDeleteTask() {

    }
}
