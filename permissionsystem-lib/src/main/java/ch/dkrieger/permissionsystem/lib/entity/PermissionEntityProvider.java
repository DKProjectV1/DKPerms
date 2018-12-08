package ch.dkrieger.permissionsystem.lib.entity;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class PermissionEntityProvider {

    private static PermissionEntityProvider instance;
    private PermissionEntityStorage storage;

    public PermissionEntityProvider(PermissionEntityStorage storage) {
        if(storage == null) throw new IllegalArgumentException("Storage can't be null.");
        instance = this;
        this.storage = storage;
    }
    public PermissionEntityStorage getStorage() {
        return storage;
    }
    public void setStorage(PermissionEntityStorage storage) {
        if(storage == null) throw new IllegalArgumentException("Storage can't be null.");
        this.storage = storage;
    }
    public static PermissionEntityProvider getInstance() {
        return instance;
    }
}
