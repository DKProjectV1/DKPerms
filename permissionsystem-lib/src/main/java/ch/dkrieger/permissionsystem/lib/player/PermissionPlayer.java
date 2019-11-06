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

    private final int id;

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

    public PermissionGroupEntity getHighestGroupEntity(){
        List<PermissionGroupEntity> groups = getSortedGroupEntities();
        if(groups != null && groups.size() > 0) return groups.get(0);
        return null;
    }

    public int getJoinPower(){
        int joinPower = 0;
        for(PermissionGroupEntity entity : this.groups){
            if(!entity.hasTimeOut()){
                PermissionGroup group = entity.getGroup();
                if(group != null && joinPower < group.getJoinpower()) joinPower = group.getJoinpower();
            }
        }
        return joinPower;
    }

    public PlayerDesign getPlayerDesign(){
        PermissionGroup group = getHighestGroup();
        if(group != null) return group.getPlayerDesign();
        PermissionGroup defaultGroup =  PermissionGroupManager.getInstance().getHighestDefaultGroup();
        if(defaultGroup != null) return defaultGroup.getPlayerDesign();
        return PlayerDesign.EMPTY;
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