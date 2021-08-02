package io.github.stampede2011.psplayerwarps.commands;

import io.github.stampede2011.psplayerwarps.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;

public class Help implements CommandExecutor {
    private Player player;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        PaginationList.builder()
                .title(Utilities.toText("&b&lPlayer Warps"))
                .padding(Utilities.toText("&8&m-&r"))
                .contents(
                        Utilities.toText("&b/pwarp &8- &7Opens the warp listing menu"),
                        Utilities.toText("&b/pwarp help &8- &7Lists all available commands"),
                        Utilities.toText("&b/pwarp <warp> &8- &7Teleport to the specified warp"),
                        Utilities.toText("&b/pwarp set <warp> &8- &7Set a warp at your location"),
                        Utilities.toText("&b/pwarp remove <warp> &8- &7Removes the specified warp"),
                        Utilities.toText("&b/pwarp list &8- &7List all of your active warps"),
                        Utilities.toText("&b/pwarp open &8- &7Opens the warp listing menu"),
                        Utilities.toText("&b/pwarp like <warp> &8- &7Leaves a like on the specified warp"),
                        Utilities.toText("&b/pwarp desc <warp> <desc> &8- &7Sets the description of the specified warp"),
                        Utilities.toText("&b/pwarp icon <warp> &8- &7Change the icon of the specified warp")
                )
                .linesPerPage(15)
                .sendTo(src);

        return CommandResult.success();
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .permission("psplayerwarps.command.base")
                .executor(new Help())
                .build();
    }
}