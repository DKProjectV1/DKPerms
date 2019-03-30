package ch.dkrieger.permissionsystem.lib.updater.cloudnet;

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandTeam;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateExecutor;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.wrapper.Wrapper;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 08.12.18 16:51
 *
 */

public class CloudNetV3UpdateExecutor implements PermissionUpdateExecutor{

    @Override
    public void executePermissionGroupCreate(UUID uuid) {
        Wrapper.getInstance().sendChannelMessage("dkperms","group_create"
                ,new JsonDocument().append("uuid",uuid));
    }
    @Override
    public void executePermissionGroupDelete(PermissionGroup group) {
        Wrapper.getInstance().sendChannelMessage("dkperms","group_delete"
                ,new JsonDocument().append("uuid",group.getUUID()));
        CommandTeam.forceupdate();
    }
    @Override
    public void executePermissionUpdate(PermissionType type, UUID uuid, PermissionUpdateData data) {
        Wrapper.getInstance().sendChannelMessage("dkperms","update"
                ,new JsonDocument().append("type",type).append("uuid",uuid).append("data",data));
        CommandTeam.forceupdate();
    }
}
