package io.github.stampede2011.psplayerwarps.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.util.List;
import java.util.Map;

@ConfigSerializable
public class MainConfig {

    @Setting
    public Messages messages = new Messages();

    @ConfigSerializable
    public static class Messages {

        @Setting
        public String PREFIX = "&8(&b&lPlayer Warps&8)";

        @Setting
        public String RELOAD = "&aPSPlayerWarps has been successfully reloaded!";

        @Setting
        public String WARP_TELEPORT_COUNTDOWN = "&aYou will be warped to &l%warp% &ain &l%time%&a!";

        @Setting
        public String WARP_TELEPORT_SUCCESS = "&aYou have successfully been teleported to &l%warp%&a!";

        @Setting
        public String WARP_SET_CONFIRMATION_COST = "&aAre you sure you want to create the warp &l%warp%&a for &l$%cost%&a? &lClick to confirm";

        @Setting
        public String WARP_SET_CONFIRMATION = "&aAre you sure you want to create the warp &l%warp%&a? &lClick to confirm";

        @Setting
        public String WARP_REMOVE_CONFIRMATION = "&aAre you sure you want to remove the warp &l%warp%&a? &lClick to confirm";

        @Setting
        public String LIST_SUCCESS = "&aYour player warps: &l%warps%&a!";

        @Setting
        public String WARP_SET_SUCCESS = "&aYou have successfully set the warp &l%warp%&a!";

        @Setting
        public String WARP_REMOVE_SUCCESS = "&aYou have successfully removed the warp &l%warp%&a!";

        @Setting
        public String LIKE_SUCCESS = "&aYou have successfully liked the warp &l%warp%&a!";

        @Setting
        public String ICON_SUCCESS = "&aYou have successfully set the icon for the warp &l%warp%&a!";

        @Setting
        public String DESCRIPTION_SUCCESS = "&aYou have successfully set the description for the warp &l%warp%&a!";

        @Setting
        public String ERROR_DESCRIPTION_INVALID = "&cThat description contains the invalid character &l%char%&c!";

        @Setting
        public String ERROR_COMMAND_SYNTAX = "&cIncorrect command syntax! Correct usage: &l%usage%";

        @Setting
        public String ERROR_NOT_A_PLAYER = "&cYou are not a player!";

        @Setting
        public String ERROR_WARP_NOT_FOUND = "&cThe warp &l%warp% &cis not a valid warp!";

        @Setting
        public String ERROR_WARP_EXISTS = "&cThe warp &l%warp% &chas already been set!";

        @Setting
        public String ERROR_SET_MAXED = "&cYou have reached your maximum amount of &l%amount%/%max% warp(s) &cfor your rank!";

        @Setting
        public String ERROR_SET_MONEY = "&cYou do not have at least $%cost% %currency%s to set this warp!";

        @Setting
        public String ERROR_SET_TRUSTED = "&cYou are not trusted on this claim!";

        @Setting
        public String ERROR_OWNERSHIP = "&cYou do not own this warp!";

        @Setting
        public String ERROR_WARP_NAME_LENGTH = "&cThe warp &l%warp% &cis larger than the &l%max% &ccharacter limit!";

        @Setting
        public String ERROR_DESCRIPTION_LENGTH = "&cThat description is larger than the &l%max% &ccharacter limit!";

        @Setting
        public String ERROR_WARP_NAME_INVALID = "&cThe warp &l%warp% &ccontains invalid characters!";

        @Setting
        public String ERROR_WARP_WORLD = "&cPlayer Warps are not allowed in this world!";

        @Setting
        public String ERROR_WARP_NAME_BLACKLISTED = "&cThe warp &l%warp% &cis a blacklisted name!";

        @Setting
        public String ERROR_LIKE_OWNER = "&cYou cannot like this warp because you own it!";

        @Setting
        public String ERROR_LIKE_ALREADY = "&cYou have already liked the warp &l%warp%&c!";

        @Setting
        public String ERROR_LIST_NONE = "&cYou don't have any player warps set!";

        @Setting
        public String ERROR_ICON_NO_PERMS = "&cYou don't have permission to use this icon!";

    }

    @Setting
    public Settings settings = new Settings();

    @ConfigSerializable
    public static class Settings {

        @Setting
        public Cooldowns cooldowns = new Cooldowns();

        @ConfigSerializable
        public static class Cooldowns {

            @Setting(value="create-cooldown")
            public int createCooldown = 60;

            @Setting(value="teleport-cooldown")
            public int teleportCooldown = 10;

            @Setting(value="teleport-warmup")
            public int teleportWarmup = 5;

        }

        @Setting
        public Economy economy = new Economy();

        @ConfigSerializable
        public static class Economy {

            @Setting(value="create-cost")
            public double createCost = 100;

            @Setting
            public boolean enabled = true;

        }

        @Setting
        public Map<String, Icon> icons = ImmutableMap.of("playerhead", new Icon());

        @ConfigSerializable
        public static class Icon {

            @Setting
            public ItemType id = ItemTypes.SKULL;

            @Setting
            public int unsafeDamage = 0;

            @Setting
            public String nbt = "";

            @Setting
            public List<String> lore = Lists.newArrayList(
                    "Example Lore 1",
                    "Example Lore 2"
            );

            @Setting
            public String name = "Player Head";

            @Setting
            public String permission = "psplayerwarps.icon.playerhead";

            @Setting
            public int position = 1;

        }

        @Setting
        public Restrictions restrictions = new Restrictions();

        @ConfigSerializable
        public static class Restrictions {

            @Setting(value="max-name-length")
            public int maxNameLength = 30;

            @Setting(value="max-description-length")
            public int maxDescLength = 150;

            @Setting(value="name-blacklist")
            public List<String> nameBlacklist = Lists.newArrayList(
                    "warp",
                    "help",
                    "set",
                    "remove",
                    "list",
                    "open",
                    "like",
                    "desc",
                    "icon",
                    "reload"
            );

            @Setting(value="max-warps")
            public Map<String, Integer> maxWarps = ImmutableMap.of("default", 1);

            @Setting(value="world-whitelist")
            public List<String> worldWhitelist = Lists.newArrayList(
                    "world",
                    "DIM1",
                    "DIM-1"
            );

        }

    }

}

