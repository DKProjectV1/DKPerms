package ch.dkrieger.permissionsystem.lib.storage;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 13:57
 *
 */

public enum StorageType {

    YAML("Yaml","1.0.0"),
    MYSQL("Mysql","1.0.0");
    //MONGODB("MongoDB","1.0.0");

    private String name,version;

    StorageType(String name,String version) {
        this.name = name;
        this.version = version;
    }
    public String getName() {
        return name;
    }
    public String getVersion() {
        return version;
    }
}
