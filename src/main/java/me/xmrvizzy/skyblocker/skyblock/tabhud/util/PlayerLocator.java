package me.xmrvizzy.skyblocker.skyblock.tabhud.util;

import java.util.List;

import me.xmrvizzy.skyblocker.mixin.PlayerListHudAccessor;
import me.xmrvizzy.skyblocker.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

public class PlayerLocator {

    public static enum Location {
        DUNGEON,
        GUEST_ISLAND,
        HOME_ISLAND,
        CRIMSON_ISLE,
        DUNGEON_HUB,
        FARMING_ISLAND,
        PARK,
        DWARVEN_MINES,
        CRYSTAL_HOLLOWS,
        END,
        GOLD_MINE,
        DEEP_CAVERNS,
        HUB,
        SPIDER_DEN,
        JERRY,
        GARDEN,
        UNKNOWN,
        NONE
    }

    public static Location getPlayerLocation() {

        if (!Utils.isOnSkyblock) {
            return Location.UNKNOWN;
        }

        List<PlayerListEntry> ple = MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream()
                .sorted(PlayerListHudAccessor.getOrdering()).toList();

        String cat2Name = StrMan.strAt(ple, 40);

        if (cat2Name.contains("Dungeon Stats")) {
            return Location.DUNGEON;
        }

        String areaDesciptor = StrMan.strAt(ple, 41).substring(6);
        switch (areaDesciptor) {
            case "Private Island":
                if (ple.get(44).getDisplayName().getString().endsWith("Guest")) {
                    return Location.GUEST_ISLAND;
                } else {
                    return Location.HOME_ISLAND;
                }
            case "Crimson Isle":
                return Location.CRIMSON_ISLE;
            case "Dungeon Hub":
                return Location.DUNGEON_HUB;
            case "The Farming Islands":
                return Location.FARMING_ISLAND;
            case "The Park":
                return Location.PARK;
            case "Dwarven Mines":
                return Location.DWARVEN_MINES;
            case "Crystal Hollows":
                return Location.CRYSTAL_HOLLOWS;
            case "The End":
                return Location.END;
            case "Gold Mine":
                return Location.GOLD_MINE;
            case "Deep Caverns":
                return Location.DEEP_CAVERNS;
            case "Hub":
                return Location.HUB;
            case "Spider's Den":
                return Location.SPIDER_DEN;
            case "Jerry's Workshop":
                return Location.JERRY;
            case "Garden":
                return Location.GARDEN;
            default:
                return Location.NONE;
        }
    }
}
