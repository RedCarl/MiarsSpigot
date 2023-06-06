package org.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MiarsUtils {
    /**
     * 获取服务器版本信息
     *
     * @param player 玩家
     */
    public static void getVersion(CommandSender player) {
        player.sendMessage(ColorParserBukkit.parse("&r"));
        player.sendMessage(ColorParserBukkit.parse("&3&lMiarsSystem&8(forked Bukkit) &7服务端相关信息"));
        player.sendMessage(ColorParserBukkit.parse("&r &b| &7运行版本 &bgit-MiarsSystem-1.8.8 (MC: 1.8.8)"));
        player.sendMessage(ColorParserBukkit.parse("&r &b| &7接口版本 &b") + Bukkit.getBukkitVersion());
        player.sendMessage(ColorParserBukkit.parse("&r &b| &f相关服务端更新请查询开发者内部构建服务器。"));
        player.sendMessage(ColorParserBukkit.parse("&r &b| &f相关插件状态查询请参考 &b/Plugins &f指令。"));
    }

    /**
     * 获取服务器插件列表
     *
     * @param sender 玩家
     */
    public static void getPlugins(CommandSender sender) {
        Plugin[] pluginsOriginal = Bukkit.getPluginManager().getPlugins();
        List<Plugin> pluginsRely = new ArrayList<>();
        StringBuilder pluginName = new StringBuilder();
        int hidePluginCount = 0;
        for (Plugin plugin : pluginsOriginal) {
            String s = plugin.getName().toLowerCase(Locale.ROOT);
            if (sender instanceof Player && !s.contains("miars") && (
                    s.contains("lib")
                            || s.contains("api")
                            || s.contains("world")
                            || s.contains("spark")
                            || s.contains("citizen")
                            || s.contains("vault")
                            || s.contains("lore")
                            || s.contains("perm")
            )) {
                ++hidePluginCount;
                continue;
            }
            if (isPrivatePlugin(plugin.getDescription())) {
                pluginName.append("&b").append(plugin.getName()).append("&fⒸ&8, ");
            } else {
                pluginsRely.add(plugin);
            }
        }
        for (Plugin plugin : pluginsRely) {
            pluginName.append("&3").append(plugin.getName()).append("&7, ");
        }
        sender.sendMessage(ColorParserBukkit.parse("&7您好,本服有 &f" + (pluginsOriginal.length - hidePluginCount) + "&7 插件 &b(标有&fⒸ&b为原创)&7:"));
        sender.sendMessage(ColorParserBukkit.parse(pluginName.substring(0, pluginName.length() - 2)));
    }


    public static boolean isPrivatePlugin(PluginDescriptionFile desc) {
        if (desc.getAuthors().stream().map(e -> e.toLowerCase(Locale.ROOT)).anyMatch(author -> author.contains("red_carl"))) {
            return true;
        }
        if (desc.getMain().toLowerCase(Locale.ROOT).contains("miars")) {
            return true;
        }
        return false;
    }
}
