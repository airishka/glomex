package de.glomex.player.model.api;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.etc.ShutdownListener;
import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.model.events.EventHandler;
import de.glomex.player.model.events.EventLogger;
import de.glomex.player.model.events.SubscribeManager;
import de.glomex.player.model.player.MediaPlayerFactory;
import de.glomex.player.model.playlist.EmptyPlaylistListener;
import de.glomex.player.model.playlist.PlaylistManager;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class GlomexPlayer implements PlayerAPI {

    private static final Logger log = Logging.getLogger(GlomexPlayer.class);

    final EtcController etcController;
    final PlaylistManager playlistManager;
    final SubscribeManager subscribeManager;

    final ExecutionManager executionManager;
    final ActionDispatcher actionDispatcher;
    final EventHandler eventHandler;

    final MediaPlayerFactory mediaPlayerFactory;

    public GlomexPlayer() {
        mediaPlayerFactory = MediaPlayerFactory.get();
        executionManager = new ExecutionManager();
        actionDispatcher = new ActionDispatcher();
        subscribeManager = new SubscribeManager();
        eventHandler = new EventHandler(subscribeManager, executionManager);
        etcController = new EtcController(this);
        playlistManager = new PlaylistManager(eventHandler.playlistListener()); // raise playlist events
        subscribeManager.registerListener(playlistManager); // listen lifecycle events
        subscribeManager.registerListener(new EmptyPlaylistListener() {
            @Override
            public void onPlaylistFinished() {
                if (etcController.autoShutdown())
                    etcController.shutdown();
            }
        });
    }

    @Override
    public @NotNull EtcControl etcController() {
        return etcController;
    }

    @Override
    public @NotNull SubscribeControl subscribeManager() {
        return subscribeManager;
    }

    // no sense in action dispatcher - playlist manager is always the same
    @Override
    public @NotNull PlaylistControl playlistManager() {
        return playlistManager;
    }

    @Override
    public @NotNull PlaybackControl playbackController() {
        return actionDispatcher.playbackProxy();
    }

    void shutdown() {
        log.finest("Glomex Player shutdown");
        ShutdownListener shutdownListener = eventHandler.shutdownListener();
            shutdownListener.onShutdown();
        executionManager.shutdown();
        playlistManager.shutdown();
        subscribeManager.shutdown();
    }

    // todo; could be better
    public void addEventLogger(@NotNull EventLogger logger) {
        eventHandler.addLogger(logger);
    }

}
