/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.modwiz.modworlds;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;

/**
 *
 * @author Starbuck
 */
public class WorldInfo {
    private WorldType type;
    private String worldName;
    private GameMode defaultMode;
    private String displayName;
    private ChatColor displayColor;
    
    public WorldInfo(World world, GameMode mode) {
        type = world.getWorldType();
        worldName = world.getName();
        defaultMode = mode;
        displayName = worldName;
        displayColor = ChatColor.WHITE;
    }
    
    public String getColoredName() {
        return displayColor + displayName;
    }
    
    public WorldType getType() {
        return type;
    }
    
    public GameMode getGamemode() {
        return defaultMode;
    }
    
    public void setGamemode(GameMode mode) {
        this.defaultMode = mode;
    }
    
    public void setAlias(String alias) {
        this.displayName = alias;
    }
    
    public void setColor(ChatColor color) {
        displayColor = color;
    }
}
