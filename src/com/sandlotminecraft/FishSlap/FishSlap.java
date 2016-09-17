package com.sandlotminecraft.FishSlap;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Shawn on 9/15/2016.
 */
public class FishSlap extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Listeners(), this);

        this.getCommand("points").setExecutor(new PointsCommand());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
    }

    public static HashMap<UUID, HashMap<UUID, Long>> cooldowns = new HashMap<>();
    public static HashMap<UUID, Integer> points = new HashMap<>();
}
