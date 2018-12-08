package ch.dkrieger.permissionsystem.lib.command.defaults;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 16:24
 *
 */

import ch.dkrieger.permissionsystem.lib.PermissionSystem;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommand;
import ch.dkrieger.permissionsystem.lib.command.PermissionCommandSender;
import ch.dkrieger.permissionsystem.lib.utils.Messages;

import java.util.LinkedList;
import java.util.List;

public class CommandDKPerms extends PermissionCommand {

    public CommandDKPerms() {
        super("dkperms",null);
    }
    @Override
    public void execute(PermissionCommandSender sender, String[] args) {
        sender.sendMessage(Messages.PREFIX+"§7PermissionSystem v§c"+PermissionSystem.getInstance().getVersion()+" §7by §cDkrieger");
    }
    @Override
    public List<String> tabcomplete(PermissionCommandSender sender, String[] args) {
        return new LinkedList<>();
    }
}
