package ch.dkrieger.permissionsystem.lib.command;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 23.06.18 14:45
 *
 */

public interface PermissionCommandSender {

    String getName();

    UUID getUUID();

    boolean hasPermission(String permission);

    void sendMessage(String message);

    void sendMessage(TextComponent component);

    default boolean isConsole(){
        return getUUID() == null;
    }

    default boolean isPlayer(){
        return getUUID() != null;
    }
}
