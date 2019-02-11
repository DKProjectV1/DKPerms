package ch.dkrieger.permissionsystem.lib.storage.mysql;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:46
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupStorage;
import ch.dkrieger.permissionsystem.lib.player.PlayerDesign;
import ch.dkrieger.permissionsystem.lib.storage.mysql.query.SelectQuery;
import ch.dkrieger.permissionsystem.lib.storage.mysql.table.TableManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MySQLPermissionGroupStorage implements PermissionGroupStorage{

    @Override
    public List<PermissionGroup> loadGroups() {
        List<PermissionGroup> list = new LinkedList();
        try {
            SelectQuery query = TableManager.getInstance().getGroupTable().select();
            ResultSet result = query.execute();
            if(result == null) return list;
            try {
                while(result.next()){
                    list.add(new PermissionGroup(result.getString("name")
                            ,UUID.fromString(result.getString("uuid"))
                            ,result.getString("description")
                            ,result.getBoolean("default")
                            ,result.getBoolean("team")
                            ,result.getInt("priority")
                            ,result.getInt("tsgroupid")
                            ,result.getInt("joinpower")
                            ,new PlayerDesign(result.getString("prefix")
                            ,result.getString("suffix")
                            ,result.getString("display")
                            ,result.getString("color"))));
                }
            }finally {
                query.close();
                result.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public PermissionGroup createGroup(String name) {
        UUID uuid = UUID.randomUUID();
        while(!isUUIDAvalibal(uuid)) uuid = UUID.randomUUID();
        TableManager.getInstance().getGroupTable().insert().insert("name").insert("uuid").insert("created").value(name)
                .value(uuid).value(System.currentTimeMillis()).execute();
        return new PermissionGroup(name,uuid,"New PermissionGroup",false,false,-1,-1
                ,0,new PlayerDesign("-1","-1","-1","-1"));
    }
    @Override
    public void deleteGroup(UUID uuid) {
        TableManager.getInstance().getGroupTable().delete().where("uuid",uuid).execute();
    }
    @Override
    public void setSetting(UUID uuid, String identifier, Object value) {
        TableManager.getInstance().getGroupTable().update().set(identifier,value).where("uuid",uuid).execute();
    }
    @Override
    public List<UUID> getPlayers(PermissionGroup group) {
        List<UUID> players = new ArrayList<>();
        try {
            SelectQuery query = TableManager.getInstance().getEntityTable().select().where("permissiontype", PermissionType.PLAYER)
                    .where("groupuuid",group.getUUID());
            ResultSet result = query.execute();
            if(result == null) return players;
            try {
                while(result.next()) players.add(UUID.fromString(result.getString("uuid")));
            }finally {
                query.close();
                result.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return players;
    }
    private Boolean isUUIDAvalibal(UUID uuid){
        try {
            SelectQuery query = TableManager.getInstance().getPermissionTable().select().where("uuid", uuid);
            ResultSet result = query.execute();
            if(result == null) return true;
            try {
                return !result.next();
            }finally {
                query.close();
                result.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}
