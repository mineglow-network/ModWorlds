/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.modwiz.modworlds;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.GameMode;

/**
 *
 * @author Starbuck
 */
public class Configuration {
    Map<String, WorldInfo> worldList = new HashMap<String, WorldInfo>();
    
    private ModWorlds plugin;
    
    public Configuration(ModWorlds plugin) {
        this.plugin = plugin;
    }
    
    
    public WorldInfo getWorldInfo(String worldName) {
        WorldInfo worldInfo = worldList.get(worldName);
        if (worldInfo == null) {
            worldList.put(worldName, new WorldInfo(plugin.getServer().getWorld(worldName), GameMode.SURVIVAL));
        }
        worldInfo = worldList.get(worldName);
        return worldInfo;
    }
    
}
