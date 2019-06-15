package ch.dkrieger.permissionsystem.bukkit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 10.06.18 14:17
 *
 */

import ch.dkrieger.permissionsystem.bukkit.hook.PlaceHolderAPIHook;
import ch.dkrieger.permissionsystem.bukkit.hook.WorldEditPermissionResolver;
import ch.dkrieger.permissionsystem.bukkit.hook.vault.VaultPermissionHook;
import ch.dkrieger.permissionsystem.bukkit.listeners.CloudNetV2Listener;
import ch.dkrieger.permissionsystem.bukkit.listeners.PlayerListener;
import ch.dkrieger.permissionsystem.bukkit.updater.SpigotPluginMessageUpdateExecutor;
import ch.dkrieger.permissionsystem.bukkit.updater.cloudnet.SpigotCloudNetV2UpdateExecutor;
import ch.dkrieger.permissionsystem.bukkit.updater.cloudnet.SpigotCloudNetV3UpdateExecutor;
import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandManager;
import ch.dkrieger.permissionsystem.lib.command.defaults.CommandPermission;
import ch.dkrieger.permissionsystem.lib.config.Config;
import ch.dkrieger.permissionsystem.lib.importation.defaults.CloudNetV2PermissionImport;
import ch.dkrieger.permissionsystem.lib.platform.DKPermsPlatform;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.task.PermissionTaskManager;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdateExecutor;
import ch.dkrieger.permissionsystem.lib.updater.PermissionUpdater;
import ch.dkrieger.permissionsystem.lib.updater.cloudnet.CloudNetV2UpdateExecutor;
import ch.dkrieger.permissionsystem.lib.updater.cloudnet.CloudNetV3UpdateExecutor;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import ch.dkrieger.permissionsystem.lib.utils.PermissionDocument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BukkitBootstrap extends JavaPlugin implements DKPermsPlatform, PermissionTaskManager {

    private static BukkitBootstrap instance;
    private BukkitCommandManager commandManager;
    private PermissionUpdateExecutor updateExecutor;
    private String server;
    private boolean cloudNetV2, cloudNetV3;
    private List<WaitingRunnable> waitingRunnables;

    private PlaceHolderAPIHook placeHolderAPI;

    @Override
    public void onLoad() {
        instance = this;

        this.server = Bukkit.getServer().getName();

        this.commandManager = new BukkitCommandManager();

        this.updateExecutor = new SpigotPluginMessageUpdateExecutor();
        this.waitingRunnables = new LinkedList<>();

        new PermissionSystem(this,this.updateExecutor,false);
        hook();
    }
    @Override
    public void onEnable() {
        //Hook into WorldEdit
        if(Bukkit.getPluginManager().getPlugin("WorldEdit") != null){
            System.out.println(Messages.SYSTEM_PREFIX+"WorldEdit found");
            new WorldEditPermissionResolver();
        }
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
        Bukkit.getScheduler().runTaskLater(this,()->{
            checkCloudNet();

            if(cloudNetV2){
                Bukkit.getPluginManager().registerEvents(new CloudNetV2Listener(),this);
                this.updateExecutor = new CloudNetV2UpdateExecutor();
                Bukkit.getPluginManager().registerEvents(new SpigotCloudNetV2UpdateExecutor(),this);
                PermissionUpdater.getInstance().setExecutor(this.updateExecutor);
                CloudNetV2PermissionImport.AVAILABLE = true;
            }else if(cloudNetV3){
                this.updateExecutor = new CloudNetV3UpdateExecutor();
                Bukkit.getPluginManager().registerEvents(new SpigotCloudNetV3UpdateExecutor(),this);
                PermissionUpdater.getInstance().setExecutor(this.updateExecutor);
            }else if(this.updateExecutor instanceof SpigotPluginMessageUpdateExecutor){
                Bukkit.getMessenger().registerOutgoingPluginChannel(this,"dkperms:dkperms");
                Bukkit.getMessenger().registerIncomingPluginChannel(this,"dkperms:dkperms",(SpigotPluginMessageUpdateExecutor)this.updateExecutor);
            }
            for(Player player : Bukkit.getOnlinePlayers()){
                try {
                    Class<?> clazz = PlayerListener.reflectCraftClazz(".entity.CraftHumanEntity");
                    Field field = null;
                    if(clazz != null){
                        field = clazz.getDeclaredField("perm");
                    }else field = Class.forName("net.glowstone.entity.GlowHumanEntity").getDeclaredField("permissions");
                    field.setAccessible(true);
                    field.set(player,new DKPermissable(player));
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if(Bukkit.getOnlinePlayers().size() > 0 && this.updateExecutor instanceof SpigotPluginMessageUpdateExecutor){
                ((SpigotPluginMessageUpdateExecutor)this.updateExecutor).sendToBungeeCord(new PermissionDocument("getserver"));
            }
            for(WaitingRunnable runnable : this.waitingRunnables){
                if(runnable.type == WaitingRunnableType.ASYNC){
                    Bukkit.getScheduler().runTaskAsynchronously(this,runnable.runnable);
                }else if(runnable.type == WaitingRunnableType.LATER){
                    Bukkit.getScheduler().runTaskLater(this,runnable.runnable,runnable.ticks);
                }else if(runnable.type == WaitingRunnableType.SCHEDULE){
                    Bukkit.getScheduler().runTaskTimer(this,runnable.runnable,0L,runnable.ticks);
                }
            }
        },8);
    }
    @Override
    public void onDisable() {
        PermissionSystem.getInstance().disable();
    }
    public String getPlatformName() {
        return "bukkit";
    }
    public String getServerVersion() {
        return Bukkit.getVersion();
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
    public String getServerName() {
        return server.toLowerCase();
    }

    public PlaceHolderAPIHook getPlaceHolderAPI() {
        return placeHolderAPI;
    }

    public void runTaskAsync(Runnable runnable) {
        if(isEnabled()) Bukkit.getScheduler().runTaskAsynchronously(this,runnable);
        else this.waitingRunnables.add(new WaitingRunnable(runnable,0L,WaitingRunnableType.ASYNC));
    }
    public void runTaskLater(Runnable runnable, Long duration, TimeUnit unit) {
        if(isEnabled()) Bukkit.getScheduler().runTaskLater(this,runnable,unit.toSeconds(duration)*20);
        else this.waitingRunnables.add(new WaitingRunnable(runnable,unit.toSeconds(duration)*20,WaitingRunnableType.LATER));
    }
    public void scheduleTask(Runnable runnable, Long repet, TimeUnit unit) {
        if(isEnabled()) Bukkit.getScheduler().runTaskTimer(this,runnable,0L,unit.toSeconds(repet)*20);
        else this.waitingRunnables.add(new WaitingRunnable(runnable,unit.toSeconds(repet)*20,WaitingRunnableType.SCHEDULE));
    }
    public static BukkitBootstrap getInstance() {
        return instance;
    }
    public void updateDisplayName(UUID uuid){
        updateDisplayName(Bukkit.getPlayer(uuid), PermissionPlayerManager.getInstance().getPermissionPlayer(uuid));
    }

    public void updateDisplayName(Player player){
        updateDisplayName(player, PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId()));
    }

    public void updateDisplayName(Player player, PermissionPlayer networkPlayer){
        if(player == null|| networkPlayer== null) return;
        if(Config.PLAYER_DISPLAYNAME_ENABLED) player.setDisplayName(Config.PLAYER_DISPLAYNAME_FORMAT
                .replace("[display]",networkPlayer.getPlayerDesign().getDisplay())
                .replace("[prefix]",networkPlayer.getPlayerDesign().getPrefix())
                .replace("[suffix]",networkPlayer.getPlayerDesign().getSuffix())
                .replace("[player]",player.getName())
                .replace("[color]",networkPlayer.getColor()));
    }

    private void checkCloudNet(){
        Plugin plugin = getServer().getPluginManager().getPlugin("CloudNetAPI");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV2 = true;
            CommandPermission.ADVANCED = true;
            CommandPermission.JOINPOWER = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV2 found");
            return;
        }else this.cloudNetV2 = false;
        plugin = getServer().getPluginManager().getPlugin("CloudNet-Bridge");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV3 = true;
            CommandPermission.ADVANCED = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV3 found");
        }else this.cloudNetV3 = false;
    }

    private void hook(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            System.out.println(Messages.SYSTEM_PREFIX+"PlaceHolderAPI found");
            this.placeHolderAPI = new PlaceHolderAPIHook();
        }
        if(Bukkit.getPluginManager().getPlugin("Vault") != null){
            System.out.println(Messages.SYSTEM_PREFIX+"Vault found");
            new VaultPermissionHook(this);
        }
    }

    private class WaitingRunnable {

        private Runnable runnable;
        private Long ticks;
        private WaitingRunnableType type;

        public WaitingRunnable(Runnable runnable, Long ticks, WaitingRunnableType type) {
            this.runnable = runnable;
            this.ticks = ticks;
            this.type = type;
        }
    }
    private enum WaitingRunnableType {
        ASYNC(),
        LATER(),
        SCHEDULE();
    }
    public void setServerName(String server) {
        this.server = server;
    }
}
