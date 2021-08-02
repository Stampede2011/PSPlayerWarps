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

import java.util.UUID;

public class Like implements CommandExecutor {
    private PSPlayerWarps plugin = PSPlayerWarps.getInstance();
    private String warpName;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.getOne(Text.of("warp")).isPresent()) {
            this.warpName = (String) args.getOne(Text.of("warp")).get();
            WarpType warp = plugin.warpManager.getWarp(warpName.toLowerCase());

            if (src instanceof Player) {

                if (warp == null) {
                    src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_NOT_FOUND.replace("%warp%", warpName), true));
                    return CommandResult.success();
                }

                Player player = ((Player) src).getPlayer().get();
                UUID playerUUID = player.getUniqueId();
                if (warp.owner.equals(playerUUID)) {
                    player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_LIKE_OWNER, true));
                } else if (warp.likes.contains(playerUUID)){
                    player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_LIKE_ALREADY.replace("%warp%", warpName), true));
                } else {
                    warp.likes.add(playerUUID);
                    plugin.storage.save();
                    player.sendMessage(Utilities.getMessage(plugin.config.get().messages.LIKE_SUCCESS.replace("%warp%", warpName), true));
                }

            } else {
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_NOT_A_PLAYER, true));
            }

        } else {
            src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_COMMAND_SYNTAX.replace("%usage%", "/pwarp like <warp>"), true));
        }


        return CommandResult.success();
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .permission("psplayerwarps.command.like.base")
                .arguments(GenericArguments.optionalWeak(GenericArguments.string(Text.of("warp"))))
                .executor(new Like())
                .build();
    }

}