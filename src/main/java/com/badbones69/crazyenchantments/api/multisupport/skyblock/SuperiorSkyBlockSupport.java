package com.badbones69.crazyenchantments.api.multisupport.skyblock;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.entity.Player;

public class SuperiorSkyBlockSupport {

    public static boolean inTerritory(Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return superiorPlayer.isInsideIsland();
    }

    public static boolean isFriendly(Player player, Player other) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player), otherPlayer = SuperiorSkyblockAPI.getPlayer(other);
        return superiorPlayer.getIsland() != null && superiorPlayer.getIsland().isMember(otherPlayer);
    }
}