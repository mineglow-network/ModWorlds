/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.modwiz.modworlds;

import com.modwiz.modperms.players.PermPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Starbuck
 */
public class EventListener implements Listener{
    private ModWorlds plugin;
    
    public EventListener(ModWorlds plugin) {
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) {
            PermPlayer pp = plugin.getPlayerManager().getPlayer(event.getPlayer());
            if (pp.hasPermission("modworlds.*") || pp.hasPermission("modworlds.teleport.*")
                    || pp.hasPermission("modworlds.teleport." + event.getTo().getWorld().getName())) {
                String worldName = event.getTo().getWorld().getName();
                GameMode mode = GameMode.valueOf(plugin.getConfig().getString("worlds." + worldName + ".defaultMode", GameMode.SURVIVAL.name()));
                player.setGameMode(mode);
                plugin.getConfig().set("worlds." + worldName + ".defaultMode", mode.name());
                plugin.saveConfig();
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Sorry insufficent permissions.");
            }
        }
    }
}
