package ch.dkrieger.permissionsystem.lib.storage.yaml;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.07.18 13:57
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.config.SimpleConfig;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityStorage;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.permission.PermissionEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class YamlPermissionEntityStorage implements PermissionEntityStorage{

    private SimpleConfig groups, players;

    public YamlPermissionEntityStorage(SimpleConfig groups, SimpleConfig players) {
        this.groups = groups;
        this.players = players;
    }
    @Override
    public List<PermissionGroupEntity> getPermissionEntitys(PermissionType type, UUID uuid) {
        List<PermissionGroupEntity> list = new LinkedList<>();
        if(type == PermissionType.GROUP){
            List<String> data = this.groups.getStringListValue("groups."+uuid+".implementation");
            if(data != null){
                for(String entity : data){
                    try{
                        if(entity.contains(";")){
                            String[] split = entity.split(";");
                            list.add(new PermissionGroupEntity(Long.valueOf(split[1]),UUID.fromString(split[0])));
                        }else list.add(new PermissionGroupEntity(-1L,UUID.fromString(entity)));
                    }catch (Exception exception){}
                }
            }
        }else{
            List<String> data = this.players.getStringListValue("players."+uuid+".groups");
            if(data != null){
                for(String entity : data){
                    try{
                        if(entity.contains(";")){
                            String[] split = entity.split(";");
                            list.add(new PermissionGroupEntity(Long.valueOf(split[1]),UUID.fromString(split[0])));
                        }else list.add(new PermissionGroupEntity(-1L,UUID.fromString(entity)));
                    }catch (Exception exception){}
                }
            }
        }
        return list;
    }
    @Override
    public void setEntity(PermissionType type, UUID uuid, UUID group, Long timeout) {
        if(type == PermissionType.GROUP){
            List<String> list = new LinkedList<>();
            list.add(group+";"+timeout);
            this.groups.setValue("groups."+uuid+".implementation",list);
            this.groups.save();
        }else{
            List<String> list = new LinkedList<>();
            list.add(group+";"+timeout);
            this.players.setValue("players."+uuid+".groups",list);
            this.players.save();
        }
    }
    @Override
    public void addEntity(PermissionType type, UUID uuid, UUID group, Long timeout) {
        if(type == PermissionType.GROUP){
            List<String> list = this.groups.getStringListValue("groups."+uuid+".implementation");
            if(list == null) list = new LinkedList<>();
            list.add(group+";"+timeout);
            this.groups.setValue("groups."+uuid+".implementation",list);
            this.groups.save();
        }else{
            List<String> list = this.players.getStringListValue("players."+uuid+".groups");
            if(list == null) list = new LinkedList<>();
            list.add(group+";"+timeout);
            this.players.setValue("players."+uuid+".groups",list);
            this.players.save();
        }
    }
    @Override
    public void removeEntity(PermissionType type, UUID uuid, UUID group) {
        if(type == PermissionType.GROUP){
            List<String> list = this.groups.getStringListValue("groups."+uuid+".implementation");
            if(list != null){
                for(String entity : new LinkedList<>(list)){
                    try{
                        String[] split = entity.split(";");
                        if(split[0].equalsIgnoreCase(""+group)) list.remove(entity);
                    }catch (Exception exception){}
                }
                if(list.size() > 0) this.groups.setValue("groups."+uuid+".implementation",list);
                else this.groups.setValue("groups."+uuid+".implementation",null);
            }
            this.groups.save();
        }else{
            List<String> list = this.players.getStringListValue("players."+uuid+".groups");
            if(list != null){
                for(String entity : new LinkedList<>(list)){
                    try{
                        String[] split = entity.split(";");
                        if(split[0].equalsIgnoreCase(""+group)) list.remove(entity);
                    }catch (Exception exception){}
                }
                if(list.size() > 0) this.players.setValue("players."+uuid+".groups",list);
                else this.players.setValue("players."+uuid+".groups",null);
            }
            this.players.save();
        }
    }
    @Override
    public void clearEntity(PermissionType type, UUID uuid) {
        if(type == PermissionType.GROUP){
            this.groups.setValue("groups."+uuid+".implementation",null);
            this.groups.save();
        }else{
            this.players.setValue("players."+uuid+".groups",null);
            this.players.save();
        }
    }
}
