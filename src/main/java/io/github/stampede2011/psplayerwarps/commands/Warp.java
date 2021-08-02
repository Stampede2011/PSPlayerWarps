package io.github.stampede2011.psplayerwarps.commands;

import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.config.WarpType;
import io.github.stampede2011.psplayerwarps.tasks.InventoryTask;
import io.github.stampede2011.psplayerwarps.tasks.TeleportTask;
import io.github.stampede2011.psplayerwarps.utils.Utilities;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class Warp implements CommandExecutor {
    private PSPlayerWarps plugin = PSPlayerWarps.getInstance();
    private Player player;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            this.player = ((Player) src).getPlayer().get();

            if (args.<String>getOne(Text.of("warp")).isPresent()) {
                String warpName = args.<String>getOne(Text.of("warp")).get();
                WarpType warp = plugin.warpManager.getWarp(warpName.toLowerCase());

                if (warp == null) {
                    player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_NOT_FOUND.replace("%warp%", warpName), true));
                    return CommandResult.success();
                }

                TeleportTask.teleport(player, warp, warpName);
            } else {
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_COMMAND_SYNTAX.replace("%usage%", "/pwarp warp <warp>"), true));
            }
        } else {
            src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_NOT_A_PLAYER, true));
        }


        return CommandResult.success();
    }


    public static CommandSpec build() {

        return CommandSpec.builder()
                .permission("psplayerwarps.command.base")
                .arguments(GenericArguments.optionalWeak(GenericArguments.string(Text.of("warp"))))
                .executor(new Warp())
                .build();
    }
}