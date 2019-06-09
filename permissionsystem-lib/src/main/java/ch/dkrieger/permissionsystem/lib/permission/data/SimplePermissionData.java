package ch.dkrieger.permissionsystem.lib.permission.data;

import ch.dkrieger.permissionsystem.lib.permission.PermissionEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class SimplePermissionData {

    private List<PermissionEntity> permissions;
    private Map<String,List<PermissionEntity>> worldPermissions;

    public SimplePermissionData() {
        this.permissions =  new ArrayList<>();
        this.worldPermissions =  new ConcurrentHashMap<>();
    }
    public List<PermissionEntity> getPermissions() {
        return permissions;
    }
    public Map<String, List<PermissionEntity>> getWorldPermissions() {
        return worldPermissions;
    }
    public List<PermissionEntity> getPermissions(String world) {
        if(this.worldPermissions.containsKey(world)) return this.worldPermissions.get(world);
        return new LinkedList<>();
    }
    public List<PermissionEntity> getAllPermissions(String world) {
        List<PermissionEntity> permissions = new LinkedList<>(this.permissions);
        if(world != null && this.worldPermissions.containsKey(world))
            permissions.addAll(this.worldPermissions.get(world));
        return permissions;
    }
    public void removePermission(String permission, String world){
        if(world != null){
            if(this.worldPermissions.containsKey(world)){
                for(PermissionEntity permissions : new LinkedList<>(this.worldPermissions.get(world))) if(permissions.getPermission().equalsIgnoreCase(permission)){
                    this.worldPermissions.get(world).remove(permissions);
                }
            }
        }else{
            for(PermissionEntity permissions : new LinkedList<>(this.permissions)) if(permissions.getPermission().equalsIgnoreCase(permission)){
                this.permissions.remove(permissions);
            }
        }
    }
}
