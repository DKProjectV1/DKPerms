package ch.dkrieger.permissionsystem.lib.permission;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class PermissionProvider {

    private static PermissionProvider instance;
    private PermissionStorage storage;

    public PermissionProvider(PermissionStorage storage) {
        if(storage == null) throw new IllegalArgumentException("Storage can't be null.");
        instance = this;
        this.storage = storage;
    }
    public PermissionStorage getStorage() {
        return storage;
    }
    public void setStorage(PermissionStorage storage) {
        this.storage = storage;
    }
    public static PermissionProvider getInstance() {
        return instance;
    }
}
