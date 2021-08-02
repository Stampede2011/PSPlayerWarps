package io.github.stampede2011.psplayerwarps.commands;

import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

public class Reload implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        PSPlayerWarps.getInstance().config.reload();
        PSPlayerWarps.getInstance().storage.reload();
        PSPlayerWarps.getLogger().info("PSPlayerWarps has been successfully reloaded!");
        src.sendMessage(Utilities.getMessage(PSPlayerWarps.getInstance().config.get().messages.RELOAD, true));

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("psplayerwarps.command.reload.base")
                .executor(new Reload())
                .build();
    }
}