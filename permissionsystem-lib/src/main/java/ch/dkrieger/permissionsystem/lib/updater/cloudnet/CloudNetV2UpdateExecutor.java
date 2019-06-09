package ch.dkrieger.permissionsystem.lib.updater.cloudnet;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 15:58
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandTeam;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateExecutor;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.utility.document.Document;

import java.util.UUID;

public class CloudNetV2UpdateExecutor implements PermissionUpdateExecutor{

    @Override
    public void executePermissionGroupCreate(UUID uuid) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("dkperms","group_create"
                ,new Document().append("uuid",uuid));
        CloudAPI.getInstance().sendCustomSubServerMessage("dkperms","group_create"
                ,new Document().append("uuid",uuid));
        CommandTeam.forceUpdate();
    }
    @Override
    public void executePermissionGroupDelete(PermissionGroup group) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("dkperms","group_delete"
                ,new Document().append("uuid",group.getUUID()));
        CloudAPI.getInstance().sendCustomSubServerMessage("dkperms","group_delete"
                ,new Document().append("uuid",group.getUUID()));
        CommandTeam.forceUpdate();
    }
    @Override
    public void executePermissionUpdate(PermissionType type, UUID uuid, PermissionUpdateData data) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("dkperms","update"
                ,new Document().append("type",type).append("uuid",uuid).append("data",data));
        CloudAPI.getInstance().sendCustomSubServerMessage("dkperms","update"
                ,new Document().append("type",type).append("uuid",uuid).append("data",data));
        CommandTeam.forceUpdate();
    }
}
