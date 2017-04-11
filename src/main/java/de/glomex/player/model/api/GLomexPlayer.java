package de.glomex.player.model.api;

import com.google.inject.Provides;
import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.api.playlist.PlaylistListener;
import de.glomex.player.javafx.JavaFXPlayer;
import de.glomex.player.model.events.EventHandler;
import de.glomex.player.model.events.EventLogger;
import de.glomex.player.model.events.SubscribeManager;
import de.glomex.player.model.playlist.PlaylistManager;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class GlomexPlayer implements PlayerAPI {

    private EtcController etcController;
    private ActionDispatcher dispatcher;
    private PlaylistManager playlistManager;
    private SubscribeManager subscribeManager;
    private EventHandler eventHandler;

    public GlomexPlayer() {
        etcController = new EtcController();
        subscribeManager = new SubscribeManager();
        eventHandler = new EventHandler(subscribeManager);
        playlistManager = new PlaylistManager(eventHandler.listener(PlaylistListener.class));

        dispatcher = new ActionDispatcher();
        // mock: this is hack to run player until rest is implemented
        dispatcher.playbackController(new PlaybackControl() {
            JavaFXPlayer player;
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

    @Override
    public EtcControl etcController() {
        return etcController;
    }

    @Override @Provides
    public SubscribeControl subscribeManager() {
        return subscribeManager;
    }

    // no sense in action dispatcher - playlist manager is always the same
    @Override
    public PlaylistControl playlistManager() {
        return playlistManager;
    }

    @Override
    public PlaybackControl playbackController() {
        return dispatcher.playbackController();
    }

    @Provides
    protected EventHandler eventHandler() {
        return eventHandler;
    }

}
