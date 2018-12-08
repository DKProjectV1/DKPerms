package ch.dkrieger.permissionsystem.lib.updater;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandTeam;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;

import java.util.UUID;

public class PermissionUpdater {

    private static PermissionUpdater instance;
    private PermissionUpdateExecutor executor;

    public PermissionUpdater(PermissionUpdateExecutor executor) {
        instance = this;
        this.executor = executor;
    }
    public PermissionUpdateExecutor getExecutor() {
        return executor;
    }
    public void onPermissionGroupCreate(UUID uuid){
        PermissionSystem.getInstance().sync();
    }
    public void onPermissionGroupDelete(UUID uuid){
        PermissionGroup group = PermissionGroupManager.getInstance().getGroup(uuid);
        PermissionSystem.getInstance().sync();
        if(group.isTeam()) CommandTeam.forceupdate();
    }
    public void onPermissionUpdate(PermissionType type, UUID uuid,Boolean online){
        if(type == PermissionType.GROUP){
            PermissionSystem.getInstance().syncGroups();
        }else{
            if(online){
                try{
                    PermissionPlayerManager.getInstance().getPermissionPlayerSave(uuid);
                }catch (Exception exception){}
            }else{
                PermissionPlayerManager.getInstance().getLoadedPlayers().remove(uuid);
            }
        }
        CommandTeam.forceupdate();
    }
    public void setExecutor(PermissionUpdateExecutor executor) {
        this.executor = executor;
    }
    public static PermissionUpdater getInstance() {
        return instance;
    }
}
