package ch.dkrieger.permissionsystem.lib.permission.data;

import ch.dkrieger.permissionsystem.lib.permission.PermissionEntity;
import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class PermissionData extends SimplePermissionData {

    private Map<String,SimplePermissionData> serverPermissions, groupPermissions;

    public PermissionData() {
        this.serverPermissions = new ConcurrentHashMap<>();
        this.groupPermissions = new ConcurrentHashMap<>();
    }
    public Map<String, SimplePermissionData> getServerPermissions() {
        return serverPermissions;
    }
    public Map<String, SimplePermissionData> getGroupPermissions() {
        return groupPermissions;
    }
    public List<PermissionEntity> getAllPermissions(String server, String world){
        List<PermissionEntity> permissions = new LinkedList<>(getAllPermissions(world));
        if(server != null){
            String group = GeneralUtil.getGroup(server);
            if(this.serverPermissions.containsKey(server)){
                permissions.addAll(this.serverPermissions.get(server).getAllPermissions(world));
            }
            if(this.groupPermissions.containsKey(group)){
                permissions.addAll(this.groupPermissions.get(group).getAllPermissions(world));
            }
        }
        return permissions;
    }
}
