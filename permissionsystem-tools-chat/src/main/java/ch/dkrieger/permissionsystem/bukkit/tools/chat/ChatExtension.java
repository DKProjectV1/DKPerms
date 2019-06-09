package ch.dkrieger.permissionsystem.bukkit.tools.chat;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 17:46
 *
 */

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.player.PlayerDesign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatExtension extends JavaPlugin implements Listener{

    private String format;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this,()->{
            PermissionSystem.getInstance().getConfig().addValue("extension.chat.format","[display][player]&8: &f");
            PermissionSystem.getInstance().getConfig().save();
            this.format = PermissionSystem.getInstance().getConfig().getMessageValue("extension.chat.format");
            Bukkit.getPluginManager().registerEvents(this,this);
        },5L);
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        PermissionGroup group = PermissionPlayerManager.getInstance().getPermissionPlayer(event.getPlayer().getUniqueId()).getHighestGroup();
        if(group == null) return;
        PlayerDesign design = group.getPlayerDesign();
        if(design == null) return;
        String prefix = ChatColor.translateAlternateColorCodes('&',design.getPrefix()).replace("_"," ");
        String suffix = ChatColor.translateAlternateColorCodes('&',design.getSuffix()).replace("_"," ");
        String display = ChatColor.translateAlternateColorCodes('&',design.getDisplay()).replace("_"," ");
        if(prefix.equalsIgnoreCase("-1")) prefix = "";
        if(suffix.equalsIgnoreCase("-1")) suffix = "";
        if(display.equalsIgnoreCase("-1")) display = "";

        if(BukkitBootstrap.getInstance().getPlaceHolderAPI() != null){
            BukkitBootstrap.getInstance().getPlaceHolderAPI().set(event.getPlayer(),prefix);
        }

        if(event.getPlayer().hasPermission("dkperms.chat.color"))
            event.setMessage(ChatColor.translateAlternateColorCodes('&',event.getMessage()));
        event.setFormat(this.format.replace("[display]",display).replace("[prefix]",prefix)
                .replace("[player]",event.getPlayer().getName()).replace("[suffix]",suffix)+"%2$s");
    }
}
