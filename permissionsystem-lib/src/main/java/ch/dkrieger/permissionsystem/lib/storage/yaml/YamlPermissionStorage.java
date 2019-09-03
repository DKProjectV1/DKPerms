package ch.dkrieger.permissionsystem.lib.storage.yaml;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 22:35
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.config.SimpleConfig;
import ch.dkrieger.permissionsystem.lib.permission.PermissionEntity;
import ch.dkrieger.permissionsystem.lib.permission.PermissionStorage;
import ch.dkrieger.permissionsystem.lib.permission.data.PermissionData;
import ch.dkrieger.permissionsystem.lib.permission.data.SimplePermissionData;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class YamlPermissionStorage implements PermissionStorage{

    private SimpleConfig groups, players;

    public YamlPermissionStorage(SimpleConfig groups, SimpleConfig players) {
        this.groups = groups;
        this.players = players;
    }

    @Override
    public PermissionData getPermissions(PermissionType type, UUID uuid) {
        PermissionData data = new PermissionData();
        if(type == PermissionType.GROUP){
            try{
                List<String> global = this.groups.getStringListValue("groups."+uuid+".permissions.global");
                if(global != null){
                    for(String entity : global){
                        try{
                            if(entity.contains(";")){
                                String[] split = entity.split(";");
                                data.getPermissions().add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                            }else data.getPermissions().add(new PermissionEntity(-1L,entity));
                        }catch (Exception exception){}
                    }
                }
                for(String world : this.groups.getKeys("groups."+uuid+".permissions.worlds")){
                    List<String> worldperms = this.groups.getStringListValue("groups."+uuid+".permissions.worlds."+world);
                    if(global != null){
                        LinkedList<PermissionEntity> permissions = new LinkedList<>();
                        for(String entity : worldperms){
                            try{
                                if(entity.contains(";")){
                                    String[] split = entity.split(";");
                                    permissions.add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                }else permissions.add(new PermissionEntity(-1L,entity));
                            }catch (Exception ignored){}
                        }
                        if(permissions.size() > 0) data.getWorldPermissions().put(world.toLowerCase(),permissions);
                    }
                }
                for(String server : this.groups.getKeys("groups."+uuid+".permissions.servers")){
                    SimplePermissionData serverdata = new SimplePermissionData();
                    List<String> serverperms = this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".global");
                    if(serverperms != null){
                        for(String entity : serverperms){
                            try{
                                if(entity.contains(";")){
                                    String[] split = entity.split(";");
                                   serverdata.getPermissions().add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                }else serverdata.getPermissions().add(new PermissionEntity(-1L,entity));
                            }catch (Exception ignored){}
                        }
                    }
                    for(String world : this.groups.getKeys("groups."+uuid+".permissions.servers."+server+".worlds")){
                        List<String> worldperms = this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".worlds."+world);
                        if(global != null){
                            LinkedList<PermissionEntity> permissions = new LinkedList<>();
                            for(String entity : worldperms){
                                try{
                                    if(entity.contains(";")){
                                        String[] split = entity.split(";");
                                        permissions.add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                    }else permissions.add(new PermissionEntity(-1L,entity));
                                }catch (Exception ignored){}
                            }
                            if(permissions.size() > 0) serverdata.getWorldPermissions().put(world.toLowerCase(),permissions);
                        }
                    }
                    if(serverdata.getPermissions().size() > 0 || serverdata.getWorldPermissions().size() > 0)
                        data.getServerPermissions().put(server.toLowerCase(),serverdata);
                }
                for(String server : this.groups.getKeys("groups."+uuid+".permissions.groups")){
                    SimplePermissionData serverdata = new SimplePermissionData();
                    List<String> serverperms = this.groups.getStringListValue("groups."+uuid+".permissions.groups."+server+".global");
                    if(serverperms != null){
                        for(String entity : serverperms){
                            try{
                                if(entity.contains(";")){
                                    String[] split = entity.split(";");
                                    serverdata.getPermissions().add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                }else serverdata.getPermissions().add(new PermissionEntity(-1L,entity));
                            }catch (Exception ignored){}
                        }
                    }
                    for(String world : this.groups.getKeys("groups."+uuid+".permissions.groups."+server+".worlds")){
                        List<String> worldperms = this.groups.getStringListValue("groups."+uuid+".permissions.groups."+server+".worlds."+world);
                        if(global != null){
                            LinkedList<PermissionEntity> permissions = new LinkedList<>();
                            for(String entity : worldperms){
                                try{
                                    if(entity.contains(";")){
                                        String[] split = entity.split(";");
                                        permissions.add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                    }else permissions.add(new PermissionEntity(-1L,entity));
                                }catch (Exception ignored){}
                            }
                            if(permissions.size() > 0) serverdata.getWorldPermissions().put(world.toLowerCase(),permissions);
                        }
                    }
                    if(serverdata.getPermissions().size() > 0 || serverdata.getWorldPermissions().size() > 0)
                        data.getGroupPermissions().put(server.toLowerCase(),serverdata);
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }else{
            try{
                List<String> global = this.players.getStringListValue("players."+uuid+".permissions.global");
                if(global != null){
                    for(String entity : global){
                        try{
                            if(entity.contains(";")){
                                String[] split = entity.split(";");
                                data.getPermissions().add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                            }else data.getPermissions().add(new PermissionEntity(-1L,entity));
                        }catch (Exception ignored){}
                    }
                }
                for(String world : this.players.getKeys("players."+uuid+".permissions.worlds")){
                    List<String> worldperms = this.players.getStringListValue("players."+uuid+".permissions.worlds."+world);
                    if(worldperms != null){
                        LinkedList<PermissionEntity> permissions = new LinkedList<>();
                        for(String entity : worldperms){
                            try{
                                if(entity.contains(";")){
                                    String[] split = entity.split(";");
                                    permissions.add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                }else permissions.add(new PermissionEntity(-1L,entity));
                            }catch (Exception ignored){}
                        }
                        if(permissions.size() > 0) data.getWorldPermissions().put(world.toLowerCase(),permissions);
                    }
                }
                for(String server : this.players.getKeys("players."+uuid+".permissions.servers")){
                    SimplePermissionData serverdata = new SimplePermissionData();
                    List<String> serverperms = this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".global");
                    if(serverperms != null){
                        for(String entity : serverperms){
                            try{
                                if(entity.contains(";")){
                                    String[] split = entity.split(";");
                                    serverdata.getPermissions().add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                }else serverdata.getPermissions().add(new PermissionEntity(-1L,entity));
                            }catch (Exception ignored){}
                        }
                    }
                    for(String world : this.players.getKeys("players."+uuid+".permissions.servers."+server+".worlds")){
                        List<String> worldperms = this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".worlds."+world);
                        if(global != null){
                            LinkedList<PermissionEntity> permissions = new LinkedList<>();
                            for(String entity : worldperms){
                                try{
                                    if(entity.contains(";")){
                                        String[] split = entity.split(";");
                                        permissions.add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                    }else permissions.add(new PermissionEntity(-1L,entity));
                                }catch (Exception ignored){}
                            }
                            if(permissions.size() > 0) serverdata.getWorldPermissions().put(world.toLowerCase(),permissions);
                        }
                    }
                    if(serverdata.getPermissions().size() > 0 || serverdata.getWorldPermissions().size() > 0)
                        data.getServerPermissions().put(server.toLowerCase(),serverdata);
                }
                for(String server : this.players.getKeys("players."+uuid+".permissions.groups")){
                    SimplePermissionData serverdata = new SimplePermissionData();
                    List<String> serverperms = this.players.getStringListValue("players."+uuid+".permissions.groups."+server+".global");
                    if(serverperms != null){
                        for(String entity : serverperms){
                            try{
                                if(entity.contains(";")){
                                    String[] split = entity.split(";");
                                    serverdata.getPermissions().add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                }else serverdata.getPermissions().add(new PermissionEntity(-1L,entity));
                            }catch (Exception exception){}
                        }
                    }
                    for(String world : this.players.getKeys("players."+uuid+".permissions.groups."+server+".worlds")){
                        List<String> worldperms = this.players.getStringListValue("players."+uuid+".permissions.groups."+server+".worlds."+world);
                        if(global != null){
                            LinkedList<PermissionEntity> permissions = new LinkedList<>();
                            for(String entity : worldperms){
                                try{
                                    if(entity.contains(";")){
                                        String[] split = entity.split(";");
                                        permissions.add(new PermissionEntity(Long.valueOf(split[1]),split[0]));
                                    }else permissions.add(new PermissionEntity(-1L,entity));
                                }catch (Exception exception){}
                            }
                            if(permissions.size() > 0) serverdata.getWorldPermissions().put(world.toLowerCase(),permissions);
                        }
                    }
                    if(serverdata.getPermissions().size() > 0 || serverdata.getWorldPermissions().size() > 0)
                        data.getGroupPermissions().put(server.toLowerCase(),serverdata);
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        return data;
    }

    @Override
    public void addPermission(PermissionType type, UUID uuid, String permission, String world, Long timeout) {
        if(type == PermissionType.GROUP){
            if(world == null){
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.global");
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.groups.setValue("groups."+uuid+".permissions.global",list);
            }else{
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.worlds."+world);
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.groups.setValue("groups."+uuid+".permissions.worlds."+world,list);
            }
            this.groups.save();
        }else{
            if(world == null){
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.global");
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.players.setValue("players."+uuid+".permissions.global",list);
            }else{
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.worlds."+world);
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.players.setValue("players."+uuid+".permissions.worlds."+world,list);
            }
            this.players.save();
        }
    }

    @Override
    public void addServerPermission(PermissionType type, UUID uuid, String server, String permission, String world, Long timeout) {
        if(type == PermissionType.GROUP){
            if(world == null){
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".global");
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.groups.setValue("groups."+uuid+".permissions.servers."+server+".global",list);
            }else{
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".worlds."+world);
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.groups.setValue("groups."+uuid+".permissions.servers."+server+".worlds."+world,list);
            }
            this.groups.save();
        }else{
            if(world == null){
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".global");
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.players.setValue("players."+uuid+".permissions.servers."+server+".global",list);
            }else{
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".worlds."+world);
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.players.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,list);
            }
            this.players.save();
        }
    }

    @Override
    public void addServerGroupPermission(PermissionType type, UUID uuid, String group, String permission, String world, Long timeout) {
        if(type == PermissionType.GROUP){
            if(world == null){
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.groups."+group+".global");
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.groups.setValue("groups."+uuid+".permissions.groups."+group+".global",list);
            }else{
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.groups."+group+".worlds."+world);
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.groups.setValue("groups."+uuid+".permissions.groups."+group+".worlds."+world,list);
            }
            this.groups.save();
        }else{
            if(world == null){
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.groups."+group+".global");
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.players.setValue("players."+uuid+".permissions.groups."+group+".global",list);
            }else{
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.groups."+group+".worlds."+world);
                if(list == null) list = new LinkedList<>();
                list.add(permission+";"+timeout);
                this.players.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,list);
            }
            this.players.save();
        }
    }

    @Override
    public void removePermission(PermissionType type, UUID uuid, String permission, String world) {
        if(type == PermissionType.GROUP){
            if(world == null){
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.global");
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.groups.setValue("groups."+uuid+".permissions.global",list);
                    else this.groups.setValue("groups."+uuid+".permissions.global",null);
                }
            }else{
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.worlds."+world);
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.groups.setValue("groups."+uuid+".permissions.worlds."+world,list);
                    else this.groups.setValue("groups."+uuid+".permissions.worlds."+world,null);
                }
            }
            this.groups.save();
        }else{
            if(world == null){
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.global");
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.global",list);
                    else this.players.setValue("players."+uuid+".permissions.global",null);
                }
            }else{
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.worlds."+world);
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.worlds."+world,list);
                    else this.players.setValue("players."+uuid+".permissions.worlds."+world,null);
                }
            }
            this.players.save();
        }
    }

    @Override
    public void removeServerPermission(PermissionType type, UUID uuid, String server, String permission, String world) {
        if(server == null) return;
        if(type == PermissionType.GROUP){
            if(world == null){
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".global");
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.groups.setValue("players."+uuid+".permissions.servers."+server+".global",list);
                    else this.groups.setValue("players."+uuid+".permissions.servers."+server+".global",null);
                }
            }else{
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".worlds."+world);
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.groups.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,list);
                    else this.groups.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,null);
                }
            }
            this.groups.save();
        }else{
            if(world == null){
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".global");
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.servers."+server+".global",list);
                    else this.players.setValue("players."+uuid+".permissions.servers."+server+".global",null);
                }
            }else{
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".worlds."+world);
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,list);
                    else this.players.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,null);
                }
            }
            this.players.save();
        }
    }

    @Override
    public void removeServerGroupPermission(PermissionType type, UUID uuid, String group, String permission, String world) {
        if(group == null) return;
        if(type == PermissionType.GROUP){
            if(world == null){
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.groups."+group+".global");
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.groups.setValue("players."+uuid+".permissions.groups."+group+".global",list);
                    else this.groups.setValue("players."+uuid+".permissions.groups."+group+".global",null);
                }
            }else{
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.groups."+group+".worlds."+world);
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.groups.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,list);
                    else this.groups.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,null);
                }
            }
            this.groups.save();
        }else{
            if(world == null){
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.groups."+group+".global");
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.groups."+group+".global",list);
                    else this.players.setValue("players."+uuid+".permissions.groups."+group+".global",null);
                }
            }else{
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.groups."+group+".worlds."+world);
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(split[0].equalsIgnoreCase(permission)) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,list);
                    else this.players.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,null);
                }
            }
            this.players.save();
        }
    }

    @Override
    public void clearAllPermissions(PermissionType type, UUID uuid) {
        if(type == PermissionType.GROUP){
            this.groups.setValue("groups."+uuid+".permissions",null);
            this.groups.save();
        }else{
            this.players.setValue("players."+uuid+".permissions",null);
            this.players.save();
        }
    }

    @Override
    public void clearPermissions(PermissionType type, UUID uuid, String world) {
        if(type == PermissionType.GROUP){
            if(world == null){
                this.groups.setValue("groups."+uuid+".permissions.global",null);
                this.groups.setValue("groups."+uuid+".permissions.worlds",null);
            }else this.groups.setValue("groups."+uuid+".permissions.worlds."+world,null);
            this.groups.save();
        }else{
            if(world == null){
                this.players.setValue("players."+uuid+".permissions.global",null);
                this.players.setValue("players."+uuid+".permissions.worlds",null);
            }else this.players.setValue("players."+uuid+".permissions.worlds."+world,null);
            this.players.save();
        }
    }

    @Override
    public void clearServerPermission(PermissionType type, UUID uuid, String server, String world) {
        if(type == PermissionType.GROUP){
            if(server == null) this.groups.setValue("groups."+uuid+".permissions.servers",null);
            else{
                if(world == null) this.groups.setValue("groups."+uuid+".permissions.servers."+server,null);
                else this.groups.setValue("groups."+uuid+".permissions.servers."+server+".worlds."+world,null);
            }
            this.groups.save();
        }else{
            if(server == null) this.players.setValue("players."+uuid+".permissions.servers",null);
            else{
                if(world == null) this.players.setValue("players."+uuid+".permissions.servers."+server,null);
                else this.players.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,null);
            }
            this.players.save();
        }
    }

    @Override
    public void clearServerGroupPermission(PermissionType type, UUID uuid, String group, String world) {
        if(type == PermissionType.GROUP){
            if(group == null) this.groups.setValue("groups."+uuid+".permissions.groups",null);
            else{
                if(world == null) this.groups.setValue("groups."+uuid+".permissions.groups."+group,null);
                else this.groups.setValue("groups."+uuid+".permissions.groups."+group+".worlds."+world,null);
            }
            this.groups.save();
        }else{
            if(group == null) this.players.setValue("players."+uuid+".permissions.groups",null);
            else{
                if(world == null) this.players.setValue("players."+uuid+".permissions.groups."+group,null);
                else this.players.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,null);
            }
            this.players.save();
        }
    }

    @Override
    public void onTimeOutDeleteTask() {
        for(String uuid : this.groups.getKeys("groups")){
            try{
                UUID.fromString(uuid);
            }catch (Exception exception){ continue; }
            List<String> global = this.groups.getStringListValue("groups."+uuid+".permissions.global");
            if(global != null){
                for(String entity : new LinkedList<>(global)){
                    try{
                        String[] split = entity.split(";");
                        if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) global.remove(entity);
                    }catch (Exception exception){}
                }
                if(global.size() > 0) this.groups.setValue("groups."+uuid+".permissions.global",global);
                else this.groups.setValue("groups."+uuid+".permissions.global",null);
            }
            for(String world : this.groups.getStringListValue("groups."+uuid+".permissions.worlds")){
                List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.worlds."+world);
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.groups.setValue("groups."+uuid+".permissions.worlds."+world,list);
                    else this.groups.setValue("groups."+uuid+".permissions.worlds."+world,null);
                }
            }
            for(String server : this.groups.getStringListValue("groups."+uuid+".permissions.servers")){
                List<String> globalserver = this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".global");
                if(globalserver != null){
                    for(String entity : new LinkedList<>(globalserver)){
                        try{
                            String[] split = entity.split(";");
                            if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) globalserver.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(globalserver.size() > 0) this.groups.setValue("players."+uuid+".permissions.servers."+server+".global",globalserver);
                    else this.groups.setValue("players."+uuid+".permissions.servers."+server+".global",null);
                }
                for(String world : this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".worlds")){
                    List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.servers."+server+".worlds."+world);
                    if(list != null){
                        for(String entity : new LinkedList<>(list)){
                            try{
                                String[] split = entity.split(";");
                                if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) list.remove(entity);
                            }catch (Exception exception){}
                        }
                        if(list.size() > 0) this.groups.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,list);
                        else this.groups.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,null);
                    }
                }
            }
            for(String group : this.groups.getStringListValue("groups."+uuid+".permissions.groups")){
                List<String> globalgroup = this.groups.getStringListValue("groups."+uuid+".permissions.groups."+group+".global");
                if(globalgroup != null){
                    for(String entity : new LinkedList<>(globalgroup)){
                        try{
                            String[] split = entity.split(";");
                            if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) globalgroup.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(globalgroup.size() > 0) this.groups.setValue("players."+uuid+".permissions.groups."+group+".global",globalgroup);
                    else this.groups.setValue("players."+uuid+".permissions.groups."+group+".global",null);
                }
                for(String world : this.groups.getStringListValue("groups."+uuid+".permissions.groups."+group+".worlds")){
                    List<String> list = this.groups.getStringListValue("groups."+uuid+".permissions.groups."+group+".worlds."+world);
                    if(list != null){
                        for(String entity : new LinkedList<>(list)){
                            try{
                                String[] split = entity.split(";");
                                if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) list.remove(entity);
                            }catch (Exception exception){}
                        }
                        if(list.size() > 0) this.groups.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,list);
                        else this.groups.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,null);
                    }
                }
            }
            List<String> groups = this.groups.getStringListValue("groups."+uuid+".implementation");
            if(groups != null){
                for(String entity : new LinkedList<>(groups)){
                    try{
                        String[] split = entity.split(";");
                        if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) groups.remove(entity);
                    }catch (Exception exception){}
                }
                if(groups.size() > 0) this.groups.setValue("groups."+uuid+".implementation",groups);
                else this.groups.setValue("groups."+uuid+".implementation",null);
            }
            this.groups.save();
        }
        for(String uuid : this.players.getKeys("players")){
            try{
                UUID.fromString(uuid);
            }catch (Exception exception){ continue; }
            List<String> global = this.players.getStringListValue("players."+uuid+".permissions.global");
            if(global != null){
                for(String entity : new LinkedList<>(global)){
                    try{
                        String[] split = entity.split(";");
                        if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) global.remove(entity);
                    }catch (Exception exception){}
                }
                if(global.size() > 0) this.players.setValue("players."+uuid+".permissions.global",global);
                else this.players.setValue("players."+uuid+".permissions.global",null);
            }
            for(String world : this.players.getStringListValue("players."+uuid+".permissions.worlds")){
                List<String> list = this.players.getStringListValue("players."+uuid+".permissions.worlds."+world);
                if(list != null){
                    for(String entity : new LinkedList<>(list)){
                        try{
                            String[] split = entity.split(";");
                            if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) list.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.worlds."+world,list);
                    else this.players.setValue("players."+uuid+".permissions.worlds."+world,null);
                }
            }
            for(String server : this.players.getStringListValue("players."+uuid+".permissions.servers")){
                List<String> globalserver = this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".global");
                if(globalserver != null){
                    for(String entity : new LinkedList<>(globalserver)){
                        try{
                            String[] split = entity.split(";");
                            if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) globalserver.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(globalserver.size() > 0) this.players.setValue("players."+uuid+".permissions.servers."+server+".global",globalserver);
                    else this.players.setValue("players."+uuid+".permissions.servers."+server+".global",null);
                }
                for(String world : this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".worlds")){
                    List<String> list = this.players.getStringListValue("players."+uuid+".permissions.servers."+server+".worlds."+world);
                    if(list != null){
                        for(String entity : new LinkedList<>(list)){
                            try{
                                String[] split = entity.split(";");
                                if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) list.remove(entity);
                            }catch (Exception exception){}
                        }
                        if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,list);
                        else this.players.setValue("players."+uuid+".permissions.servers."+server+".worlds."+world,null);
                    }
                }
            }
            for(String group : this.players.getStringListValue("players."+uuid+".permissions.groups")){
                List<String> globalgroup = this.players.getStringListValue("players."+uuid+".permissions.groups."+group+".global");
                if(globalgroup != null){
                    for(String entity : new LinkedList<>(globalgroup)){
                        try{
                            String[] split = entity.split(";");
                            if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) globalgroup.remove(entity);
                        }catch (Exception exception){}
                    }
                    if(globalgroup.size() > 0) this.players.setValue("players."+uuid+".permissions.groups."+group+".global",globalgroup);
                    else this.players.setValue("players."+uuid+".permissions.groups."+group+".global",null);
                }
                for(String world : this.players.getStringListValue("players."+uuid+".permissions.groups."+group+".worlds")){
                    List<String> list = this.players.getStringListValue("players."+uuid+".permissions.groups."+group+".worlds."+world);
                    if(list != null){
                        for(String entity : new LinkedList<>(list)){
                            try{
                                String[] split = entity.split(";");
                                if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) list.remove(entity);
                            }catch (Exception exception){}
                        }
                        if(list.size() > 0) this.players.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,list);
                        else this.players.setValue("players."+uuid+".permissions.groups."+group+".worlds."+world,null);
                    }
                }
            }
            List<String> groups = this.players.getStringListValue("players."+uuid+".groups");
            if(groups != null){
                for(String entity : new LinkedList<>(groups)){
                    try{
                        String[] split = entity.split(";");
                        if(Long.valueOf(split[1]) <= System.currentTimeMillis() && Long.valueOf(split[1]) > 0) groups.remove(entity);
                    }catch (Exception exception){}
                }
                if(groups.size() > 0) this.players.setValue("players."+uuid+".groups",groups);
                else this.players.setValue("players."+uuid+".groups",null);
            }
            this.players.save();
        }
    }
}
