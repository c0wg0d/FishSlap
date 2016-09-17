package com.sandlotminecraft.FishSlap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by Shawn on 9/16/2016.
 */
public class Fish implements Listener {
    public static ItemStack getRawFish() {
        ItemStack rawfish = new ItemStack(Material.RAW_FISH, 1);
        ItemMeta im = rawfish.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "A Stinky Fish");
        im.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Slimy, Stinky, Ick."));
        rawfish.setItemMeta(im);
        return rawfish;
    }
}
