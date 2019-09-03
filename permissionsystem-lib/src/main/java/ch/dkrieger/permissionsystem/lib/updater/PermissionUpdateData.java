package ch.dkrieger.permissionsystem.lib.updater;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.08.18 12:16
 *
 */

import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;

import java.util.concurrent.TimeUnit;

public class PermissionUpdateData {

    private PermissionUpdateCause cause;
    private PermissionUpdateOption option;
    private PermissionUpdateType type;
    private String value, value2, world;
    private Long duration;
    private TimeUnit unit;

    public PermissionUpdateData(PermissionUpdateCause cause, PermissionUpdateOption option, PermissionUpdateType type, String value, String value2, String world, Long duration, TimeUnit unit) {
        this.cause = cause;
        this.option = option;
        this.type = type;
        this.value = value;
        this.value2 = value2;
        this.world = world;
        this.duration = duration;
        this.unit = unit;
    }
    public PermissionUpdateCause getCause() {
        return cause;
    }

    public PermissionUpdateOption getOption() {
        return option;
    }

    public PermissionUpdateType getType() {
        return type;
    }

    public String getWorld() {
        return world;
    }

    public String getValue() {
        return value;
    }

    public String getValue2() {
        return value2;
    }

    public Long getDuration() {
        return duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public static PermissionUpdateData addPermission(String permission,String world, Long duration, TimeUnit unit){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.ADD,
                PermissionUpdateType.GLOBAL,permission,null,world,duration,unit);
    }

    public static PermissionUpdateData addServerPermission(String server, String permission,String world, Long duration, TimeUnit unit){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.ADD,
                PermissionUpdateType.SERVER,permission,server,world,duration,unit);
    }

    public static PermissionUpdateData addServerGroupPermission(String group, String permission,String world, Long duration, TimeUnit unit){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.ADD,
                PermissionUpdateType.SERVERGROUP,permission,group,world,duration,unit);
    }

    public static PermissionUpdateData removePermission(String permission,String world){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.REMOVE
                ,PermissionUpdateType.GLOBAL,permission,null,world,-1L,null);
    }

    public static PermissionUpdateData removeServerPermission(String server, String permission,String world){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.REMOVE
                ,PermissionUpdateType.GLOBAL,permission,server,world,-1L,null);
    }

    public static PermissionUpdateData removeServerGroupPermission(String group, String permission,String world){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.REMOVE
                ,PermissionUpdateType.GLOBAL,permission,group,world,-1L,null);
    }

    public static PermissionUpdateData clearAllPermission(){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.CLEAR
                ,PermissionUpdateType.ALL,null,null,null,-1L,null);
    }

    public static PermissionUpdateData clearPermission(String world){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.CLEAR
                ,PermissionUpdateType.GLOBAL,null,null,world,-1L,null);
    }

    public static PermissionUpdateData clearServerPermission(String server,String world){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.CLEAR
                ,PermissionUpdateType.GLOBAL,null,server,world,-1L,null);
    }

    public static PermissionUpdateData clearServerGroupPermission(String group,String world){
        return new PermissionUpdateData(PermissionUpdateCause.PERMISSION,PermissionUpdateOption.CLEAR
                ,PermissionUpdateType.GLOBAL,null,group,world,-1L,null);
    }

    public static PermissionUpdateData setGroup(PermissionGroup group, Long duration, TimeUnit unit){
        return new PermissionUpdateData(PermissionUpdateCause.GROUP,PermissionUpdateOption.SET,
                PermissionUpdateType.GLOBAL,group.getUUID().toString(),null,null,duration,unit);
    }

    public static PermissionUpdateData addGroup(PermissionGroup group, Long duration, TimeUnit unit){
        return new PermissionUpdateData(PermissionUpdateCause.GROUP,PermissionUpdateOption.ADD,
                PermissionUpdateType.GLOBAL,group.getUUID().toString(),null,null,duration,unit);
    }

    public static PermissionUpdateData removeGroup(PermissionGroup group){
        return new PermissionUpdateData(PermissionUpdateCause.GROUP,PermissionUpdateOption.REMOVE,
                PermissionUpdateType.GLOBAL,group.getUUID().toString(),null,null,-1L,null);
    }

    public static PermissionUpdateData clearGroup(){
        return new PermissionUpdateData(PermissionUpdateCause.GROUP,PermissionUpdateOption.CLEAR,
                PermissionUpdateType.GLOBAL,null,null,null,-1L,null);
    }

    public static PermissionUpdateData settingChange(String setting,String value){
        return new PermissionUpdateData(PermissionUpdateCause.GROUP,PermissionUpdateOption.SET,
                PermissionUpdateType.GLOBAL,setting,value,null,-1L,null);
    }
}
