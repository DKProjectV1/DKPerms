package ch.dkrieger.permissionsystem.lib.storage.mysql;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityStorage;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupEntity;
import ch.dkrieger.permissionsystem.lib.storage.mysql.query.QueryBuilder;
import ch.dkrieger.permissionsystem.lib.storage.mysql.query.SelectQuery;
import ch.dkrieger.permissionsystem.lib.storage.mysql.table.TableManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MySQLPermissionEntityStorage implements PermissionEntityStorage{

    @Override
    public List<PermissionGroupEntity> getPermissionEntities(PermissionType type, UUID uuid) {
        List<PermissionGroupEntity> list = new LinkedList<>();
        try {
            SelectQuery query = TableManager.getInstance().getEntityTable().select()
                    .where("permissiontype",type).where("uuid",uuid);
            ResultSet result = query.execute();
            if(result == null) return list;
            try {
                while(result.next()){
                    list.add(new PermissionGroupEntity(result.getLong("timeout")
                            ,UUID.fromString(result.getString("groupuuid"))));
                }
            }finally {
                query.close();
                result.close();
            }
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        return list;
    }
    @Override
    public void setEntity(PermissionType type, UUID uuid, UUID group, Long timeout) {
        if(timeout == null) timeout = -1L;
        QueryBuilder builder = new QueryBuilder();
        builder.append(TableManager.getInstance().getEntityTable().delete().where("permissiontype",type).where("uuid",uuid));
        builder.append(TableManager.getInstance().getEntityTable().insert().insert("permissiontype").insert("uuid")
                .insert("groupuuid").insert("timeout").insert("created").value(type).value(uuid).value(group)
                .value(timeout).value(System.currentTimeMillis()));
        builder.buildAndExecute();
    }
    @Override
    public void addEntity(PermissionType type, UUID uuid, UUID group, Long timeout) {
        if(timeout == null) timeout = -1L;
        TableManager.getInstance().getEntityTable().insert().insert("permissiontype").insert("uuid")
                .insert("groupuuid").insert("timeout").insert("created").value(type).value(uuid).value(group)
                .value(timeout).value(System.currentTimeMillis()).execute();
    }
    @Override
    public void removeEntity(PermissionType type, UUID uuid, UUID group) {
        TableManager.getInstance().getEntityTable().delete().where("permissiontype",type).where("uuid",uuid)
                .where("groupuuid",group).execute();
    }
    @Override
    public void clearEntity(PermissionType type, UUID uuid) {
        TableManager.getInstance().getEntityTable().delete().where("permissiontype",type).where("uuid",uuid).execute();
    }
}
