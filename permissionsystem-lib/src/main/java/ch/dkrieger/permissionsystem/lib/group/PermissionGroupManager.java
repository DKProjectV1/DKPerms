package ch.dkrieger.permissionsystem.lib.group;

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityProvider;
import ch.dkrieger.permissionsystem.lib.permission.PermissionProvider;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;

import java.util.*;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class PermissionGroupManager {

    private static PermissionGroupManager INSTANCE;
    private PermissionGroupStorage storage;
    private List<PermissionGroup> groups;

    public PermissionGroupManager(PermissionGroupStorage storage){
        if(storage == null) throw new IllegalArgumentException("Storage can't be null.");
        INSTANCE = this;
        this.storage = storage;
        load();
    }
    public PermissionGroupStorage getStorage() {
        return storage;
    }

    public List<PermissionGroup> getGroups() {
        return groups;
    }

    public List<PermissionGroup> getSortedGroups() {
        Map<Integer,List<PermissionGroup>> grouplist = new TreeMap<>();
        for(PermissionGroup group : this.groups){
            if(!grouplist.containsKey(group.getPriority())) grouplist.put(group.getPriority(),new LinkedList<>());
            grouplist.get(group.getPriority()).add(group);
        }
        List<PermissionGroup> list = new ArrayList<>();
        for(Integer priority : grouplist.keySet()) list.addAll(grouplist.get(priority));
        return list;
    }

    public List<PermissionGroup> getDefaultGroups(){
        List<PermissionGroup> groups = new ArrayList<>();
        for(PermissionGroup group : this.groups) if(group.isDefault()) groups.add(group);
        return groups;
    }

    public PermissionGroup getHighestDefaultGroup(){
        Map<Integer,PermissionGroup> grouplist = new TreeMap<>();
        for(PermissionGroup group : this.groups) if(group.isDefault()) grouplist.put(group.getPriority(),group);
        for(Integer priority : grouplist.keySet()) return grouplist.get(priority);
        return null;
    }

    public List<PermissionPlayer> getPlayers(PermissionGroup group){
        List<PermissionPlayer> players = new ArrayList<>();
        for(UUID uuid : this.storage.getPlayers(group)){
            players.add(PermissionPlayerManager.getInstance().getPermissionPlayer(uuid));
        }
        return players;
    }

    public PermissionGroup getGroup(String name){
        for(PermissionGroup group : this.groups) if(group.getName().equalsIgnoreCase(name)) return group;
        return null;
    }

    public PermissionGroup getGroup(UUID uuid){
        for(PermissionGroup group : this.groups) if(group.getUUID().equals(uuid)) return group;
        return null;
    }

    public PermissionGroup getNextGroup(int priority){
        int current = priority;
        PermissionGroup group = null;
        for(PermissionGroup groups : this.groups){
            if(groups.getPriority() == priority) return groups;
            else if(groups.getPriority() < priority && groups.getPriority() > current){
                group = groups;
                current = groups.getPriority();
            }
        }
        return group;
    }

    public PermissionGroup getBeforeGroup(int priority){
        int current = priority;
        PermissionGroup group = null;
        for(PermissionGroup groups : this.groups){
            if(groups.getPriority() == priority) return groups;
            else if(groups.getPriority() > priority && groups.getPriority() < current){
                group = groups;
                current = groups.getPriority();
            }
        }
        return group;
    }

    public void setStorage(PermissionGroupStorage storage) {
        if(storage == null) throw new IllegalArgumentException("Storage can't be null.");
        this.storage = storage;
    }

    public PermissionGroup createGroup(String name){
        PermissionGroup group = getStorage().createGroup(name);
        this.groups.add(group);
        PermissionUpdater.getInstance().getExecutor().executePermissionGroupCreate(group.getUUID());
        return group;
    }

    public void deleteGroup(String name){
        deleteGroup(getGroup(name));
    }

    public void deleteGroup(PermissionGroup group){
        if(group == null) return;
        this.groups.remove(group);
        PermissionProvider.getInstance().getStorage().clearAllPermissions(PermissionType.GROUP,group.getUUID());
        PermissionEntityProvider.getInstance().getStorage().clearEntity(PermissionType.GROUP,group.getUUID());
        this.storage.deleteGroup(group.getUUID());
        PermissionUpdater.getInstance().getExecutor().executePermissionGroupDelete(group);
    }

    public void load(){
        final long timestamp = System.currentTimeMillis();
        this.groups = storage.loadGroups();
        loadPermissions();
        PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.INFO, PermissionSystem.PermissionDebugLevel.NORMAL,"loaded all groups in "+(System.currentTimeMillis()-timestamp)+"ms");
    }

    public void loadPermissions(){
        for(PermissionGroup group : this.groups) loadPermissions(group);
    }

    public void loadPermissions(PermissionGroup group){
        group.setPermissionData(PermissionProvider.getInstance().getStorage().getPermissions(PermissionType.GROUP,group.getUUID()));
        group.setGroups(PermissionEntityProvider.getInstance().getStorage().getPermissionEntities(PermissionType.GROUP,group.getUUID()));
    }

    public static PermissionGroupManager getInstance() {
        return INSTANCE;
    }
}
