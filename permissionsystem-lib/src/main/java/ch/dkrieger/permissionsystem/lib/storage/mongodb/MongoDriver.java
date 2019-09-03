package ch.dkrieger.permissionsystem.lib.storage.mongodb;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 11.07.18 17:16
 *
 */
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDriver {

    private static MongoDriver instance;
    private String host,username, password, databasename;
    private int port;

    private MongoClient client;
    private MongoDatabase database;

    public MongoDriver(String host, int port, String username, String password, String databasename) {
        instance = this;
        this.host = host;
        this.username = username;
        this.password = password;
        this.databasename = databasename;
        this.port = port;
    }

    public void connect() {
        this.client = MongoClients.create("mongodb://"+this.username+":"+this.password+"@"+this.host+"/?authSource="+this.database+"&authMechanism=SCRAM-SHA-256");
        for(String s : this.client.listDatabaseNames()){
            System.out.println(s);
        }
        this.database = client.getDatabase(this.databasename);

        database.createCollection("dkperms_players", null);
    }

    public void disconnect(){
        if(this.client != null) this.client.close();
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection getPlayerCollection(){
        return this.database.getCollection("dkperms_players");
    }

    public MongoCollection getGroupCollection(){
        return this.database.getCollection("dkperms_groups");
    }

    public MongoCollection getPermissionCollection(){
        return this.database.getCollection("dkperms_permissions");
    }

    public MongoCollection getEntityCollection(){
        return this.database.getCollection("dkperms_entity");
    }

    public boolean isConnected(){
        return this.database != null;
    }

    public static MongoDriver getInstance() {
        return instance;
    }
}
