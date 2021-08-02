package io.github.stampede2011.psplayerwarps.commands;

import com.flowpowered.math.vector.Vector3d;
import io.github.stampede2011.psplayerwarps.PSPlayerWarps;
import io.github.stampede2011.psplayerwarps.utils.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class Set implements CommandExecutor {
    EventContext eventContext = EventContext.builder().add(EventContextKeys.PLUGIN, PSPlayerWarps.getContainer()).build();
    Optional<EconomyService> econ = Sponge.getServiceManager().provide(EconomyService.class);
    private PSPlayerWarps plugin = PSPlayerWarps.getInstance();
    private Player player;
    private String warpName;

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.getOne(Text.of("warp")).isPresent()) {
            this.warpName = (String) args.getOne(Text.of("warp")).get();
            if (src instanceof Player) {
                this.player = ((Player) src).getPlayer().get();

                if (plugin.warpManager.getWarp(warpName.toLowerCase()) != null) {
                    player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_EXISTS.replace("%warp%", warpName), true));
                    return CommandResult.success();
                }

                if (plugin.config.get().settings.restrictions.worldWhitelist.contains(player.getLocation().getExtent().getName())) {

                    int maxHomes = 0;

                    for (Map.Entry<String, Integer> entry : plugin.config.get().settings.restrictions.maxWarps.entrySet()) {
                        if (player.hasPermission("psplayerwarps.command.set.maxwarps." + entry.getKey())) {
                            if (maxHomes < entry.getValue()) {
                                maxHomes = entry.getValue();
                            }
                        }
                    }

                    if (plugin.warpManager.getWarpCount(player) < maxHomes) {
                        if (!warpName.chars().allMatch(Character::isLetter)) {
                            player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_NAME_INVALID.replace("%warp%", warpName), true));
                            return CommandResult.success();
                        }
                        if (warpName.length() > plugin.config.get().settings.restrictions.maxNameLength) {
                            player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_NAME_LENGTH.replace("%warp%", warpName).replace("%max%", String.valueOf(plugin.config.get().settings.restrictions.maxNameLength)), true));
                            return CommandResult.success();
                        }
                        if (plugin.config.get().settings.restrictions.nameBlacklist.contains(warpName.toLowerCase())) {
                            if (!player.hasPermission("psplayerwarps.command.set.bypass")) {
                                player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_NAME_BLACKLISTED.replace("%warp%", warpName), true));
                                return CommandResult.success();
                            }
                        }

                        Location<World> location = player.getLocation();
                        Vector3d rotation = player.getRotation();

                        Text confirmation;

                        if (plugin.config.get().settings.economy.enabled && econ.isPresent()) {
                            Optional<UniqueAccount> playerAcc = econ.get().getOrCreateAccount(player.getUniqueId());
                            Currency defaultCur = econ.get().getDefaultCurrency();
                            double cost = plugin.config.get().settings.economy.createCost;
                            confirmation = Text.builder()
                                    .append(Utilities.getMessage(plugin.config.get().messages.WARP_SET_CONFIRMATION_COST.replace("%warp%", warpName).replace("%cost%", String.valueOf(cost)), true))
                                    .onClick(TextActions.executeCallback(commandSource -> {
                                        if (commandSource instanceof Player) {
                                            if (playerAcc.get().getBalance(defaultCur).doubleValue() >= cost) {
                                                playerAcc.get().withdraw(defaultCur, BigDecimal.valueOf(cost), Cause.of(eventContext, PSPlayerWarps.getContainer()));
                                                warpConfirm(location, rotation);
                                            } else {
                                                player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_SET_MONEY.replace("%cost%", String.valueOf(cost)).replace("%currency%", defaultCur.getName()), true));
                                            }
                                        }
                                    }))
                                    .build();
                        } else {
                            confirmation = Text.builder()
                                    .append(Utilities.getMessage(plugin.config.get().messages.WARP_SET_CONFIRMATION.replace("%warp%", warpName), true))
                                    .onClick(TextActions.executeCallback(commandSource -> {
                                        if (commandSource instanceof Player) {
                                            warpConfirm(location, rotation);
                                        }
                                    }))
                                    .build();
                        }

                        player.sendMessage(confirmation);
                    } else {
                        player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_SET_MAXED.replace("%amount%", String.valueOf(plugin.warpManager.getWarpCount(player))).replace("%max%", String.valueOf(maxHomes)), true));
                    }
                } else {
                    player.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_WARP_WORLD, true));
                }
            } else {
                src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_NOT_A_PLAYER, true));
            }
        } else {
            src.sendMessage(Utilities.getMessage(plugin.config.get().messages.ERROR_COMMAND_SYNTAX.replace("%usage%", "/pwarp set <warp>"), true));
        }

        return CommandResult.success();
    }

    private void warpConfirm(Location<World> location, Vector3d rotation) {
        plugin.warpManager.createWarp(warpName, this.player, location, rotation);
        player.sendMessage(Utilities.getMessage(plugin.config.get().messages.WARP_SET_SUCCESS.replace("%warp%", warpName), true));
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .permission("psplayerwarps.command.set.base")
                .arguments(GenericArguments.optionalWeak(GenericArguments.string(Text.of("warp"))))
                .executor(new Set())
                .build();
    }

}
