package ch.dkrieger.permissionsystem.lib.permission;

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.permission.data.PermissionData;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public interface PermissionStorage {

    public PermissionData getPermissions(PermissionType type, UUID uuid);

    //add permissions
    public void addPermission(PermissionType type, UUID uuid, String permission, String world,Long timeout);

    public void addServerPermission(PermissionType type, UUID uuid,String server, String permission, String world,Long timeout);

    public void addServerGroupPermission(PermissionType type, UUID uuid,String group, String permission, String world,Long timeout);

    //remove
    public void removePermission(PermissionType type, UUID uuid,String permission,String world);

    public void removeServerPermission(PermissionType type, UUID uuid,String server, String permission, String world);

    public void removeServerGroupPermission(PermissionType type, UUID uuid,String group, String permission, String world);

    //clear
    public void clearAllPermissions(PermissionType type, UUID uuid);

    public void clearPermissions(PermissionType type, UUID uuid, String world);

    public void clearServerPermission(PermissionType type, UUID uuid, String server,String world);

    public void clearServerGroupPermission(PermissionType type, UUID uuid, String group,String world);

    public void onTimeOutDeleteTask();

}
