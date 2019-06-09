package ch.dkrieger.permissionsystem.lib;

import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityProvider;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.permission.PermissionEntity;
import ch.dkrieger.permissionsystem.lib.permission.PermissionProvider;
import ch.dkrieger.permissionsystem.lib.permission.data.PermissionData;
import ch.dkrieger.permissionsystem.lib.permission.data.SimplePermissionData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;
import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class PermissionAdapter {

    protected String name;
    protected UUID uuid;
    protected PermissionType type;
    protected PermissionData permissiondata;
    protected List<PermissionGroupEntity> groups;

    public PermissionAdapter(String name, UUID uuid, PermissionType type) {
        this(name,uuid,type,new PermissionData(),new ArrayList<>());
    }
    public PermissionAdapter(String name, UUID uuid, PermissionType type, PermissionData permissiondata, List<PermissionGroupEntity> groups) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
        this.permissiondata = permissiondata;
        this.groups = groups;
    }
    public String getName() {
        return name;
    }
    public UUID getUUID() {
        return uuid;
    }
    public PermissionType getType() {
        return type;
    }
    public PermissionData getPermissionData() {
        return permissiondata;
    }
    public List<PermissionGroupEntity> getGroups() {
        return groups;
    }
    public List<PermissionGroup> getSortedGroups() {
        Map<Integer,List<PermissionGroup>> grouplist = new TreeMap<>();
        for(PermissionGroupEntity entity: this.groups){
            if(!entity.hasTimeOut()){
                PermissionGroup group = entity.getGroup();
                if(group == null) continue;
                if(!grouplist.containsKey(group.getPriority())) grouplist.put(group.getPriority(),new ArrayList<>());
                grouplist.get(group.getPriority()).add(group);
            }
        }
        List<PermissionGroup> list = new ArrayList<>();
        for(Integer priority : grouplist.keySet()) list.addAll(grouplist.get(priority));
        return list;
    }
    public List<PermissionGroupEntity> getSortedGroupEntities() {
        Map<Integer,List<PermissionGroupEntity>> groupList = new TreeMap<>();

        GeneralUtil.iterateAcceptedForEach(this.groups, object -> !object.hasTimeOut(), object -> {
            PermissionGroup group = object.getGroup();
            if(group != null){
                if(!groupList.containsKey(group.getPriority())) groupList.put(group.getPriority(),new ArrayList<>());
                groupList.get(group.getPriority()).add(object);
            }
        });

        List<PermissionGroupEntity> list = new ArrayList<>();
        GeneralUtil.iterateForEach(groupList.keySet(), object -> list.addAll(groupList.get(object)));
        return list;
    }
    public List<PermissionEntity> getPermissions(){
        return getPermissions(null);
    }
    public List<PermissionEntity> getPermissions(String server){
        return getPermissions(server,null);
    }
    public List<PermissionEntity> getPermissions(String server, String world) {
        return this.permissiondata.getAllPermissions(server, world);
    }
    public List<PermissionEntity> getAllPermissions(String server, String world){
        return getAllPermissions(server, world,new ArrayList<>());
    }
    public List<PermissionEntity> getAllPermissions(String server, String world, List<UUID> checked){
        checked.add(this.uuid);
        List<PermissionEntity> permissions = new ArrayList<>(this.permissiondata.getAllPermissions(server,world));
        try{
            GeneralUtil.iterateAcceptedForEach(this.groups, object -> !checked.contains(object.getGroupUUID()), object -> {
                checked.add(object.getGroupUUID());
                if(!object.hasTimeOut()){
                    PermissionGroup group = object.getGroup();
                    if(group != null) permissions.addAll(group.getAllPermissions(server,world,checked));
                }
            });
            GeneralUtil.iterateAcceptedForEach(PermissionGroupManager.getInstance().getDefaultGroups(), object -> !(checked.contains(object.getUUID())), object -> {
                checked.add(object.getUUID());
                permissions.addAll(object.getAllPermissions(server,world,checked));
            });
        }catch (Exception exception){}
        return permissions;
    }
    public Boolean hasPermission(String permission){
        return hasPermission(permission,null,null);
    }
    public Boolean hasPermission(String permission, String server){
        return hasPermission(permission,server,null);
    }
    public Boolean hasPermission(String permission, String server, String world){
        PermissionSystem.getInstance().debug(PermissionSystem.PermissionDebugLevel.HIGH
                ,"checking permission "+permission+"["+server+"/"+world+"] for "+getName());
        Boolean has = false;
        permission = permission.toLowerCase();
        if(server != null) server = server.toLowerCase();
        if(world != null) world = world.toLowerCase();

        Iterator<PermissionEntity> iterator = getAllPermissions(server,world).iterator();
        PermissionEntity permissions;
        while(iterator.hasNext() && (permissions=iterator.next()) != null){
            if(!permissions.hasTimeOut()){
                if(permissions.getPermission().equalsIgnoreCase("*")){
                    has = true;
                    continue;
                }else if(permissions.getPermission().equalsIgnoreCase("-*")) return false;
                Boolean hasthis = false;
                Boolean negative = false;
                String perm = permissions.getPermission();
                if(perm.startsWith("-")){
                    negative = true;
                    perm = perm.replaceFirst("-","");
                }
                if(perm.endsWith(".*")){
                    perm = perm.replace("*","");//dkbans.   dkbans.test
                    if(permission.startsWith(perm.toLowerCase())) hasthis = true;
                }else if(perm.equalsIgnoreCase(permission)) hasthis = true;
                if(hasthis) has = hasthis;
                if(negative && hasthis) return false;
            }
        }
        return has;
    }
    public Boolean isInGroup(PermissionGroup group){
        for(PermissionGroup groups : getSortedGroups()) if(groups.getUUID().equals(group)) return true;
        return false;
    }
    public void addPermission(String permission){
        addPermission(permission,-1L,TimeUnit.SECONDS);
    }
    public void addPermission(String permission, String world){
        addPermission(permission,world,-1L,TimeUnit.SECONDS);
    }
    public void addPermission(String permission, Long duration, TimeUnit unit){
        addPermission(permission,null,duration,unit);
    }
    public void addPermission(String permission, String world, Long duration, TimeUnit unit){
        Long timeout = -1L;
        if(world != null) world = world.toLowerCase();
        if(duration != null && duration > 0 && unit != null) timeout = System.currentTimeMillis()+unit.toMillis(duration);
        if(world == null) this.permissiondata.getPermissions().add(new PermissionEntity(timeout,permission));
        else{
            if(!this.permissiondata.getWorldPermissions().containsKey(world))
                this.permissiondata.getWorldPermissions().put(world,new ArrayList<>());
            this.permissiondata.getWorldPermissions().get(world).add(new PermissionEntity(timeout,permission));
        }
        PermissionProvider.getInstance().getStorage().addPermission(this.type,this.uuid,permission,world,timeout);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.addPermission(permission,world,duration,unit));
    }
    public void addServerPermission(String server,String permission){
        addServerPermission(server,permission,-1L,TimeUnit.SECONDS);
    }
    public void addServerPermission(String server,String permission,String world){
        addServerPermission(server,permission,world,-1L,TimeUnit.SECONDS);
    }
    public void addServerPermission(String server,String permission,Long duration,TimeUnit unit){
        addServerPermission(server,permission,null,duration, unit);
    }
    public void addServerPermission(String server,String permission,String world, Long duration, TimeUnit unit){
        if(server != null) server = server.toLowerCase();
        if(world != null) world = world.toLowerCase();
        Long timeout = -1L;
        if(duration != null && duration > 0 && unit != null) timeout = System.currentTimeMillis()+unit.toMillis(duration);
        if(!this.permissiondata.getServerPermissions().containsKey(server))
            this.permissiondata.getServerPermissions().put(server,new SimplePermissionData());
        if(world == null) this.permissiondata.getServerPermissions().get(server).getPermissions()
                .add(new PermissionEntity(timeout,permission));
        else{
            if(!this.permissiondata.getServerPermissions().get(server).getWorldPermissions().containsKey(world))
                this.permissiondata.getServerPermissions().get(server).getWorldPermissions().put(world,new ArrayList<>());
            this.permissiondata.getServerPermissions().get(server).getWorldPermissions().get(world).add(new PermissionEntity(timeout,permission));
        }
        PermissionProvider.getInstance().getStorage().addServerPermission(this.type,this.uuid,server,permission,world,timeout);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.addServerPermission(server,permission,world,duration,unit));
    }
    public void addServerGroupPermission(String group,String permission){
        addServerGroupPermission(group,permission,-1L,TimeUnit.SECONDS);
    }
    public void addServerGroupPermission(String group,String permission,String world){
        addServerGroupPermission(group,permission,world,-1L,TimeUnit.SECONDS);
    }
    public void addServerGroupPermission(String group,String permission,Long duration,TimeUnit unit){
        addServerGroupPermission(group,permission,null,duration, unit);
    }
    public void addServerGroupPermission(String group,String permission,String world, Long duration, TimeUnit unit){
        if(group != null) group = group.toLowerCase();
        if(world != null) world = world.toLowerCase();
        Long timeout = -1L;
        if(duration != null && duration > 0 && unit != null) timeout = System.currentTimeMillis()+unit.toMillis(duration);
        if(!this.permissiondata.getGroupPermissions().containsKey(group))
            this.permissiondata.getGroupPermissions().put(group,new SimplePermissionData());
        if(world == null) this.permissiondata.getGroupPermissions().get(group).getPermissions()
                .add(new PermissionEntity(timeout,permission));
        else{
            if(!this.permissiondata.getGroupPermissions().get(group).getWorldPermissions().containsKey(world))
                this.permissiondata.getGroupPermissions().get(group).getWorldPermissions().put(world,new ArrayList<>());
            this.permissiondata.getGroupPermissions().get(group).getWorldPermissions().get(world).add(new PermissionEntity(timeout,permission));
        }
        PermissionProvider.getInstance().getStorage().addServerGroupPermission(this.type,this.uuid,group,permission,world,timeout);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.addServerGroupPermission(group,permission,world,duration,unit));
    }
    public void removePermission(String permission){
        removePermission(permission,null);
    }
    public void removePermission(String permission, String world){
        if(world != null) world = world.toLowerCase();
        this.permissiondata.removePermission(permission,world);
        PermissionProvider.getInstance().getStorage().removePermission(this.type,this.uuid,permission,world);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.removePermission(permission,world));
    }
    public void removeServerPermission(String server, String permission){
        removeServerPermission(server,permission,null);
    }
    public void removeServerPermission(String server, String permission, String world){
        if(server != null) server = server.toLowerCase();
        if(world != null) world = world.toLowerCase();
        if(this.permissiondata.getServerPermissions().containsKey(server)){
            this.permissiondata.getServerPermissions().get(server).removePermission(permission,world);
        }
        PermissionProvider.getInstance().getStorage().removeServerPermission(this.type,this.uuid,server,permission,world);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.removeServerPermission(server,permission,world));
    }
    public void removeServerGroupPermission(String group, String permission){
        removeServerGroupPermission(group,permission,null);
    }
    public void removeServerGroupPermission(String group, String permission, String world){
        if(group != null) group = group.toLowerCase();
        if(world != null) world = world.toLowerCase();
        if(this.permissiondata.getGroupPermissions().containsKey(group)){
            this.permissiondata.getGroupPermissions().get(group).removePermission(permission,world);
        }
        PermissionProvider.getInstance().getStorage().removeServerGroupPermission(this.type,this.uuid,group,permission,world);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.removeServerGroupPermission(group,permission,world));
    }
    public void clearAllPermissions(){
        this.permissiondata = new PermissionData();
        PermissionProvider.getInstance().getStorage().clearAllPermissions(this.type,this.uuid);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.clearAllPermission());
    }
    public void clearPermissions(){
        clearPermissions(null);
    }
    public void clearPermissions(String world){
        if(world != null) world = world.toLowerCase();
        if(world == null){
            this.permissiondata.getPermissions().clear();
            this.permissiondata.getWorldPermissions().clear();
        }else{
            if(this.permissiondata.getWorldPermissions().containsKey(world))
                this.permissiondata.getWorldPermissions().remove(world);
        }
        PermissionProvider.getInstance().getStorage().clearPermissions(this.type,this.uuid,world);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.clearPermission(world));
    }
    public void clearServerPermissions(){
        clearServerGroupPermissions(null);
    }
    public void clearServerPermissions(String server){
        clearServerPermissions(server,null);
    }
    public void clearServerPermissions(String server, String world){
        if(server != null) server = server.toLowerCase();
        if(world != null) world = world.toLowerCase();
        if(server == null) this.permissiondata.getServerPermissions().clear();
        else if(this.permissiondata.getServerPermissions().containsKey(server)){
            if(world == null) this.permissiondata.getServerPermissions().remove(server);
            else{
                if(this.permissiondata.getServerPermissions().get(server).getWorldPermissions().containsKey(world))
                    this.permissiondata.getServerPermissions().get(server).getWorldPermissions().remove(world);
            }
        }
        PermissionProvider.getInstance().getStorage().clearServerPermission(this.type,this.uuid,server,world);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.clearServerPermission(server,world));
    }
    public void clearServerGroupPermissions(){
        clearServerGroupPermissions(null);
    }
    public void clearServerGroupPermissions(String group){
        clearServerGroupPermissions(group,null);
    }
    public void clearServerGroupPermissions(String group, String world){
        if(group != null) group = group.toLowerCase();
        if(world != null) world = world.toLowerCase();
        if(group == null) this.permissiondata.getGroupPermissions().clear();
        else if(this.permissiondata.getGroupPermissions().containsKey(group)){
            if(world == null) this.permissiondata.getGroupPermissions().remove(group);
            else{
                if(this.permissiondata.getGroupPermissions().get(group).getWorldPermissions().containsKey(world))
                    this.permissiondata.getGroupPermissions().get(group).getWorldPermissions().remove(world);
            }
        }
        PermissionProvider.getInstance().getStorage().clearServerGroupPermission(this.type,this.uuid,group,world);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.clearServerGroupPermission(group,world));
    }
    public void setGroup(PermissionGroup group){
        setGroup(group,null,null);
    }
    public void setGroup(PermissionGroup group,Long duration,TimeUnit unit){
        Long timeout = -1L;
        if(duration != null && duration > 0 && unit != null) timeout = System.currentTimeMillis()+unit.toMillis(duration);
        this.groups.clear();
        this.groups.add(new PermissionGroupEntity(timeout,group.getUUID()));
        PermissionEntityProvider.getInstance().getStorage().setEntity(this.type,this.uuid,group.getUUID(),timeout);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.setGroup(group,duration,unit));
    }
    public void addGroup(PermissionGroup group){
        addGroup(group,null,null);
    }
    public void addGroup(PermissionGroup group,Long duration,TimeUnit unit){
        Long timeout = -1L;
        if(duration != null && duration > 0 && unit != null) timeout = System.currentTimeMillis()+unit.toMillis(duration);
        this.groups.add(new PermissionGroupEntity(timeout,group.getUUID()));
        PermissionEntityProvider.getInstance().getStorage().addEntity(this.type,this.uuid,group.getUUID(),timeout);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.addGroup(group,duration,unit));
    }
    public void removeGroup(PermissionGroup group){
        for(PermissionGroupEntity entity : new ArrayList<>(this.groups)) if(entity.getGroupUUID().equals(group.getUUID())) this.groups.remove(entity);
        PermissionEntityProvider.getInstance().getStorage().removeEntity(this.type,this.uuid,group.getUUID());
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.removeGroup(group));
    }
    public void clearGroups(){
        this.groups.clear();
        PermissionEntityProvider.getInstance().getStorage().clearEntity(this.type,this.uuid);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid
                ,PermissionUpdateData.clearGroup());
    }
    public void setPermissionData(PermissionData permissiondata) {
        this.permissiondata = permissiondata;
    }
    public void setGroups(List<PermissionGroupEntity> groups) {
        this.groups = groups;
    }
}
