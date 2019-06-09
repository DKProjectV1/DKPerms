package ch.dkrieger.permissionsystem.lib.group;

import ch.dkrieger.permissionsystem.lib.PermissionAdapter;
import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.permission.PermissionEntity;
import ch.dkrieger.permissionsystem.lib.permission.data.SimplePermissionData;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PlayerDesign;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateData;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class PermissionGroup extends PermissionAdapter{

    private String description;
    private boolean defaultGroup, team;
    private int priority, joinPower, tsGroupId;
    private PlayerDesign playerdesign;

    public PermissionGroup(String name, UUID uuid, String description, boolean defaultGroup, boolean team, int priority, int tsGroupId, int joinPower, PlayerDesign playerdesign) {
        super(name, uuid, PermissionType.GROUP);
        this.description = description;
        this.defaultGroup = defaultGroup;
        this.team = team;
        this.priority = priority;
        this.tsGroupId = tsGroupId;
        this.joinPower = joinPower;
        this.playerdesign = playerdesign;
    }
    public String getDescription() {
        return description;
    }
    public String getColor(){
        if(this.playerdesign.getColor().equalsIgnoreCase("-1")) return "";
        else return this.playerdesign.getColor();
    }
    public int getPriority() {
        return priority;
    }
    public int getJoinpower() {
        return joinPower;
    }
    public int getTsgroupID() {
        return tsGroupId;
    }
    public PlayerDesign getPlayerDesign() {
        return playerdesign;
    }
    public List<PermissionPlayer> getPlayers(){
        return PermissionGroupManager.getInstance().getPlayers(this);
    }
    public boolean isDefault(){
        return this.defaultGroup;
    }
    public boolean isTeam(){
        return this.team;
    }
    public void setName(String name){
        this.name = name;
        setSetting("name",name);
    }
    public void setDescription(String description) {
        description = PermissionSystem.getInstance().getPlatform().translateColorCodes(description);
        if(this.description.equals(description)) return;
        this.description = description;
        setSetting("description",description);
    }
    public void setDefault(boolean defaultGroup) {
        if(this.defaultGroup == defaultGroup) return;
        this.defaultGroup = defaultGroup;
        setSetting("default",defaultGroup);
    }
    public void setTeam(boolean team) {
        if(this.team == team) return;
        this.team = team;
        setSetting("team",team);
    }
    public void setPriority(int priority) {
        if(this.priority == priority) return;
        this.priority = priority;
        setSetting("priority",priority);
    }
    public void setJoinPower(int joinPower) {
        if(this.joinPower == joinPower) return;
        this.joinPower = joinPower;
        setSetting("joinPower",joinPower);
    }
    public void setTsGroupID(int tsGroupId) {
        if(this.tsGroupId == tsGroupId) return;
        this.tsGroupId = tsGroupId;
        setSetting("tsGroupId",tsGroupId);
    }
    public void setPrefix(String prefix){
        prefix = PermissionSystem.getInstance().getPlatform().translateColorCodes(prefix);
        this.playerdesign.setPrefix(prefix);
        setSetting("prefix",prefix);
    }
    public void setSuffix(String suffix){
        suffix = PermissionSystem.getInstance().getPlatform().translateColorCodes(suffix);
        this.playerdesign.setSuffix(suffix);
        setSetting("suffix",suffix);
    }
    public void setDisplay(String display){
        display = PermissionSystem.getInstance().getPlatform().translateColorCodes(display);
        this.playerdesign.setDisplay(display);
        setSetting("display",display);
    }
    public void setColor(String color){
        color = PermissionSystem.getInstance().getPlatform().translateColorCodes(color);
        String exactcolor = "";
        boolean next = false;
        for(char c : color.toCharArray()) {
            if(c == '§'){
                if(!next){
                    exactcolor += c;
                    next = true;
                }
            }else if(next){
                exactcolor += c;
                next = false;
            }
        }
        if(exactcolor.endsWith("&")) exactcolor = exactcolor.substring(0,exactcolor.length()-1);
        if(exactcolor.length() == 1 && exactcolor.startsWith("&")) exactcolor = "";
        this.playerdesign.setColor(exactcolor);
        setSetting("color",exactcolor);
    }
    public void copy(PermissionGroup copy){
        copy.setColor(getPlayerDesign().getColor());
        copy.setDisplay(getPlayerDesign().getDisplay());
        copy.setPrefix(getPlayerDesign().getPrefix());
        copy.setSuffix(getPlayerDesign().getSuffix());
        copy.setDescription(getDescription());
        copy.setJoinPower(getJoinpower());
        copy.setTsGroupID(getTsgroupID());
        copy.setPriority(getPriority());
        copy.setTeam(isTeam());
        copy.setDefault(isDefault());
        for(PermissionGroupEntity implementation : getGroups())
            copy.addGroup(implementation.getGroup(),implementation.getTimeOut()- System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        for(PermissionEntity permission : getPermissionData().getPermissions())
            copy.addPermission(permission.getPermission(),permission.getTimeOut()- System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        for(Map.Entry<String,List<PermissionEntity>> entry : getPermissionData().getWorldPermissions().entrySet()){
            for(PermissionEntity permission : entry.getValue())
                copy.addPermission(permission.getPermission(),entry.getKey(),permission.getTimeOut()- System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }
        for(Map.Entry<String, SimplePermissionData> entry : getPermissionData().getServerPermissions().entrySet()){
            for(PermissionEntity permission : entry.getValue().getPermissions())
                copy.addServerPermission(entry.getKey(),permission.getPermission(),permission.getTimeOut()- System.currentTimeMillis(),TimeUnit.MILLISECONDS);
            for(Map.Entry<String,List<PermissionEntity>> entry2 : entry.getValue().getWorldPermissions().entrySet()){
                for(PermissionEntity permission : entry2.getValue())
                    copy.addServerPermission(entry.getKey(),permission.getPermission(),entry2.getKey(),permission.getTimeOut()- System.currentTimeMillis(),TimeUnit.MILLISECONDS);
            }
        }
        for(Map.Entry<String,SimplePermissionData> entry : getPermissionData().getGroupPermissions().entrySet()){
            for(PermissionEntity permission : entry.getValue().getPermissions())
                copy.addServerGroupPermission(entry.getKey(),permission.getPermission(),permission.getTimeOut()- System.currentTimeMillis(),TimeUnit.MILLISECONDS);
            for(Map.Entry<String,List<PermissionEntity>> entry2 : entry.getValue().getWorldPermissions().entrySet()){
                for(PermissionEntity permission : entry2.getValue())
                    copy.addServerGroupPermission(entry.getKey(),permission.getPermission(),entry2.getKey(),permission.getTimeOut()- System.currentTimeMillis(),TimeUnit.MILLISECONDS);
            }
        }
    }
    private void setSetting(String identifier,Object value){
        PermissionGroupManager.getInstance().getStorage().setSetting(getUUID(),identifier,value);
        PermissionUpdater.getInstance().getExecutor().executePermissionUpdate(this.type,this.uuid, PermissionUpdateData.settingChange(identifier,value.toString()));
    }
}
