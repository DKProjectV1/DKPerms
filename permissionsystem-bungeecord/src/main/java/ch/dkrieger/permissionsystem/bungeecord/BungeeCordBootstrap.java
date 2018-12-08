package ch.dkrieger.permissionsystem.bungeecord;

import ch.dkrieger.permissionsystem.bungeecord.listeners.PlayerListener;
import ch.dkrieger.permissionsystem.bungeecord.updater.BungeeCordPluginMessageUpdateExecutor;
import ch.dkrieger.permissionsystem.bungeecord.updater.cloudnet.BungeeCordCloudNetV2UpdateExecutor;
import ch.dkrieger.permissionsystem.bungeecord.updater.cloudnet.BungeeCordCloudNetV3UpdateExecutor;
import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandManager;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandPermission;
import ch.dkrieger.permissionsystem.lib.importation.defaults.CloudNetV2PermissionImport;
import ch.dkrieger.permissionsystem.lib.platform.DKPermsPlatform;
import ch.dkrieger.permissionsystem.lib.task.PermissionTaskManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateExecutor;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;
import ch.dkrieger.permissionsystem.lib.updater.cloudnet.CloudNetV2UpdateExecutor;
import ch.dkrieger.permissionsystem.lib.updater.cloudnet.CloudNetV3UpdateExecutor;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class BungeeCordBootstrap extends Plugin implements DKPermsPlatform, PermissionTaskManager {

    private static BungeeCordBootstrap instance;
    private PermissionCommandManager commandManager;
    private PermissionUpdateExecutor updateExecutor;
    private boolean cloudNetV2, cloudNetV3;

    @Override
    public void onLoad() {
        instance = this;
        this.commandManager = new BungeeCordCommandManager();
        this.updateExecutor = new BungeeCordPluginMessageUpdateExecutor();

        new PermissionSystem(this,this.updateExecutor,true);
    }
    @Override
    public void onEnable() {
        BungeeCord.getInstance().getPluginManager().registerListener(this,new PlayerListener());

        BungeeCord.getInstance().getScheduler().schedule(this,()->{
            checkCloudNet();
            if(cloudNetV2){
                CloudNetV2PermissionImport.AVAILABLE = true;
                this.updateExecutor = new CloudNetV2UpdateExecutor();
                PermissionUpdater.getInstance().setExecutor(this.updateExecutor);
                BungeeCord.getInstance().getPluginManager().registerListener(this,new BungeeCordCloudNetV2UpdateExecutor());
            }else if(cloudNetV3){
                this.updateExecutor = new CloudNetV3UpdateExecutor();
                PermissionUpdater.getInstance().setExecutor(this.updateExecutor);
                BungeeCord.getInstance().getPluginManager().registerListener(this,new BungeeCordCloudNetV3UpdateExecutor());
            }else if(this.updateExecutor instanceof BungeeCordPluginMessageUpdateExecutor){
                BungeeCord.getInstance().registerChannel("dkperms");
                BungeeCord.getInstance().getPluginManager().registerListener(this,(BungeeCordPluginMessageUpdateExecutor)this.updateExecutor);
                BungeeCord.getInstance().getScheduler().schedule(this,(BungeeCordPluginMessageUpdateExecutor)this.updateExecutor
                ,5L,TimeUnit.MINUTES);
            }
        },3L, TimeUnit.SECONDS);
    }
    @Override
    public void onDisable() {
        PermissionSystem.getInstance().disable();
    }
    public String getPlatformName() {
        return "bungeecord";
    }
    public String getServerVersion() {
        return BungeeCord.getInstance().getVersion();
    }
    public File getFolder() {
        return new File("plugins/DKPerms/");
    }
    public PermissionCommandManager getCommandManager() {
        return this.commandManager;
    }
    public PermissionTaskManager getTaskManager() {
        return this;
    }
    public String translateColorCodes(String value) {
        return ChatColor.translateAlternateColorCodes('&',value);
    }
    public void runTaskAsync(Runnable runnable) {
        BungeeCord.getInstance().getScheduler().runAsync(this,runnable);
    }
    public void runTaskLater(Runnable runnable, Long duration, TimeUnit unit) {
        BungeeCord.getInstance().getScheduler().schedule(this,runnable,duration,unit);
    }
    public void scheduleTask(Runnable runnable, Long repet, TimeUnit unit) {
        BungeeCord.getInstance().getScheduler().schedule(this,runnable,0L,repet,unit);
    }
    public Boolean isCloudNetV2(){
        return this.cloudNetV2;
    }
    public Boolean isCloudNetV3(){
        return this.cloudNetV3;
    }
    public static BungeeCordBootstrap getInstance() {
        return instance;
    }
    private void checkCloudNet(){
        Plugin plugin = BungeeCord.getInstance().getPluginManager().getPlugin("CloudNetAPI");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV2 = true;
            CommandPermission.ADVANCED = true;
            CommandPermission.JOINPOWER = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV2 found");
            return;
        }else this.cloudNetV2 = false;
        plugin = BungeeCord.getInstance().getPluginManager().getPlugin("CloudNet-Bridge");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV3 = true;
            CommandPermission.ADVANCED = true;
            CommandPermission.JOINPOWER = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV3 found");
            return;
        }else this.cloudNetV3 = false;
    }
}
