package ch.dkrieger.permissionsystem.lib.storage.mysql;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.permission.PermissionEntity;
import ch.dkrieger.permissionsystem.lib.permission.PermissionStorage;
import ch.dkrieger.permissionsystem.lib.permission.data.PermissionData;
import ch.dkrieger.permissionsystem.lib.permission.data.SimplePermissionData;
import ch.dkrieger.permissionsystem.lib.storage.mysql.query.DeleteQuery;
import ch.dkrieger.permissionsystem.lib.storage.mysql.query.QueryBuilder;
import ch.dkrieger.permissionsystem.lib.storage.mysql.query.SelectQuery;
import ch.dkrieger.permissionsystem.lib.storage.mysql.table.TableManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.UUID;

public class MySQLPermissionStorage implements PermissionStorage{

    @Override
    public PermissionData getPermissions(PermissionType type, UUID uuid) {
        int count = 0;
        PermissionData data = new PermissionData();
        try {
            SelectQuery query = TableManager.getInstance().getPermissionTable().select().where("permissiontype",type)
                    .where("uuid",uuid);
            ResultSet result = query.execute();
            if(result == null) return data;
            try {
                while(result.next()){
                    count++;
                    if(result.getString("type").equalsIgnoreCase("global")){
                        if(result.getString("world").equalsIgnoreCase("-1")){
                            data.getPermissions().add(new PermissionEntity(result.getLong("timeout")
                                    ,result.getString("permission")));
                        }else{
                            if(!data.getWorldPermissions().containsKey(result.getString("world"))){
                                data.getWorldPermissions().put(result.getString("world"),new LinkedList<>());
                            }
                            data.getWorldPermissions().get(result.getString("world"))
                                    .add(new PermissionEntity(result.getLong("timeout"),result.getString("permission")));
                        }
                    }else if(result.getString("type").equalsIgnoreCase("group")){
                        if(!data.getGroupPermissions().containsKey(result.getString("value"))){
                            data.getGroupPermissions().put(result.getString("value"),new SimplePermissionData());
                        }
                        if(result.getString("world").equalsIgnoreCase("-1")){
                            data.getGroupPermissions().get(result.getString("value")).getPermissions()
                                    .add(new PermissionEntity(result.getLong("timeout"),result.getString("permission")));
                        }else{
                            if(!data.getGroupPermissions().get(result.getString("value"))
                                    .getWorldPermissions().containsKey(result.getString("world"))){
                                data.getGroupPermissions().get(result.getString("value"))
                                        .getWorldPermissions().put(result.getString("world"),new LinkedList<>());
                            }
                            data.getGroupPermissions().get(result.getString("value"))
                                    .getWorldPermissions().get(result.getString("world"))
                                    .add(new PermissionEntity(result.getLong("timeout"),result.getString("permission")));
                        }
                    }else if(result.getString("type").equalsIgnoreCase("server")){
                        if(!data.getServerPermissions().containsKey(result.getString("value"))){
                            data.getServerPermissions().put(result.getString("value"),new SimplePermissionData());
                        }
                        if(result.getString("world").equalsIgnoreCase("-1")){
                            data.getServerPermissions().get(result.getString("value")).getPermissions()
                                    .add(new PermissionEntity(result.getLong("timeout"),result.getString("permission")));
                        }else{
                            if(!data.getServerPermissions().get(result.getString("value"))
                                    .getWorldPermissions().containsKey(result.getString("world"))){
                                data.getServerPermissions().get(result.getString("value"))
                                        .getWorldPermissions().put(result.getString("world"),new LinkedList<>());
                            }
                            data.getServerPermissions().get(result.getString("value"))
                                    .getWorldPermissions().get(result.getString("world"))
                                    .add(new PermissionEntity(result.getLong("timeout"),result.getString("permission")));
                        }
                    }
                }
            }finally {
                result.close();
                query.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }
    @Override
    public void addPermission(PermissionType type, UUID uuid, String permission, String world, Long timeout) {
        if(timeout == null) timeout = -1L;
        if(world == null) world = "-1";
        TableManager.getInstance().getPermissionTable().insert().insert("permissiontype").insert("uuid")
                .insert("type").insert("value").insert("world").insert("permission").insert("timeout").insert("created")
                .value(type).value(uuid).value("global").value("-1").value(world).value(permission).value(timeout)
                .value(System.currentTimeMillis()).execute();
    }
    @Override
    public void addServerPermission(PermissionType type, UUID uuid, String server, String permission, String world, Long timeout) {
        if(timeout == null) timeout = -1L;
        if(world == null) world = "-1";
        TableManager.getInstance().getPermissionTable().insert().insert("permissiontype").insert("uuid")
                .insert("type").insert("value").insert("world").insert("permission").insert("timeout").insert("created")
                .value(type).value(uuid).value("server").value(server).value(world).value(permission).value(timeout)
                .value(System.currentTimeMillis()).execute();
    }
    @Override
    public void addServerGroupPermission(PermissionType type, UUID uuid, String group, String permission, String world, Long timeout) {
        if(timeout == null) timeout = -1L;
        if(world == null) world = "-1";
        TableManager.getInstance().getPermissionTable().insert().insert("permissiontype").insert("uuid")
                .insert("type").insert("value").insert("world").insert("permission").insert("timeout").insert("created")
                .value(type).value(uuid).value("group").value(group).value(world).value(permission).value(timeout)
                .value(System.currentTimeMillis()).execute();
    }
    @Override
    public void removePermission(PermissionType type, UUID uuid, String permission, String world) {
        DeleteQuery query = TableManager.getInstance().getPermissionTable().delete().where("permissiontype",type)
                .where("uuid",uuid).where("type","global").where("permission",permission);
        if(world != null && !(world.equalsIgnoreCase("-1"))) query.where("world",world);
        query.execute();
    }
    @Override
    public void removeServerPermission(PermissionType type, UUID uuid, String server, String permission, String world) {
        DeleteQuery query = TableManager.getInstance().getPermissionTable().delete().where("permissiontype",type)
                .where("uuid",uuid).where("type","server").where("value",server).where("permission",permission);
        if(world != null && !(world.equalsIgnoreCase("-1"))) query.where("world",world);
        query.execute();
    }
    @Override
    public void removeServerGroupPermission(PermissionType type, UUID uuid, String group, String permission, String world) {
        DeleteQuery query = TableManager.getInstance().getPermissionTable().delete().where("permissiontype",type)
                .where("uuid",uuid).where("type","group").where("value",group).where("permission",permission);
        if(world != null && !(world.equalsIgnoreCase("-1"))) query.where("world",world);
        query.execute();
    }
    @Override
    public void clearAllPermissions(PermissionType type, UUID uuid) {
        TableManager.getInstance().getPermissionTable().delete().where("permissiontype",type).where("uuid",uuid).execute();
    }
    @Override
    public void clearPermissions(PermissionType type, UUID uuid, String world) {
        DeleteQuery query = TableManager.getInstance().getPermissionTable().delete().where("permissiontype",type)
                .where("uuid",uuid).where("type","global");
        if(world != null) query.where("world",world);
        query.execute();
    }
    @Override
    public void clearServerPermission(PermissionType type, UUID uuid, String server, String world) {
        DeleteQuery query = TableManager.getInstance().getPermissionTable().delete().where("permissiontype",type)
                .where("uuid",uuid).where("type","server");
        if(server != null) query.where("value",server);
        if(world != null) query.where("world",world);
        query.execute();
    }
    @Override
    public void clearServerGroupPermission(PermissionType type, UUID uuid, String group, String world) {
        DeleteQuery query = TableManager.getInstance().getPermissionTable().delete().where("permissiontype",type)
                .where("uuid",uuid).where("type","group");
        if(group != null) query.where("value",group);
        if(world != null) query.where("world",world);
        query.execute();
    }
    @Override
    public void onTimeOutDeleteTask() {
        QueryBuilder query = new QueryBuilder();
        query.append(TableManager.getInstance().getPermissionTable().delete().whereLower("timeout",System.currentTimeMillis())
                .whereHigher("timeout",0));
        query.append(TableManager.getInstance().getEntityTable().delete().whereLower("timeout",System.currentTimeMillis())
                .whereHigher("timeout",0));
        query.buildAndExecute();
    }
}
