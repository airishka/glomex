package de.glomex.player.model.api;

import de.glomex.player.api.PlayerAPI;
import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.javafx.JavaFXPlayer;
import de.glomex.player.model.events.EventHandler;
import de.glomex.player.model.events.EventLogger;
import de.glomex.player.model.events.SubscribeManager;
import de.glomex.player.model.playlist.PlaylistManager;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlayerAPIImpl implements PlayerAPI {

    private ActionDispatcher dispatcher;
    private PlaylistManager playlistManager;
    private EventHandler eventHandler;
    private SubscribeManager subscribeManager;

    public PlayerAPIImpl() {
        playlistManager = new PlaylistManager();
        subscribeManager = new SubscribeManager();

        // TODO: this is hack to run player until rest is implemented
        dispatcher = new ActionDispatcher();
        dispatcher.playbackController(new PlaybackControl() {
            JavaFXPlayer player;
            public void play() {
                if (player == null) {
                    player = new JavaFXPlayer();
                    player.eventListener = eventHandler.listener(PlaybackListener.class);
                    player.openMedia(playlistManager.currentContent);
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
        eventHandler = new EventHandler(subscribeManager);
    }

    public void addEventLogger(EventLogger logger) {
        eventHandler.addLogger(logger);
    }

    @Override
    public void requestFullScreen() {
        throw new IllegalStateException("FIXME: Not implemented");
    }

    @Override
    public SubscribeControl subscribeManager() {
        return subscribeManager;
    }

    @Override
    public void destroy(Runnable callback) {
        throw new IllegalStateException("FIXME: Not implemented");
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

}
