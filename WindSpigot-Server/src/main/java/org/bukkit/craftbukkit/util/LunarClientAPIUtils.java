//package org.bukkit.craftbukkit.util;
//
//import com.lunarclient.bukkitapi.LunarClientAPI;
//import com.lunarclient.bukkitapi.cooldown.LCCooldown;
//import com.lunarclient.bukkitapi.cooldown.LunarClientAPICooldown;
//import com.lunarclient.bukkitapi.nethandler.LCPacket;
//import com.lunarclient.bukkitapi.nethandler.client.LCPacketTitle;
//import com.lunarclient.bukkitapi.title.TitleType;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//
//public class LunarClientAPIUtils {
//  public static void sendTitle(Player player, String title) {
//    sendTitle(player, title, 0L, 0L, 0L);
//  }
//
//  public static void sendTitle(Player player, String title, long displayTimeMs, long fadeInTimeMs, long fadeOutTimeMs) {
//    LunarClientAPI lunarClientAPI = LunarClientAPI.getInstance();
//    lunarClientAPI.sendPacket(player, new LCPacketTitle(TitleType.TITLE.name(), title, displayTimeMs, fadeInTimeMs, fadeOutTimeMs));
//  }
//
//  public static void sendSubTitle(Player player, String title) {
//    sendSubTitle(player, title, 0L, 0L, 0L);
//  }
//
//  public static void sendSubTitle(Player player, String title, long displayTimeMs, long fadeInTimeMs, long fadeOutTimeMs) {
//    LunarClientAPI lunarClientAPI = LunarClientAPI.getInstance();
//    lunarClientAPI.sendPacket(player, (LCPacket)new LCPacketTitle(TitleType.SUBTITLE.name(), title, displayTimeMs, fadeInTimeMs, fadeOutTimeMs));
//  }
//
//  public static void clearTitle(Player player) {
//    LunarClientAPI lunarClientAPI = LunarClientAPI.getInstance();
//    if (lunarClientAPI.isRunningLunarClient(player)) {
//      lunarClientAPI.sendPacket(player, (LCPacket)new LCPacketTitle(TitleType.TITLE.name(), "", 0L, 0L, 0L));
//      lunarClientAPI.sendPacket(player, (LCPacket)new LCPacketTitle(TitleType.SUBTITLE.name(), "", 0L, 0L, 0L));
//    } else {
//      player.resetTitle();
//    }
//  }
//
//  public static void registerCoolDown(String name, long mills, Material itemId) {
//    LunarClientAPICooldown.registerCooldown(new LCCooldown(name, mills, itemId));
//  }
//
//  public static void sendCoolDown(Player player, String name) {
//    LunarClientAPICooldown.sendCooldown(player, name);
//  }
//}
