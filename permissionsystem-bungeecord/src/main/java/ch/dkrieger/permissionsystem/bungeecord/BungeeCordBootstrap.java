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
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.concurrent.TimeUnit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 20:43
 *
 */

public class BungeeCordBootstrap extends Plugin implements DKPermsPlatform, PermissionTaskManager {

    private static BungeeCordBootstrap INSTANCE;

    private PermissionCommandManager commandManager;
    private PermissionUpdateExecutor updateExecutor;
    private boolean cloudNetV2, cloudNetV3;

    @Override
    public void onLoad() {
        INSTANCE = this;
        this.commandManager = new BungeeCordCommandManager();
        this.updateExecutor = new BungeeCordPluginMessageUpdateExecutor();

        new PermissionSystem(this,this.updateExecutor,true);
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this,new PlayerListener());

        ProxyServer.getInstance().getScheduler().schedule(this,()->{
            checkCloudNet();
            if(cloudNetV2){
                CloudNetV2PermissionImport.AVAILABLE = true;
                this.updateExecutor = new CloudNetV2UpdateExecutor();
                PermissionUpdater.getInstance().setExecutor(this.updateExecutor);
                ProxyServer.getInstance().getPluginManager().registerListener(this,new BungeeCordCloudNetV2UpdateExecutor());
            }else if(cloudNetV3){
                this.updateExecutor = new CloudNetV3UpdateExecutor();
                PermissionUpdater.getInstance().setExecutor(this.updateExecutor);
                ProxyServer.getInstance().getPluginManager().registerListener(this,new BungeeCordCloudNetV3UpdateExecutor());
            }else if(this.updateExecutor instanceof BungeeCordPluginMessageUpdateExecutor){
                ProxyServer.getInstance().registerChannel("dkperms:dkperms");
                ProxyServer.getInstance().getPluginManager().registerListener(this,(BungeeCordPluginMessageUpdateExecutor)this.updateExecutor);
                ProxyServer.getInstance().getScheduler().schedule(this,(BungeeCordPluginMessageUpdateExecutor)this.updateExecutor
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
        return ProxyServer.getInstance().getVersion();
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
        ProxyServer.getInstance().getScheduler().runAsync(this,runnable);
    }

    public void runTaskLater(Runnable runnable, Long duration, TimeUnit unit) {
        ProxyServer.getInstance().getScheduler().schedule(this,runnable,duration,unit);
    }

    public void scheduleTask(Runnable runnable, Long repet, TimeUnit unit) {
        ProxyServer.getInstance().getScheduler().schedule(this,runnable,0L,repet,unit);
    }

    public boolean isCloudNetV2(){
        return this.cloudNetV2;
    }

    public boolean isCloudNetV3(){
        return this.cloudNetV3;
    }

    public static BungeeCordBootstrap getInstance() {
        return INSTANCE;
    }

    private void checkCloudNet(){
        Plugin plugin = ProxyServer.getInstance().getPluginManager().getPlugin("CloudNetAPI");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV2 = true;
            CommandPermission.ADVANCED = true;
            CommandPermission.JOINPOWER = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV2 found");
            return;
        }else this.cloudNetV2 = false;
        plugin = ProxyServer.getInstance().getPluginManager().getPlugin("CloudNet-Bridge");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV3 = true;
            CommandPermission.ADVANCED = true;
            CommandPermission.JOINPOWER = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV3 found");
            return;
        }else this.cloudNetV3 = false;
    }
}
