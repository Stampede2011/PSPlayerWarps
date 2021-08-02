//package io.github.stampede2011.psplayerwarps.utils;
//import com.griefdefender.api.Core;
//import com.griefdefender.api.GriefDefender;
//import com.griefdefender.api.claim.Claim;
//import org.spongepowered.api.Sponge;
//import org.spongepowered.api.entity.living.player.Player;
//import org.spongepowered.api.world.Location;
//import org.spongepowered.api.world.World;
//
//public class GriefDefenderValidator {
//
//    private final Core griefDefender;
//
//    public GriefDefenderValidator() {
//        Utilities.broadcast(Utilities.toText("## 1"));
//        griefDefender = GriefDefender.getCore();
//        Utilities.broadcast(Utilities.toText("## 2"));
//    }
//
//    public boolean isTrusted(Location<World> location, Player player) {
//        Utilities.broadcast(Utilities.toText("### 1"));
//        Claim claim = griefDefender.getClaimManager(location.getExtent().getUniqueId()).getClaimAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
//        Utilities.broadcast(Utilities.toText("### 2"));
//        return claim.isTrusted(player.getUniqueId());
//    }
//
//}
