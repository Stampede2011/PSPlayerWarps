package io.github.stampede2011.psplayerwarps.managers;

import com.flowpowered.math.vector.Vector3d;
import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.config.WarpType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Map;
import java.util.UUID;

public class WarpManager {


    PSPlayerWarps plugin;
    public WarpManager(PSPlayerWarps plugin) {
        this.plugin = plugin;
    }

    public WarpType getWarp(String name) {
        return plugin.storage.get().warps.get(name);
    }

    public void createWarp(String name, Player player, Location<World> location,  Vector3d rotation) {
        WarpType warp = new WarpType();
        warp.displayName = name;
        warp.owner = player.getUniqueId();
        warp.location.world = location.getExtent().getName();
        warp.location.x = location.getBlockX();
        warp.location.y = location.getBlockY();
        warp.location.z = location.getBlockZ();
        warp.location.yaw = rotation.getX();
        warp.location.pitch = rotation.getY();

        plugin.storage.get().warps.putIfAbsent(name.toLowerCase(), warp);
        plugin.storage.save();
    }

    public void removeWarp(String warpName) {
        plugin.storage.get().warps.remove(warpName);
        plugin.storage.save();
    }

    public Location<World> getWarpLocation(WarpType warp) {

        World world = Sponge.getServer().getWorld(warp.location.world).get();

       return new Location(world, warp.location.x, warp.location.y, warp.location.z);

    }
    public Vector3d getRotation(WarpType warp) {

        return new Vector3d(warp.location.yaw, warp.location.pitch, 0);

    }

    public int getWarpCount(Player player) {

        UUID uuid = player.getUniqueId();
        int warpCount = 0;

        for (Map.Entry<String, WarpType> entry : plugin.storage.get().warps.entrySet()) {
            if (entry.getValue().owner.equals(uuid)) {
                warpCount++;
            }
        }

        return warpCount;
    }

}
