package net.pl3x.bukkit.chatapi;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.07.18 20:25
 *
 */

public class ComponentSender {

    public static void sendMessage(Player player, BaseComponent component) {
        sendMessage(player, ChatMessageType.CHAT, new BaseComponent[]{component});
    }
    public static void sendMessage(Player player, BaseComponent... components) {
        sendMessage(player, ChatMessageType.CHAT, components);
    }
    public static void sendMessage(Player player, ChatMessageType position, BaseComponent component) {
        sendMessage(player, position, new BaseComponent[]{component});
    }
    public static void sendMessage(Player player, ChatMessageType position, BaseComponent... components) {
        if(player == null) return;
        ChatComponentPacket packet = getPacket();
        if(packet == null) return;
        packet.sendMessage(player, position, components);
    }
    private static ChatComponentPacket getPacket() {

        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        String path = ComponentSender.class.getPackage().getName() + ".nms." + version;

        try {
            final Class<?> clazz = Class.forName(path + ".ChatComponentPacketHandler");
            if (ChatComponentPacket.class.isAssignableFrom(clazz)) {
                return (ChatComponentPacket) clazz.getConstructor().newInstance();
            }
        } catch (Exception ignore) {
            Bukkit.getLogger().info("[ERROR] This plugin is not compatible with this server version (" + version + ").");
            Bukkit.getLogger().info("[ERROR] Could not send chat packet!");
            ignore.printStackTrace();
        }
        return null;
    }
}
