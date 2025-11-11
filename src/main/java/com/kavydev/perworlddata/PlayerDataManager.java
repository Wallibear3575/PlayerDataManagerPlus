package com.kavydev.perworlddata;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public final class PlayerDataManager extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("PerWorldPlayerDataManager v1.0.0 enabled!");
        startAutoScan();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        handlePlayerData(p);
    }

    private void handlePlayerData(Player p) {
        World world = p.getWorld();
        String worldName = world.getName();
        UUID uuid = p.getUniqueId();

        File playerDataFile = new File(worldName + "/playerdata/" + uuid + ".dat");
        if (!playerDataFile.exists()) {
            getLogger().info("Creating new playerdata for " + p.getName() + " in " + worldName);
            try {
                playerDataFile.getParentFile().mkdirs();
                playerDataFile.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            getLogger().info("Playerdata loaded for " + p.getName() + " in " + worldName);
        }
    }

    private void startAutoScan() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                handlePlayerData(p);
            }
        }, 0L, 1200L); // every 1 min
    }
}
