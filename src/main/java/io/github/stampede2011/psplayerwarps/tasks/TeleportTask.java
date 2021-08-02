package io.github.stampede2011.psplayerwarps.tasks;

import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.config.WarpType;
import io.github.stampede2011.psplayerwarps.utils.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TeleportTask {
    private static PSPlayerWarps plugin = PSPlayerWarps.getInstance();

    public static void teleport(Player player, WarpType warp, String warpName) {
        int warmupTime = (player.hasPermission("psplayerwarps.command.warp.bypass")) ? 0 : plugin.config.get().settings.cooldowns.teleportWarmup;

        if (warmupTime > 0)
            player.sendMessage(Utilities.getMessage(plugin.config.get().messages.WARP_TELEPORT_COUNTDOWN.replace("%warp%", warpName).replace("%time%", warmupTime + " seconds"), true));

        Scheduler scheduler = Sponge.getScheduler();
        Task.Builder taskBuilder = scheduler.createTaskBuilder();
        taskBuilder
                .execute(new Consumer<Task>() {
                    @Override
                    public void accept(Task t) {
                        t.cancel();
                        player.setLocation(plugin.warpManager.getWarpLocation(warp));
                        player.setRotation(plugin.warpManager.getRotation(warp));
                        player.sendMessage(Utilities.getMessage(plugin.config.get().messages.WARP_TELEPORT_SUCCESS.replace("%warp%", warpName), true));
                    }
                })
                .delay(warmupTime, TimeUnit.SECONDS)
                .name("PSPlayerWarps Teleport Warmup").submit(plugin);
    }

}
