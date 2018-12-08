package ch.dkrieger.permissionsystem.lib.command;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.06.18 14:45
 *
 */

public interface PermissionCommandSender {

    public String getName();

    public UUID getUUID();

    public Boolean hasPermission(String permission);

    public void sendMessage(String message);

    public void sendMessage(TextComponent component);
}
