package example;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 17:20
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateExecutor;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;

import java.util.UUID;

public class UpdaterExample implements PermissionUpdateExecutor{

    public UpdaterExample() {

        PermissionUpdater.getInstance().setExecutor(this);

        //execute an incoming update
        PermissionUpdater.getInstance().onPermissionGroupDelete(null);
    }
    //send update through the network
    public void executePermissionGroupCreate(UUID uuid) {

    }
    public void executePermissionGroupDelete(PermissionGroup group) {

    }

    @Override
    public void executePermissionUpdate(PermissionType type, UUID uuid, PermissionUpdateData data) {

    }

}
