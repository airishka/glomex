package de.glomex.player.model.api;

import com.google.inject.Provides;
import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.javafx.JavaFXPlayer;
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
    private final ActionDispatcher dispatcher;
    private final EventHandler eventHandler;

    public GlomexPlayer() {
        executionManager = new ExecutionManager();
        dispatcher = new ActionDispatcher();
        subscribeManager = new SubscribeManager();
        eventHandler = new EventHandler(subscribeManager, executionManager);
        etcController = new EtcController(this);
        playlistManager = new PlaylistManager(eventHandler.playlistListener());

        mockPlayer(); // mock: remove it
    }

    @Override @Provides
    public @NotNull EtcControl etcController() {
        return etcController;
    }

    @Override @Provides
    public @NotNull SubscribeControl subscribeManager() {
        return subscribeManager;
    }

    @Override @Provides
    public @NotNull PlaylistControl playlistManager() {
        return playlistManager; // no sense in action dispatcher - playlist manager is always the same
    }

    @Override @Provides
    public @NotNull PlaybackControl playbackController() {
        return dispatcher.playbackController();
    }

    @Provides
    public @NotNull EventHandler eventHandler() {
        return eventHandler;
    }

    @Provides
    public @NotNull ExecutionManager executionManager() {
        return executionManager;
    }

    void shutdown() {
        log.entering("Glomex PLayer", "Shutdown");
        executionManager.shutdown();
        player.shutdown(); // mock: remove
    }

    // mock: remove it
    private JavaFXPlayer player;

    // mock: remove it
    void mockPlayer() {
        dispatcher.playbackController(new PlaybackControl() {
            //            JavaFXPlayer player;
            public void play() {
                if (player == null) {
                    player = new JavaFXPlayer();
                    player.eventListener = eventHandler.listener(PlaybackListener.class);
                    MediaData media = etcController.mediaResolver().resolve(playlistManager.currentContent());
                    player.openMedia(media);
                } else
                    player.play();
            }
            public void pause() {
                player.pause();
            }
            public void seek(double position) {
                player.seek(position);
            }
            public double getPosition() {
                return player.getPosition();
            }
        });
    }

    // mock: remove it
    public void addEventLogger(EventLogger logger) {
        eventHandler.addLogger(logger);
    }

}
