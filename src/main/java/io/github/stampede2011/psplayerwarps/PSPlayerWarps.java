package io.github.stampede2011.psplayerwarps;

import com.google.inject.Inject;
import io.github.eufranio.config.Config;
import io.github.stampede2011.psplayerwarps.commands.Base;
import io.github.stampede2011.psplayerwarps.config.MainConfig;
import io.github.stampede2011.psplayerwarps.config.Storage;
import io.github.stampede2011.psplayerwarps.managers.WarpManager;
import me.rojo8399.placeholderapi.PlaceholderService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import com.griefdefender.api.claim.ClaimType;

import java.io.File;

@Plugin(id = PSPlayerWarps.ID,
        name = PSPlayerWarps.NAME,
        authors = PSPlayerWarps.AUTHORS,
        description = PSPlayerWarps.DESCRIPTION,
        version = PSPlayerWarps.VERSION,
        dependencies = {
            @Dependency(id = "placeholderapi", optional = true),
            @Dependency(id = "griefdefender")
        })

public class PSPlayerWarps {

    public static final String ID = "psplayerwarps";
    public static final String NAME = "PSPlayerWarps";
    public static final String AUTHORS = "Stampede2011";
    public static final String DESCRIPTION = "Player Warps system for the PokeSkies Network!";
    public static final String VERSION = "1.12.2-1.0.0";

    @Inject
    private org.slf4j.Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File dir;

    public Config<MainConfig> config;
    public Config<Storage> storage;
    public WarpManager warpManager = new WarpManager(this);
//    private GriefDefenderValidator gdValid;

    @Inject
    private PluginContainer container;

    private static PSPlayerWarps instance;
    private PlaceholderService placeholder;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        this.config = new Config<>(MainConfig.class, "Config.conf", dir);
        this.storage = new Config<>(Storage.class, "Storage.conf", dir);


        logger.info("PSPlayerWarps has been enabled!");

        Sponge.getCommandManager().register(instance, Base.build(), "playerwarps", "playerwarps", "pwarp", "pwarps", "pw", "pws");
    }

    @Listener
    public void onReload(GameReloadEvent e) {
        this.config.reload();
        this.storage.reload();
    }

    @Listener
    public void onGameInit(GameInitializationEvent e) {
        instance = this;

        this.placeholder = (PlaceholderService)Sponge.getServiceManager().provideUnchecked(PlaceholderService.class);
    }

//    @Listener
//    public void onPreInitialization(GamePreInitializationEvent e) {
//        if (Sponge.getPluginManager().getPlugin("griefdefender").isPresent()) {
//            Utilities.broadcast(Utilities.toText("# 1"));
//            gdValid = new GriefDefenderValidator();
//            Utilities.broadcast(Utilities.toText("# 2"));
//        }
//    }

    public static PSPlayerWarps getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return instance.logger;
    }

    public static PluginContainer getContainer() {
        return instance.container;
    }

    public static PlaceholderService getPH() { return instance.placeholder; }

//    public static GriefDefenderValidator getGDValid() { return instance.gdValid; }

}
