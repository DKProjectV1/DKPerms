package ch.dkrieger.permissionsystem.lib.storage.yaml;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.07.18 13:27
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.config.SimpleConfig;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerStorage;

import java.io.File;
import java.util.UUID;

public class YamlPermissionPlayerStorage extends SimpleConfig implements PermissionPlayerStorage{

    public YamlPermissionPlayerStorage() {
        super(PermissionSystem.getInstance().getPlatform(),new File(Config.STORAGE_FOLDER,"players.yml"));
        loadConfig();
    }
    @Override
    public void onLoad() {}

    @Override
    public void registerDefaults() {
        addValue("nextid",1);
    }
    @Override
    public PermissionPlayer getPermissionPlayer(UUID uuid) throws Exception {
        if(getStringValue("players."+uuid+".name") != null){
            return new PermissionPlayer(getIntValue("players."+uuid+".id"),getStringValue("players."+uuid+".name"),uuid);
        }
        return null;
    }
    @Override
    public PermissionPlayer getPermissionPlayer(String name) throws Exception {
        try{
            if(getStringValue("nameplayers."+name.toLowerCase()) != null)
                return getPermissionPlayer(UUID.fromString(getStringValue("nameplayers."+name.toLowerCase())));
        }catch (Exception exception){}
        return null;
    }
    @Override
    public PermissionPlayer createPermissionPlayer(UUID uuid, String name) {
        int id = getIntValue("nextid");
        if(id == 0) id = 1;
        setValue("players."+uuid+".id",id);
        setValue("players."+uuid+".name",name);
        setValue("nameplayers."+name.toLowerCase(),""+uuid);
        setValue("nextid",id+1);
        save();
        return new PermissionPlayer(id,name,uuid);
    }
    @Override
    public void updateName(UUID uuid, String name) {
        setValue("nameplayers."+name.toLowerCase(),""+uuid);
        setValue("players."+uuid+".name",name);
        save();
    }
}
