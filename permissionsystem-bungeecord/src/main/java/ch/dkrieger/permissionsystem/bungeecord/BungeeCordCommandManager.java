package ch.dkrieger.permissionsystem.bungeecord;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 04.07.18 17:51
 *
 */

import ch.dkrieger.permissionsystem.lib.command.PermissionCommand;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandManager;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;

public class BungeeCordCommandManager implements PermissionCommandManager{

    private Collection<PermissionCommand> commands;

    public BungeeCordCommandManager() {
        this.commands = new LinkedHashSet<>();
    }
    public Collection<PermissionCommand> getCommands() {
        return this.commands;
    }
    public PermissionCommand getCommand(String name) {
        for(PermissionCommand command : this.commands) if(command.getName().equalsIgnoreCase(name)) return command;
        return null;
    }
    public void registerCommand(final PermissionCommand command) {
        BungeeCord.getInstance().getPluginManager().registerCommand(BungeeCordBootstrap.getInstance()
                ,new BungeeCordPermissionCommand(command));
    }
    private class BungeeCordPermissionCommand extends Command implements TabExecutor {

        private PermissionCommand command;

        public BungeeCordPermissionCommand(PermissionCommand command) {
            super(command.getName(),null,command.getAliases().toArray(new String[command.getAliases().size()]));
            this.command = command;
        }
        @Override
        public void execute(CommandSender sender, String[] args) {
            if(command.getPermission() == null || sender.hasPermission(command.getPermission())){
                BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBootstrap.getInstance(),()->{
                    command.execute(new BungeeCordPermissionCommandSender(sender),args);
                });
                return;
            }
            sender.sendMessage(TextComponent.fromLegacyText(Messages.PREFIX+Messages.NOPERMISSIONS));
            return;
        }
        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            return this.command.tabcomplete(new BungeeCordPermissionCommandSender(sender),args);
        }
    }

    private class BungeeCordPermissionCommandSender implements PermissionCommandSender {

        private CommandSender sender;

        public BungeeCordPermissionCommandSender(CommandSender sender) {
            this.sender = sender;
        }

        public String getName() {
            return sender.getName();
        }

        public UUID getUUID() {
            if(sender instanceof ProxiedPlayer) return ((ProxiedPlayer) sender).getUniqueId();
            else return null;
        }

        public Boolean hasPermission(String permission) {
            return sender.hasPermission(permission);
        }

        public void sendMessage(String message) {
            sender.sendMessage(TextComponent.fromLegacyText(message));
        }

        public void sendMessage(TextComponent component) {
            sender.sendMessage(component);
        }
    }
}
