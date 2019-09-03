package ch.dkrieger.permissionsystem.lib.player;

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.PermissionType;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityProvider;
import ch.dkrieger.permissionsystem.lib.permission.PermissionProvider;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class PermissionPlayerManager {

    private static PermissionPlayerManager instance;
    private PermissionPlayerStorage storage;
    private Map<UUID,PermissionPlayer> players;

    public PermissionPlayerManager(PermissionPlayerStorage storage) {
        if(storage == null) throw new IllegalArgumentException("Storage can't be null.");
        instance = this;
        this.storage = storage;
        this.players =  new ConcurrentHashMap<>();
    }
    public Map<UUID, PermissionPlayer> getLoadedPlayers() {
        return this.players;
    }

    public PermissionPlayer getPermissionPlayer(UUID uuid){
        if(this.players.containsKey(uuid)) return this.players.get(uuid);
        try{
            return getPermissionPlayerSave(uuid);
        }catch (Exception ignored){}
        return null;
    }

    public PermissionPlayer getPermissionPlayer(String name){
        for(PermissionPlayer player : this.players.values()) if(player.getName().equalsIgnoreCase(name)) return player;
        final long timestamp = System.currentTimeMillis();
        try{
            PermissionPlayer player = this.storage.getPermissionPlayer(name);
            if(player != null){
                player.setPermissionData(PermissionProvider.getInstance().getStorage().getPermissions(PermissionType.PLAYER,player.getUUID()));
                player.setGroups(PermissionEntityProvider.getInstance().getStorage().getPermissionEntities(PermissionType.PLAYER,player.getUUID()));
                this.players.put(player.getUUID(),player);
                PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.INFO, PermissionSystem.PermissionDebugLevel.NORMAL,"Loaded player "+player.getName()+" in "+(System.currentTimeMillis()-timestamp)+"ms");
            }
            return player;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    public PermissionPlayer getPermissionPlayerSave(UUID uuid) throws Exception{
        final long timestamp = System.currentTimeMillis();
        PermissionPlayer player = this.storage.getPermissionPlayer(uuid);
        if(player != null){
            player.setPermissionData(PermissionProvider.getInstance().getStorage().getPermissions(PermissionType.PLAYER,uuid));
            player.setGroups(PermissionEntityProvider.getInstance().getStorage().getPermissionEntities(PermissionType.PLAYER,uuid));
            this.players.put(uuid,player);
            PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.INFO,PermissionSystem.PermissionDebugLevel.NORMAL,"Loaded player "+player.getName()+" in "+(System.currentTimeMillis()-timestamp)+"ms");
        }
        return player;
    }

    public PermissionPlayer createPermissionPlayer(UUID uuid,String name){
        final long timestamp = System.currentTimeMillis();
        PermissionPlayer player = this.storage.createPermissionPlayer(uuid,name);
        if(player != null){
            this.players.put(uuid,player);
            PermissionSystem.getInstance().debug(PermissionSystem.PermissionInfoLevel.INFO, PermissionSystem.PermissionDebugLevel.NORMAL,"created player "+player.getName()+" in "+(System.currentTimeMillis()-timestamp)+"ms");
        }
        return player;
    }

    public void setStorage(PermissionPlayerStorage storage) {
        if(storage == null) throw new IllegalArgumentException("Storage can't be null.");
        this.storage = storage;
    }

    public void checkName(UUID uuid, String name){
        PermissionPlayer player = getPermissionPlayer(uuid);
        if(player != null && !(player.getName().equals(name))){
            player.setName(name);
            this.storage.updateName(uuid,name);
        }
    }

    public static PermissionPlayerManager getInstance() {
        return instance;
    }
}
