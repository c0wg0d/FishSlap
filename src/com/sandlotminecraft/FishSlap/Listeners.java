package com.sandlotminecraft.FishSlap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

import java.security.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Shawn on 9/16/2016.
 */
public class Listeners implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (p.getInventory().contains(Fish.getRawFish()))
            return;

        p.getInventory().addItem(Fish.getRawFish());
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().hasItemMeta()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.AQUA + "You can't drop your fish!");
        }
    }

    @EventHandler
    public void onEatFish(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.RAW_FISH) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onUseFish(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.PLAYER) || !event.getEntity().getType().equals(EntityType.PLAYER))
            return;

        // Cancel the event so players don't actually take damage
        event.setDamage(0);
        //event.setCancelled(true);

        Player p = (Player) event.getDamager();
        Player t = (Player) event.getEntity();

        // Check if the player is using a fish
        if (!p.getInventory().getItemInMainHand().getType().equals(Material.RAW_FISH)) {
            event.setCancelled(true);
            return;
        }

        // Check if the cooldown is expired and send a message if it isn't
        if (FishSlap.cooldowns.containsKey(p.getUniqueId()) && FishSlap.cooldowns.get(p.getUniqueId()).containsKey(t.getUniqueId())) {
            long lastSlapTime = FishSlap.cooldowns.get(p.getUniqueId()).get(t.getUniqueId());

            if (System.currentTimeMillis() - lastSlapTime < 6000) {
                p.sendMessage(ChatColor.AQUA + "You cannot fish slap " + t.getName() + " for another " + (6000 - (System.currentTimeMillis() - lastSlapTime))/1000 + " seconds.");
                event.setCancelled(true);
                return;
            }
        }

        // Fish Slap the target, send them a message, set a cooldown, and log a point
        p.sendMessage(ChatColor.AQUA + "You have fish slapped " + t.getName() + "!");
        t.sendMessage(ChatColor.RED + "You've been fish slapped by " + p.getName() + "!");

        //pull the player's current target cooldowns and save a new one for the current target
        UUID slapper = p.getUniqueId();
        UUID slappee = t.getUniqueId();

        HashMap<UUID, Long> playerCooldowns = new HashMap<>();
        if (FishSlap.cooldowns.containsKey(slapper)) {
            playerCooldowns = FishSlap.cooldowns.get(slapper);
        }

        playerCooldowns.put(slappee, System.currentTimeMillis());
        FishSlap.cooldowns.put(slapper, playerCooldowns);


        // log a point
        if (FishSlap.points.containsKey(p.getUniqueId()))
            FishSlap.points.put(p.getUniqueId(), FishSlap.points.get(p.getUniqueId()) + 1);
        else
            FishSlap.points.put(p.getUniqueId(), 1);

        // Check if the player took the lead in points or has reached a point milestone
        Integer playerScore = FishSlap.points.get(p.getUniqueId());

            //Find the player with the highest points that is not the current player
            Map.Entry<UUID, Integer> topScore = null;

            for (Map.Entry<UUID, Integer> entry : FishSlap.points.entrySet()) {
                if (topScore == null || entry.getValue().compareTo(topScore.getValue()) > 0)
                    if (entry.getKey() != p.getUniqueId())
                        topScore = entry;
            }

            // This player has the first point record. Tally it and exit
            if (topScore == null) {
                return;
            }

            // Check if the current player has the highest points
            if (playerScore < topScore.getValue()) {
                // this player does not have the highest points, so do nothing and stop processing
                return;
            }

            // Current player has top score, check if the current player has just overtaken the top scorer
            if (playerScore == topScore.getValue() + 1) {
                Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA + p.getName() + " has just taken the lead with " + playerScore + " points!");
                return;
            }
            // Check if the player's score is a multiple of 50 and announce
            else if (playerScore % 50 == 0) {
                Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA + p.getName() + " has reached " + playerScore + " points!");
                return;
            }


    }
}

