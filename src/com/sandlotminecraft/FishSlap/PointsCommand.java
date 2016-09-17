package com.sandlotminecraft.FishSlap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Shawn on 9/16/2016.
 */
public class PointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg2, String[] arg3) {

        // Declare variables
        Player p = (Player) sender;
        Map.Entry<UUID, Integer>[] topScores = new Map.Entry[5];

        Map<UUID, Integer> scores = new ConcurrentHashMap<>(FishSlap.points);

        for (Integer i=0;i<=4;i++) {

            topScores[i] = null;
            for (Map.Entry<UUID, Integer> entry : scores.entrySet()) {
                if (topScores[i] == null || entry.getValue().compareTo(topScores[i].getValue()) > 0) {
                    topScores[i] = entry;
                    scores.remove(entry.getKey());
                }
            }
        }

        // Send the message text
        p.sendMessage("Top Players");
        p.sendMessage("======================");

        Boolean inTopFive = false;

        for (Integer i=0;i<=4;i++){
            if (topScores[i] != null){
                p.sendMessage(String.format("%-16s %5d", Bukkit.getOfflinePlayer(topScores[i].getKey()).getName(), topScores[i].getValue()));
                if (topScores[i].getKey().equals(p.getUniqueId()))
                    inTopFive = true;
            }
        }

        if (!inTopFive) {
            if (!scores.containsKey(p.getUniqueId()))
                p.sendMessage(String.format("%-16s %5d", p.getName(), 0));
            else
                p.sendMessage(String.format("%-16s %5d", p.getName(), scores.get(p.getUniqueId())));
        }

        return true;
    }
}
