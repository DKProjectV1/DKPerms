package ch.dkrieger.permissionsystem.lib;

import ch.dkrieger.permissionsystem.lib.command.defaults.CommandDKPerms;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandPermission;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandRank;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandTeam;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.config.MessageConfig;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityProvider;
import ch.dkrieger.permissionsystem.lib.entity.PermissionEntityStorage;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupStorage;
import ch.dkrieger.permissionsystem.lib.importation.PermissionImportManager;
import ch.dkrieger.permissionsystem.lib.importation.defaults.CloudNetV2PermissionImport;
import ch.dkrieger.permissionsystem.lib.importation.defaults.PermissionExImport;
import ch.dkrieger.permissionsystem.lib.permission.PermissionProvider;
import ch.dkrieger.permissionsystem.lib.permission.PermissionStorage;
import ch.dkrieger.permissionsystem.lib.platform.DKPermsPlatform;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerStorage;
import ch.dkrieger.permissionsystem.lib.storage.StorageType;
import ch.dkrieger.permissionsystem.lib.storage.mysql.*;
import ch.dkrieger.permissionsystem.lib.storage.mysql.table.TableManager;
import ch.dkrieger.permissionsystem.lib.storage.yaml.YamlPermissionEntityStorage;
import ch.dkrieger.permissionsystem.lib.storage.yaml.YamlPermissionGroupStorage;
import ch.dkrieger.permissionsystem.lib.storage.yaml.YamlPermissionPlayerStorage;
import ch.dkrieger.permissionsystem.lib.storage.yaml.YamlPermissionStorage;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateExecutor;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;
import ch.dkrieger.permissionsystem.lib.utils.Messages;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 13:20
 *
 */

public class PermissionSystem {

    private static PermissionSystem instance;
    private Properties dkpermsProperties;
    private String version;
    private DKPermsPlatform platform;
    private Config config;
    private MessageConfig messageconfig;
    private PermissionPlayerStorage playerStorage;
    private PermissionGroupStorage groupStorage;
    private PermissionStorage permissionStorage;
    private PermissionEntityStorage entityStorage;
    private MySQL mysql;

    public PermissionSystem(DKPermsPlatform platform, PermissionUpdateExecutor updateexecutor, Boolean advanced) {
        instance = this;
        this.platform = platform;
        new Messages("DKPerms");
        this.dkpermsProperties = new Properties();
        try {
            this.dkpermsProperties.load(getClass().getClassLoader().getResourceAsStream("dkperms.properties"));
        } catch (Exception exception) {
            System.out.println(Messages.SYSTEM_PREFIX+"Could not load dkperms plugin build informations");
            this.version = "Unknown";
        }
        this.version = dkpermsProperties.getProperty("version");
        System.out.println(Messages.SYSTEM_PREFIX+"plugin is starting");
        System.out.println(Messages.SYSTEM_PREFIX+"PermissionSystem "+this.version+" by Davide Wietlisbach");

        systemBootstrap(updateexecutor,advanced);

        System.out.println(Messages.SYSTEM_PREFIX+"plugin successfully started");
    }
    private void systemBootstrap(PermissionUpdateExecutor updateexecutor,Boolean advanced){
        this.platform.getFolder().mkdirs();

        this.config = new Config(this.platform,advanced);
        this.messageconfig = new MessageConfig(this.platform);
        this.config.loadConfig();
        this.messageconfig.loadConfig();

        PermissionImportManager impmanager = new PermissionImportManager();
        impmanager.registerImport(new CloudNetV2PermissionImport());
        impmanager.registerImport(new PermissionExImport());

        setupStorage();

        new PermissionProvider(this.permissionStorage);
        new PermissionEntityProvider(this.entityStorage);

        new PermissionPlayerManager(this.playerStorage);
        new PermissionGroupManager(this.groupStorage);

        new PermissionUpdater(updateexecutor);

        registerCommands();

        if(Config.SYNCHRONISE_TASK_ENABLED){
            this.platform.getTaskManager().scheduleTask(()->{
                this.platform.getTaskManager().runTaskAsync(()->{
                    syncGroups();
                });
            },Config.SYNCHRONISE_TASK_DELAY,TimeUnit.MINUTES);
        }
        this.platform.getTaskManager().scheduleTask(()->{
            this.platform.getTaskManager().runTaskAsync(()->{
                if(this.permissionStorage != null) this.permissionStorage.onTimeOutDeleteTask();
            });
        },5L,TimeUnit.MINUTES);
    }
    private void setupStorage(){
        if(Config.STORAGE_TYPE == StorageType.MYSQL){
            this.mysql = new MySQL(Messages.SYSTEM_NAME,Config.STORAGE_MYSQL_HOST,Config.STORAGE_MYSQL_PORT,Config.STORAGE_MYSQL_USER
                    ,Config.STORAGE_MYSQL_PASSWORD,Config.STORAGE_MYSQL_DATABASE);
            this.mysql.connect();
            if(this.mysql.isConnect()){
                new TableManager(this.mysql);

                this.playerStorage = new MySQLPermissionPlayerStorage();
                this.groupStorage= new MySQLPermissionGroupStorage();
                this.permissionStorage = new MySQLPermissionStorage();
                this.entityStorage = new MySQLPermissionEntityStorage();
                return;
            }
        }/*else if(Config.STORAGE_TYPE == StorageType.MONGODB){
            MongoDriver driver = new MongoDriver(Config.STORAGE_MONGODB_HOST,Config.STORAGE_MONGODB_PORT,Config.STORAGE_MONGODB_USER
            ,Config.STORAGE_MONGODB_PASSWORD,Config.STORAGE_MONGODB_DATABASE);
            driver.connect();
            if(driver.isConnected()){
                this.playerstorage = new MongoDBPermissionPlayerStorage();
            }
            return;
        }*/
        Config.STORAGE_TYPE = StorageType.YAML;
        this.playerStorage = new YamlPermissionPlayerStorage();
        this.groupStorage = new YamlPermissionGroupStorage((YamlPermissionPlayerStorage)this.playerStorage);
        this.permissionStorage = new YamlPermissionStorage((YamlPermissionGroupStorage)this.groupStorage,(YamlPermissionPlayerStorage)this.playerStorage);
        this.entityStorage = new YamlPermissionEntityStorage((YamlPermissionGroupStorage)this.groupStorage,(YamlPermissionPlayerStorage)this.playerStorage);
        System.out.println("Using "+Config.STORAGE_TYPE.getName()+" v"+Config.STORAGE_TYPE.getVersion()+" storage");
    }
    private void registerCommands(){
        if(Config.SECURITY_DISABLECOMMANDS) return;
        this.platform.getCommandManager().registerCommand(new CommandDKPerms());
        if(Config.COMMAND_PERMISSION_ENABLED) this.platform.getCommandManager().registerCommand(new CommandPermission());
        if(Config.COMMAND_RANK_ENABLED) this.platform.getCommandManager().registerCommand(new CommandRank());
        if(Config.COMMAND_TEAM_ENABLED) this.platform.getCommandManager().registerCommand(new CommandTeam());
    }
    public void disable(){
        if(this.mysql != null) this.mysql.disconnect();
        //if(MongoDriver.getInstance() != null) MongoDriver.getInstance().disconnect();
    }
    public void sync(){
        try{
            syncGroups();
            PermissionPlayerManager.getInstance().getLoadedPlayers().clear();
        }catch (Exception exception){
            debug(PermissionInfoLevel.WARN,PermissionDebugLevel.NORMAL,"Could not synchronise.");
        }
    }
    public void syncGroups(){
        PermissionGroupManager.getInstance().load();
    }
    public void debug(PermissionDebugLevel level, String message){
        debug(PermissionInfoLevel.INFO,level,message);
    }
    public void debug(PermissionInfoLevel infolevel, PermissionDebugLevel level, String message){
        if(infolevel != PermissionInfoLevel.INFO){
            if(infolevel == PermissionInfoLevel.WARN) System.out.println(Messages.SYSTEM_PREFIX+"Warn: "+message);
            else System.out.println(Messages.SYSTEM_PREFIX+"Error: "+message);
        }else{
            if(Config.DEBUG_ENABLED && Config.DEBUG_LEVEL.canSee(level)) System.out.println(Messages.SYSTEM_PREFIX+message);
        }
    }
    public String getVersion() {
        return version;
    }
    public DKPermsPlatform getPlatform() {
        return platform;
    }
    public MySQL getMySQL() {
        return mysql;
    }
    public Config getConfig() {
        return config;
    }
    public MessageConfig getMessageConfig() {
        return messageconfig;
    }
    public static PermissionSystem getInstance() {
        return instance;
    }
    public enum PermissionInfoLevel {
        INFO(),
        WARN(),
        ERROR();
    }
    public enum PermissionDebugLevel {
        LOW(),
        NORMAL(),
        HIGH();

        public Boolean canSee(PermissionDebugLevel level){
            if(this == HIGH) return true;
            else if(this == NORMAL && (level == NORMAL || level == LOW)) return true;
            else if(this == level) return true;
            return false;
        }
    }
}
