package io.github.stampede2011.psplayerwarps.commands;

import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.tasks.InventoryTask;
import io.github.stampede2011.psplayerwarps.utils.Utilities;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

public class Open implements CommandExecutor {
    private PSPlayerWarps plugin = PSPlayerWarps.getInstance();
    private Player player;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            this.player = ((Player) src).getPlayer().get();

            try {
                InventoryTask.openWarps(player);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }

        } else {
            src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_NOT_A_PLAYER, true));
        }


        return CommandResult.success();
    }


    public static CommandSpec build() {

        return CommandSpec.builder()
                .permission("psplayerwarps.command.base")
                .executor(new Open())
                .build();
    }
}