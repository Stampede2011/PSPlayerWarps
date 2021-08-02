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

public class Desc implements CommandExecutor {
    private PSPlayerWarps plugin = PSPlayerWarps.getInstance();
    private String warpName;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.getOne(Text.of("warp")).isPresent() && args.<String>getOne(Text.of("description")).isPresent()) {
            this.warpName = (String) args.getOne(Text.of("warp")).get();
            String description = args.<String>getOne(Text.of("description")).get();
            WarpType warp = plugin.warpManager.getWarp(warpName.toLowerCase());

            if (warp == null) {
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_NOT_FOUND.replace("%warp%", warpName), true));
                return CommandResult.success();
            }
            if (description.length() > plugin.config.get().settings.restrictions.maxDescLength) {
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_DESCRIPTION_LENGTH.replace("%max%", String.valueOf(plugin.config.get().settings.restrictions.maxDescLength)), true));
                return CommandResult.success();
            }
            if (description.contains("&")) {
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_DESCRIPTION_INVALID.replace("%char%", "&"), true));
                return CommandResult.success();
            }

            if (src instanceof Player) {
                Player player = ((Player) src).getPlayer().get();
                if (warp.owner.equals(player.getUniqueId()) || player.hasPermission("psplayerwarps.command.remove.bypass"))
                    setDescription(warp, description, player);
                else
                    player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_OWNERSHIP, true));
            } else {
                if (src.hasPermission("psplayerwarps.command.remove.bypass"))
                    setDescription(warp, description, src);
            }
        } else {
            src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_COMMAND_SYNTAX.replace("%usage%", "/pwarp desc <warp> <desc>"), true));
        }


        return CommandResult.success();
    }

    private void setDescription(WarpType warp, String description, CommandSource src) {
        warp.description = description;
        plugin.storage.save();
        src.sendMessage(Utilities.getMessage(plugin.config.get().messages.DESCRIPTION_SUCCESS.replace("%warp%", warpName), true));
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .permission("psplayerwarps.command.desc.base")
                .arguments(GenericArguments.optionalWeak(GenericArguments.string(Text.of("warp"))),
                        GenericArguments.optionalWeak(GenericArguments.remainingJoinedStrings(Text.of("description"))))
                .executor(new Desc())
                .build();
    }

}