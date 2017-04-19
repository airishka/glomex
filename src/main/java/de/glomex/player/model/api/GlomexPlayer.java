package de.glomex.player.model.api;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.model.events.EventHandler;
import de.glomex.player.model.events.EventLogger;
import de.glomex.player.model.events.SubscribeManager;
import de.glomex.player.model.playlist.PlaylistManager;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class GlomexPlayer implements PlayerAPI {

    private static final Logger log = Logging.getLogger(GlomexPlayer.class);

    private final EtcController etcController;
    private final PlaylistManager playlistManager;
    private final SubscribeManager subscribeManager;

    private final ExecutionManager executionManager;
    private final ActionDispatcher actionDispatcher;
    private final EventHandler eventHandler;

    public GlomexPlayer() {
        executionManager = new ExecutionManager();
        actionDispatcher = new ActionDispatcher();
        subscribeManager = new SubscribeManager();
        eventHandler = new EventHandler(subscribeManager, executionManager);
        etcController = new EtcController(this);
        playlistManager = new PlaylistManager(eventHandler.playlistListener());

        subscribeManager.registerListener(playlistManager);
    }

    public @NotNull ExecutionManager executionManager() {
        return executionManager;
    }

    public @NotNull ActionDispatcher actionDispatcher() { return actionDispatcher; }

    public @NotNull EventHandler eventHandler() {
        return eventHandler;
    }

    @Override
    public @NotNull EtcControl etcController() {
        return etcController;
    }

    @Override
    public @NotNull SubscribeControl subscribeManager() {
        return subscribeManager;
    }

    @Override
    public @NotNull PlaylistControl playlistManager() {
        return playlistManager; // no sense in action dispatcher - playlist manager is always the same
    }

    @Override
    public @NotNull PlaybackControl playbackController() {
        return actionDispatcher.playbackProxy();
    }

    void shutdown() {
        log.entering("Glomex Player", "shutdown");
        executionManager.shutdown();
        playlistManager.shutdown();
    }

    // todo; could be better
    public void addEventLogger(EventLogger logger) {
        eventHandler.addLogger(logger);
    }

}
