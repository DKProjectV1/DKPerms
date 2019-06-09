package ch.dkrieger.permissionsystem.lib.player;

import ch.dkrieger.permissionsystem.lib.PermissionAdapter;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class PermissionPlayer extends PermissionAdapter {

    private int id;

    public PermissionPlayer(int id, String name, UUID uuid) {
        super(name,uuid,PermissionType.PLAYER);
        this.id = id;
    }
    public int getID() {
        return this.id;
    }
    public String getColor(){
        PlayerDesign design = getPlayerDesign();
        if(design != null && !(design.getColor().equalsIgnoreCase("-1"))) return design.getColor();
        return "";
    }
    public PermissionGroup getHighestGroup(){
        List<PermissionGroup> groups = getSortedGroups();
        if(groups == null || groups.size() <= 0){
            return PermissionGroupManager.getInstance().getHighestDefaultGroup();
        }else return groups.get(0);
    }
    public int getJoinPower(){
        int joinpower = 0;
        for(PermissionGroupEntity entity : this.groups){
            if(!entity.hasTimeOut()){
                PermissionGroup group = entity.getGroup();
                if(group != null && joinpower < group.getJoinpower()) joinpower = group.getJoinpower();
            }
        }
        return joinpower;
    }
    public PlayerDesign getPlayerDesign(){
        PermissionGroup group = getHighestGroup();
        if(group != null) return group.getPlayerDesign();
        return PermissionGroupManager.getInstance().getHighestDefaultGroup().getPlayerDesign();
    }
    public boolean isInGroup(String group){
        return isInGroup(PermissionGroupManager.getInstance().getGroup(group));
    }
    public Boolean isInGroup(PermissionGroup group){
        if(group == null) return false;
        for(PermissionGroupEntity entitiy : this.groups){
            if(!entitiy.hasTimeOut() && entitiy.getGroupUUID().equals(group.getUUID())) return true;
        }
        return false;
    }
    public void setName(String name){
        this.name = name;
    }
    public PermissionGroup promote(){
        return promote(-1L,null);
    }
    public PermissionGroup promote(Long duration, TimeUnit unit){
        PermissionGroup group = getHighestGroup();
        if(group != null){
            group = PermissionGroupManager.getInstance().getNextGroup(group.getPriority()-1);
            if(group != null){
                if(duration < 0 || unit == null) setGroup(group);
                else addGroup(group,duration,unit);
            }
        }
        return group;
    }
    public PermissionGroup demote(){
        return demote(-1L,null);
    }
    public PermissionGroup demote(Long duration, TimeUnit unit){
        PermissionGroup group = getHighestGroup();
        if(group != null){
            group = PermissionGroupManager.getInstance().getBeforeGroup(group.getPriority()+1);
            if(group != null){
                if(duration < 0 || unit == null) setGroup(group);
                else addGroup(group,duration,unit);
            }
        }
        return group;
    }
}