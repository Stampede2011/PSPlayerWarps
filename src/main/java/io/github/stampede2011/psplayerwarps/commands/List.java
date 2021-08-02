package io.github.stampede2011.psplayerwarps.commands;

import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.config.WarpType;
import io.github.stampede2011.psplayerwarps.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;

public class List implements CommandExecutor {
    private PSPlayerWarps plugin = PSPlayerWarps.getInstance();
    private Player player;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = ((Player) src).getPlayer().get();
            StringBuilder warpList = new StringBuilder();

            for (Map.Entry<String, WarpType> entry : plugin.storage.get().warps.entrySet()) {
                if (entry.getValue().owner.equals(player.getUniqueId())) {
                    if (warpList.length() > 0) {
                        warpList.append(", ");
                    }
                    warpList.append(entry.getValue().displayName);
                }
            }

            if (warpList.length() > 0)
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.LIST_SUCCESS.replace("%warps%", warpList), true));
            else
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_LIST_NONE, true));

        } else {
            src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_NOT_A_PLAYER, true));
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .permission("psplayerwarps.command.list.base")
                .executor(new List())
                .build();
    }
}