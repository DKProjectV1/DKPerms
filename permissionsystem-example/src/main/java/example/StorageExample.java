package example;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 17:09
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityProvider;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityStorage;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupStorage;
import ch.dkrieger.permissionsystem.lib.permission.PermissionProvider;
import ch.dkrieger.permissionsystem.lib.permission.PermissionStorage;
import ch.dkrieger.permissionsystem.lib.permission.data.PermissionData;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerStorage;
import ch.dkrieger.permissionsystem.lib.storage.StorageType;

import java.util.List;
import java.util.UUID;

public class StorageExample implements PermissionPlayerStorage, PermissionStorage, PermissionGroupStorage, PermissionEntityStorage{


    public StorageExample() {
        //set a new storage
        PermissionPlayerManager.getInstance().setStorage(this);
        PermissionGroupManager.getInstance().setStorage(this);
        PermissionProvider.getInstance().setStorage(this);
        PermissionEntityProvider.getInstance().setStorage(this);

        //Default storage types
        StorageType storage = Config.STORAGE_TYPE;
    }

    public PermissionPlayer getPermissionPlayer(UUID uuid) throws Exception {
        return null;
    }

    public PermissionPlayer getPermissionPlayer(String name) throws Exception {
        return null;
    }

    public PermissionPlayer createPermissionPlayer(UUID uuid, String name) {
        return null;
    }

    public void updateName(UUID uuid, String name) {

    }

    public List<PermissionGroupEntity> getPermissionEntities(PermissionType type, UUID uuid) {
        return null;
    }

    public void setEntity(PermissionType type, UUID uuid, UUID group, Long timeout) {

    }

    public void addEntity(PermissionType type, UUID uuid, UUID group, Long timeout) {

    }

    public void removeEntity(PermissionType type, UUID uuid, UUID group) {

    }

    public void clearEntity(PermissionType type, UUID uuid) {

    }

    public List<PermissionGroup> loadGroups() {
        return null;
    }

    public PermissionGroup createGroup(String name) {
        return null;
    }

    public void deleteGroup(UUID uuid) {

    }

    public void setSetting(UUID uuid, String identifier, Object value) {

    }

    public List<UUID> getPlayers(PermissionGroup group) {
        return null;
    }

    public PermissionData getPermissions(PermissionType type, UUID uuid) {
        return null;
    }

    public void addPermission(PermissionType type, UUID uuid, String permission, String world, Long timeout) {

    }

    public void addServerPermission(PermissionType type, UUID uuid, String server, String permission, String world, Long timeout) {

    }

    public void addServerGroupPermission(PermissionType type, UUID uuid, String group, String permission, String world, Long timeout) {

    }

    public void removePermission(PermissionType type, UUID uuid, String permission, String world) {

    }

    public void removeServerPermission(PermissionType type, UUID uuid, String server, String permission, String world) {

    }

    public void removeServerGroupPermission(PermissionType type, UUID uuid, String group, String permission, String world) {

    }

    public void clearAllPermissions(PermissionType type, UUID uuid) {

    }

    public void clearPermissions(PermissionType type, UUID uuid, String world) {

    }

    public void clearServerPermission(PermissionType type, UUID uuid, String server, String world) {

    }

    public void clearServerGroupPermission(PermissionType type, UUID uuid, String group, String world) {

    }

    public void onTimeOutDeleteTask() {

    }
}
