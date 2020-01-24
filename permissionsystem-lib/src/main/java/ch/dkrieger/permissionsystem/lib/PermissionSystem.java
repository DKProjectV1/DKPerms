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
import ch.dkrieger.permissionsystem.lib.utils.UpdateChecker;

import java.net.MalformedURLException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 13:20
 *
 */

public class PermissionSystem {

    private static PermissionSystem INSTANCE;

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
    private UpdateChecker updateChecker;

    public PermissionSystem(DKPermsPlatform platform, PermissionUpdateExecutor updateexecutor, boolean advanced) {
        INSTANCE = this;
        this.platform = platform;
        new Messages("DKPerms");
        this.dkpermsProperties = new Properties();
        try {
            this.dkpermsProperties.load(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("dkperms.properties")));
        } catch (Exception exception) {
            System.out.println(Messages.SYSTEM_PREFIX+"Could not load dkperms plugin build informations");
            this.version = "Unknown";
        }
        this.version = dkpermsProperties.getProperty("version");

        System.out.println(Messages.SYSTEM_PREFIX+"plugin is starting");
        System.out.println(Messages.SYSTEM_PREFIX+"PermissionSystem "+this.version+" by Davide Wietlisbach");

        try {
            this.updateChecker = new UpdateChecker(58810);
            if(this.updateChecker.hasNewVersion()) {
                System.out.println(Messages.SYSTEM_PREFIX + "New version available: " + this.updateChecker.getLatestVersionString());
            }
        } catch (MalformedURLException ignored) {
            System.out.println(Messages.SYSTEM_PREFIX + "Can't check newest version.");
        }

        systemBootstrap(updateexecutor,advanced);

        System.out.println(Messages.SYSTEM_PREFIX+"plugin successfully started");
    }

    private void systemBootstrap(PermissionUpdateExecutor updateExecutor,boolean advanced){
        this.platform.getFolder().mkdirs();

        this.config = new Config(this.platform,advanced);
        this.messageconfig = new MessageConfig(this.platform);
        this.config.loadConfig();
        this.messageconfig.loadConfig();

        PermissionImportManager importManager = new PermissionImportManager();
        importManager.registerImport(new CloudNetV2PermissionImport());
        importManager.registerImport(new PermissionExImport());

        setupStorage();

        new PermissionProvider(this.permissionStorage);
        new PermissionEntityProvider(this.entityStorage);

        new PermissionPlayerManager(this.playerStorage);
        new PermissionGroupManager(this.groupStorage);

        new PermissionUpdater(updateExecutor);

        registerCommands();

        if(Config.SYNCHRONISE_TASK_ENABLED){
            this.platform.getTaskManager().scheduleTask(()-> this.platform.getTaskManager().runTaskAsync(this::syncGroups)
                    ,Config.SYNCHRONISE_TASK_DELAY,TimeUnit.MINUTES);
        }

        this.platform.getTaskManager().scheduleTask(()-> this.platform.getTaskManager().runTaskAsync(()->{
            if(this.permissionStorage != null) this.permissionStorage.onTimeOutDeleteTask();
        }),5L,TimeUnit.MINUTES);
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
        }
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

    public void debug(PermissionInfoLevel infoLevel, PermissionDebugLevel level, String message){
        if(infoLevel != PermissionInfoLevel.INFO){
            if(infoLevel == PermissionInfoLevel.WARN) System.out.println(Messages.SYSTEM_PREFIX+"Warn: "+message);
            else System.out.println(Messages.SYSTEM_PREFIX+"Error: "+message);
        }else{
            if(Config.DEBUG_ENABLED && Config.DEBUG_LEVEL.canSee(level)) System.out.println(Messages.SYSTEM_PREFIX+message);
        }
    }

    public String getVersion() {
        return version;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
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
        return INSTANCE;
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

        public boolean canSee(PermissionDebugLevel level){
            if(this == HIGH) return true;
            else if(this == NORMAL && (level == NORMAL || level == LOW)) return true;
            else return this == level;
        }
    }
}
