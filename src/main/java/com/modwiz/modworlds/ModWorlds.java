package com.modwiz.modworlds;

import com.modwiz.modperms.ModPermsPlugin;
import com.modwiz.modperms.groups.GroupManager;
import com.modwiz.modperms.players.PlayerManager;
import com.modwiz.modworlds.command.CommandHandler;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class ModWorlds extends JavaPlugin
{
    private ModPermsPlugin plugin;
    private Map<String, WorldType> worldList = new HashMap<String, WorldType>();
    
    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("ModPerms") != null) {
            this.plugin = (ModPermsPlugin) getServer().getPluginManager().getPlugin("ModPerms");
        }
        
        this.getServer().getPluginCommand("worldmanager").setExecutor(new CommandHandler(this));
        if (!getConfig().contains("worlds")) {
            getConfig().createSection("worlds");
            saveConfig();
        }
        ConfigurationSection worldsNode = getConfig().getConfigurationSection("worlds");
        for (String worldName : worldsNode.getKeys(false)) {
            ConfigurationSection worldNode = worldsNode.getConfigurationSection(worldName);
            WorldType worldType = WorldType.getByName(worldNode.getString("type"));
            worldList.put(worldName, worldType);
        }
        
        for (World w : getServer().getWorlds()) {
            worldList.put(w.getName(), w.getWorldType());
        }
        
        loadWorlds();
    }
    
    @Override
    public void onDisable() {
        if (!getConfig().contains("worlds")) {
            getConfig().createSection("worlds");
        }
        ConfigurationSection worldsNode = getConfig().getConfigurationSection("worlds");
        for (Map.Entry<String, WorldType> world : worldList.entrySet()) {
            if (!worldsNode.contains(world.getKey())) {
                worldsNode.createSection(world.getKey());
            }
            ConfigurationSection worldNode = worldsNode.getConfigurationSection(world.getKey());
            worldNode.set("type", world.getValue().getName());
        }
        saveConfig();
    }
    private void loadWorlds() {
        for (Map.Entry<String,WorldType> world : worldList.entrySet()) {
            getServer().createWorld(new WorldCreator(world.getKey()).type(world.getValue()));
        }
    }
    
    public PlayerManager getPlayerManager() {
        return plugin.getPlayerManager();
    }
    
    public GroupManager getGroupManager() {
        return plugin.getGroupManager();
    }
    
    public void createWorld(String worldName, WorldType type) {
        getServer().createWorld(new WorldCreator(worldName).type(type));
        worldList.put(worldName, type);
    }
}
