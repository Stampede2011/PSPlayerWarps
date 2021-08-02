package io.github.stampede2011.psplayerwarps.config;

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;
import java.util.UUID;

@ConfigSerializable
public class WarpType {

    @Setting(value="display-name")
    public String displayName = "";

    @Setting
    public String description = "This is my player warp!";

    @Setting
    public UUID owner;

    @Setting
    public String icon = "playerhead";


    @Setting
    public List<UUID> likes = Lists.newArrayList();

    @Setting
    public Location location = new Location();

    @ConfigSerializable
    public static class Location {

        @Setting
        public String world = "world";

        @Setting
        public double x = 0.0;

        @Setting
        public double y = 100.0;

        @Setting
        public double z = 0.0;

        @Setting
        public double yaw = 0.0;

        @Setting
        public double pitch = 0.0;

    }

}
