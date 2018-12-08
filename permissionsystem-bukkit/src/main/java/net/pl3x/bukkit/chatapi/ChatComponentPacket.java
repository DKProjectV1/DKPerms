package net.pl3x.bukkit.chatapi;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 20:25
 *
 */

public interface ChatComponentPacket {
    void sendMessage(Player player, ChatMessageType position, BaseComponent... components);
}
