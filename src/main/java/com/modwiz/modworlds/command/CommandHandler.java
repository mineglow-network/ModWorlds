/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.modwiz.modworlds.command;

import com.modwiz.modperms.players.PermPlayer;
import com.modwiz.modworlds.ModWorlds;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Starbuck
 */
public class CommandHandler implements CommandExecutor{
    private ModWorlds modWorlds;
    
    public CommandHandler(ModWorlds plugin) {
        modWorlds = plugin;
    }
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            cs.sendMessage(ChatColor.DARK_AQUA + "Using " + modWorlds.getDescription().getName()
                    + " v" + modWorlds.getDescription().getVersion() 
                    + " by " + modWorlds.getDescription().getAuthors().get(0));
            cs.sendMessage(ChatColor.DARK_AQUA + "Use /mw help"); 
            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 3) {
                PermPlayer pp = modWorlds.getPlayerManager().getPlayer(cs);
                if (pp.hasPermission("modworlds.create") || pp.hasPermission("modworlds.*")) {
                    String worldName = args[1];
                    String worldTypeString = args[2];
                    WorldType worldType;
                    if (worldTypeString.equalsIgnoreCase("flat")) {
                        worldType = WorldType.FLAT;
                    } else if (worldTypeString.equalsIgnoreCase("normal")) {
                        worldType = WorldType.NORMAL;
                    } else if (worldTypeString.equalsIgnoreCase("1.1")) {
                        worldType = WorldType.VERSION_1_1;
                    } else {
                        cs.sendMessage(ChatColor.RED + "Incorrect world type.");
                        cs.sendMessage(ChatColor.RED + "Correct world types are flat, normal and 1.1");
                        return false;
                    }
                    
                    modWorlds.createWorld(worldName, worldType);
                    cs.sendMessage(ChatColor.DARK_AQUA + "World " + worldName + " created.");
                } else {
                    cs.sendMessage(ChatColor.RED + "Sorry insufficent permissions.");
                }
            } else {
                cs.sendMessage(ChatColor.RED + "Correct Usage /mw create <name> <type>.");
            }
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (args.length == 2) {
                World world = modWorlds.getServer().getWorld(args[1]);
                if (world == null) {
                    cs.sendMessage(ChatColor.RED + "Sorry that world does not exist.");
                } else {
                    if (cs instanceof Player) {
                       PermPlayer pp = modWorlds.getPlayerManager().getPlayer(cs);
                       if (pp.hasPermission("modworlds.*") || pp.hasPermission("modworlds.teleport.*") ||
                               pp.hasPermission("modworlds.teleport."+world.getName())) {
                           Player player = (Player) cs;
                           player.teleport(world.getSpawnLocation());
                           cs.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + world.getName() + ".");
                       } else {
                           cs.sendMessage(ChatColor.RED + "Sorry insufficent permissions.");
                       }
                    } else {
                        cs.sendMessage(ChatColor.RED + "Sorry only players can teleport.");
                    }
                }
            } else {
                cs.sendMessage(ChatColor.RED + "Incorrect Usage.");
                cs.sendMessage(ChatColor.RED + "Correct Usage. /mw tp <worldName>");
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 1) {
                cs.sendMessage(ChatColor.RED + "Incorrect Usage.");
                cs.sendMessage(ChatColor.RED + "Correct Usage. /mw info <worldName>");
            } else {
                PermPlayer pp = modWorlds.getPlayerManager().getPlayer(cs);
                if (pp.hasPermission("modworlds.*") || pp.hasPermission("modworlds.info")) {
                    World world = modWorlds.getServer().getWorld(args[1]);
                    if (world == null) {
                        cs.sendMessage(ChatColor.RED + "Sorry that world does not exist.");
                    }
                    cs.sendMessage(ChatColor.DARK_AQUA + "World Name: " + world.getName());
                    Location spawnLocation = world.getSpawnLocation();
                    cs.sendMessage(ChatColor.DARK_AQUA + "Spawn: " + spawnLocation.getX() + ", " + spawnLocation.getY() + ", " + spawnLocation.getZ());
                    int playerCount = 0;
                    for (Player p : modWorlds.getServer().getOnlinePlayers()) {
                        if (p.getWorld().equals(world)) {
                            playerCount += 1;
                        }
                    }
                    cs.sendMessage(ChatColor.DARK_AQUA + "Players In World: " + playerCount);
                } else {
                    cs.sendMessage(ChatColor.RED + "Sorry insufficent permissions.");
                }
            }
        } else if (args[0].equalsIgnoreCase("help")) {
            cs.sendMessage(ChatColor.DARK_AQUA + "/mw help - displays this help.");
            cs.sendMessage(ChatColor.DARK_AQUA + "/mw create <worldname> <worldType> - create a world.");
            cs.sendMessage(ChatColor.DARK_AQUA + "/mw tp <worldName> - teleports a player to a world.");
            cs.sendMessage(ChatColor.DARK_AQUA + "/mw info <worldName> - displays info about a world.");
            cs.sendMessage(ChatColor.DARK_AQUA + "/mw alias <worldAlias> or /mw alias <worldName> <aliasName> - sets the display name of a world.");
            cs.sendMessage(ChatColor.DARK_AQUA + "/mw list - List's all worlds.");
            cs.sendMessage(ChatColor.DARK_AQUA + "/mw mode <gamemode> or /mw mode <worldName> <gamemode> - Set default world gamemode.");
            cs.sendMessage(ChatColor.DARK_AQUA + "/mw load - loads configuration from disk.");
        } else if (args[0].equalsIgnoreCase("alias")) {
            if (args.length == 2) {
                if (cs instanceof Player) {
                    Player player = (Player) cs;
                    String worldName = player.getWorld().getName();
                    PermPlayer pp = modWorlds.getPlayerManager().getPlayer(player);
                    if (pp.hasPermission("modworlds.*") || pp.hasPermission("modworlds.alias")) {
                        modWorlds.getConfig().set("worlds." + worldName + ".displayName", args[1]);
                        modWorlds.saveConfig();
                        cs.sendMessage(ChatColor.DARK_AQUA + "Alias for world " + worldName + " has been set to " + args[1] + ".");
                    } else {
                        cs.sendMessage(ChatColor.RED + "Sorry insufficent permissions.");
                    }
                } else {
                    cs.sendMessage(ChatColor.RED + "Sorry only players can set world alias.");
                }
            } else if (args.length == 3) {

                String worldName = args[1];
                PermPlayer pp = modWorlds.getPlayerManager().getPlayer(cs);
                if (pp.hasPermission("modworlds.*") || pp.hasPermission("modworlds.alias")) {
                    modWorlds.getConfig().set("worlds." + worldName + ".displayName", args[2]);
                    modWorlds.saveConfig();
                    cs.sendMessage(ChatColor.DARK_AQUA + "Alias for world " + worldName + " has been set to " + args[2] + ".");
                } else {
                    cs.sendMessage(ChatColor.RED + "Sorry insufficent permissions.");
                }

            } else {
                cs.sendMessage(ChatColor.RED + "Incorrect Usage.");
                cs.sendMessage(ChatColor.RED + "Correct Usage. /mw alias <aliasName> or /mw alias <worldName> <aliasName>");
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            PermPlayer pp = modWorlds.getPlayerManager().getPlayer(cs);
            boolean showRealName = false;
            
            if (pp.hasPermission("modworlds.*") || pp.hasPermission("modworlds.list.realName") 
                    || pp.hasPermission("modworlds.list.*")) {
                showRealName = true;
            }
            for (World w : modWorlds.getServer().getWorlds()) {
                String formalString = "";
                String worldName = modWorlds.getConfig().getString("worlds." + w.getName() + ".displayName",w.getName());
                formalString += worldName;
                if (showRealName == true) {
                    String realName = w.getName();
                    formalString += " (" + realName + ")";
                }
                cs.sendMessage(ChatColor.AQUA + formalString);
            }
        } else if (args[0].equalsIgnoreCase("load")) {
            PermPlayer pp = modWorlds.getPlayerManager().getPlayer(cs);
            if (pp.hasPermission("modworlds.*") || pp.hasPermission("modworlds.load")) {
                modWorlds.reloadConfig();
                modWorlds.loadSettings();
                cs.sendMessage(ChatColor.DARK_AQUA + "Config has been loaded from disk.");
                
            } else {
                cs.sendMessage(ChatColor.RED + "Sorry insufficent permissions.");
            }
        } else if (args[0].equalsIgnoreCase("mode")) {
            PermPlayer pp = modWorlds.getPlayerManager().getPlayer(cs);
            if (args.length == 2) {
                if (cs instanceof Player) {
                    Player p = (Player) cs;
                    String worldName = p.getWorld().getName();
                    FileConfiguration config = modWorlds.getConfig();
                    GameMode gamemode;
                    if (args[1].equalsIgnoreCase("1")) {
                        gamemode= GameMode.CREATIVE;
                    } else if (args[1].equalsIgnoreCase("2")) {
                        gamemode = GameMode.ADVENTURE;
                    } else if (args[1].equalsIgnoreCase("0")) {
                        gamemode = GameMode.SURVIVAL;
                    } else {
                        cs.sendMessage(ChatColor.RED + "Sorry gamemode must be of type 0, 1 or 2.");
                        return false;
                    }
                    config.set("worlds." + worldName + ".defaultMode", gamemode.name());
                    
                    cs.sendMessage(ChatColor.DARK_AQUA + "Default mode for world " + worldName + " has been set to " + gamemode.name().toLowerCase() +".");
                } else {
                    cs.sendMessage(ChatColor.RED + "Sorry only players may directly set default gamemode.");
                }
            } else if (args.length == 3) {
                World w = modWorlds.getServer().getWorld(args[1]);
                if (w == null) {
                    cs.sendMessage(ChatColor.RED + "Sorry that world doesn't exist.");
                    return false;
                }
                String worldName = w.getName();
                FileConfiguration config = modWorlds.getConfig();
                GameMode gamemode;
                if (args[2].equalsIgnoreCase("1")) {
                    gamemode = GameMode.CREATIVE;
                } else if (args[2].equalsIgnoreCase("2")) {
                    gamemode = GameMode.ADVENTURE;
                } else if (args[2].equalsIgnoreCase("0")) {
                    gamemode = GameMode.SURVIVAL;
                } else {
                    cs.sendMessage(ChatColor.RED + "Sorry gamemode must be of type 0, 1 or 2.");
                    return false;
                }
                config.set("worlds." + worldName + ".defaultMode", gamemode.name());
                cs.sendMessage(ChatColor.DARK_AQUA + "Default mode for world " + worldName + " has been set to " + gamemode.name().toLowerCase() + ".");

            } else {
                cs.sendMessage(ChatColor.RED + "Incorrect Usage.");
                cs.sendMessage(ChatColor.RED + "Correct usage. /mw mode <gamemode> or /mw mode <worldname> <gamemode>.");
            }
        } else {
            cs.sendMessage(ChatColor.RED + "Unknown Command. Use /mw help for help.");
        }
        return false;
    }
    
}
