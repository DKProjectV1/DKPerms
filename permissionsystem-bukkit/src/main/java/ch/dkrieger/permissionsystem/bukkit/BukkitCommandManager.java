package ch.dkrieger.permissionsystem.bukkit;

import ch.dkrieger.permissionsystem.lib.command.PermissionCommandManager;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommand;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.utils.Messages;
import net.md_5.bungee.api.chat.TextComponent;
import net.pl3x.bukkit.chatapi.ComponentSender;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;
/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.06.18 15:43
 *
 */

public class BukkitCommandManager implements PermissionCommandManager {

    private Collection<PermissionCommand> commands;

    public BukkitCommandManager() {
        this.commands = new LinkedHashSet<>();
    }
    public Collection<PermissionCommand> getCommands() {
        return commands;
    }
    public PermissionCommand getCommand(String name) {
        for(PermissionCommand command : this.commands) if(command.getName().equalsIgnoreCase(name)) return command;
        return null;
    }
    public void registerCommand(final PermissionCommand command) {
        this.commands.add(command);
        CommandMap cmap = null;
        try{
            final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            cmap = (CommandMap)field.get(Bukkit.getServer());
            cmap.register("",new BukkitPermissionCommand(command));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private class BukkitPermissionCommand extends Command implements TabCompleter{

        private BukkitPermissionCommand instance;
        private PermissionCommand command;

        public BukkitPermissionCommand(PermissionCommand command) {
            super(command.getName(),"","",command.getAliases());
            instance = this;
            this.command = command;
        }
        @Override
        public boolean execute(CommandSender sender, String s, String[] args) {
            if(command.getPermission() == null || sender.hasPermission(command.getPermission())){
                Bukkit.getScheduler().runTaskAsynchronously(BukkitBootstrap.getInstance(),()->{
                    command.execute(new BukkitPermissionCommandSender(sender),args);
                });
                return true;
            }
            sender.sendMessage(Messages.PREFIX+Messages.NOPERMISSIONS);
            return true;
        }
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
            if(command == this.instance && command.getPermission() == null || !sender.hasPermission(command.getPermission())){
                return this.command.tabcomplete(new BukkitPermissionCommandSender(sender),args);
            }
            return new LinkedList<>();
        }
    }
    private class BukkitPermissionCommandSender implements PermissionCommandSender {

        private CommandSender sender;

        public BukkitPermissionCommandSender(CommandSender sender) {
            this.sender = sender;
        }
        public String getName() {
            return sender.getName();
        }
        public UUID getUUID() {
            if(sender instanceof Player) return ((Player) sender).getUniqueId();
            else return null;
        }
        public Boolean hasPermission(String permission) {
            return sender.hasPermission(permission);
        }
        public void sendMessage(String message) {
            sender.sendMessage(message);
        }
        public void sendMessage(TextComponent component) {
            if(sender instanceof Player){
                try{
                    ComponentSender.sendMessage((Player)sender,component);
                }catch (Exception exception){
                    sender.sendMessage(component.getText());
                }
            }else sender.sendMessage(component.getText());
        }
    }
}
