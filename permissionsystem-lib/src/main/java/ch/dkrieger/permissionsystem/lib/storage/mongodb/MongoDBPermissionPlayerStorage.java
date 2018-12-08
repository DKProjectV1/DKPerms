package ch.dkrieger.permissionsystem.lib.storage.mongodb;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 11.07.18 15:41
 *
 */

import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerStorage;
import com.mongodb.BasicDBObject;

import java.util.UUID;

public class MongoDBPermissionPlayerStorage implements PermissionPlayerStorage{

    @Override
    public PermissionPlayer getPermissionPlayer(UUID uuid) throws Exception {
        return null;
    }
    @Override
    public PermissionPlayer getPermissionPlayer(String name) throws Exception {
        return null;
    }
    @Override
    public PermissionPlayer createPermissionPlayer(UUID uuid, String name) {
        BasicDBObject document = new BasicDBObject();
        document.put("id",0);
        document.put("name",name);
        document.put("uuid",uuid);
        MongoDriver.getInstance().getPlayerCollection().insertOne(document);
        return null;
    }
    @Override
    public void updateName(UUID uuid, String name) {

    }
}
