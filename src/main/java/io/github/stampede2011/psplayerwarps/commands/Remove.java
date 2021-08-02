package io.github.stampede2011.psplayerwarps.commands;

import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.config.WarpType;
import io.github.stampede2011.psplayerwarps.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

public class Remove implements CommandExecutor {
    private PSPlayerWarps plugin = PSPlayerWarps.getInstance();
    private String warpName;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.getOne(Text.of("warp")).isPresent()) {
            this.warpName = (String) args.getOne(Text.of("warp")).get();
            WarpType warp = plugin.warpManager.getWarp(warpName.toLowerCase());

            if (warp == null) {
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_NOT_FOUND.replace("%warp%", warpName), true));
                return CommandResult.success();
            }

            Text confirmation = Text.builder()
                    .append(Utilities.getMessage(plugin.config.get().messages.WARP_REMOVE_CONFIRMATION.replace("%warp%", warpName), true))
                    .onClick(TextActions.executeCallback(commandSource -> {
                        if (commandSource instanceof Player) {
                            removeConfirm(src);
                        }
                    }))
                    .build();

            if (src instanceof Player) {
                Player player = ((Player) src).getPlayer().get();
                if (warp.owner.equals(player.getUniqueId()) || player.hasPermission("psplayerwarps.command.remove.bypass"))
                    player.sendMessage(confirmation);
                else
                    player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_OWNERSHIP, true));
            } else {
                if (src.hasPermission("psplayerwarps.command.remove.bypass"))
                    removeConfirm(src);
            }
        } else {
            src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_COMMAND_SYNTAX.replace("%usage%", "/pwarp remove <warp>"), true));
        }


        return CommandResult.success();
    }

    private void removeConfirm(CommandSource src) {
        plugin.warpManager.removeWarp(warpName.toLowerCase());
        src.sendMessage(Utilities.getMessage(plugin.config.get().messages.WARP_REMOVE_SUCCESS.replace("%warp%", warpName), true));
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .permission("psplayerwarps.command.remove.base")
                .arguments(GenericArguments.optionalWeak(GenericArguments.string(Text.of("warp"))))
                .executor(new Remove())
                .build();
    }

}
