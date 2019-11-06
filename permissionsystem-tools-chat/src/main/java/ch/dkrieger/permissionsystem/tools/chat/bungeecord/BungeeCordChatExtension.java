package ch.dkrieger.permissionsystem.tools.chat.bungeecord;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.09.19, 19:42
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.player.PlayerDesign;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeCordChatExtension extends Plugin implements Listener {

    private String format;

    @Override
    public void onEnable() {
        PermissionSystem.getInstance().getConfig().addValue("extension.chat.format","&8[&e[server]&8][display][player]&8: &f");
        PermissionSystem.getInstance().getConfig().save();
        this.format = PermissionSystem.getInstance().getConfig().getMessageValue("extension.chat.format");
        BungeeCord.getInstance().getPluginManager().registerListener(this,this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent event){
        if(event.getSender() instanceof ProxiedPlayer && !event.isCommand() && !event.isCancelled()){
            PermissionGroup group = PermissionPlayerManager.getInstance().getPermissionPlayer(((ProxiedPlayer) event.getSender()).getUniqueId()).getHighestGroup();
            if(group == null) return;
            PlayerDesign design = group.getPlayerDesign();
            if(design == null) return;
            String prefix = ChatColor.translateAlternateColorCodes('&',design.getPrefix()).replace("_"," ");
            String suffix = ChatColor.translateAlternateColorCodes('&',design.getSuffix()).replace("_"," ");
            String display = ChatColor.translateAlternateColorCodes('&',design.getDisplay()).replace("_"," ");
            if(prefix.equalsIgnoreCase("-1")) prefix = "";
            if(suffix.equalsIgnoreCase("-1")) suffix = "";
            if(display.equalsIgnoreCase("-1")) display = "";

            String serverName = "Unknown";
            Server server = ((ProxiedPlayer) event.getSender()).getServer();
            if(server != null) serverName = server.getInfo().getName();

            if(((ProxiedPlayer) event.getSender()).hasPermission("dkperms.chat.color")) event.setMessage(ChatColor.translateAlternateColorCodes('&',event.getMessage()));
            event.setCancelled(true);
            BungeeCord.getInstance().broadcast(this.format
                    .replace("[display]",display)
                    .replace("[prefix]",prefix)
                    .replace("[player]",((ProxiedPlayer) event.getSender()).getName())
                    .replace("[suffix]",suffix)
                    .replace("[server]",serverName)
                    +event.getMessage());
        }
    }
}
