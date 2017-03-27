package com.olexxa.player.model.api;

import com.olexxa.player.api.PlayerAPI;
import com.olexxa.player.api.playback.PlaybackControl;
import com.olexxa.player.api.playlist.Content;
import com.olexxa.player.api.playlist.PlaylistControl;
import com.olexxa.player.javafx.JavaFXPlayer;
import com.olexxa.player.model.lifecycle.LifecycleManager;
import com.olexxa.player.model.playlist.PlaylistManager;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlayerAPIImpl implements PlayerAPI {

    private ActionDispatcher dispatcher;
    private PlaylistManager playlistManager;
    private LifecycleManager lifecycleManager;

    public PlayerAPIImpl() {
        lifecycleManager = new LifecycleManager();
        playlistManager = new PlaylistManager(lifecycleManager);

        dispatcher = new ActionDispatcher() {
            // TODO: this is hack to run player until rest is implemented
            JavaFXPlayer player;
            public void play() {
                if (player == null) {
                    player = new JavaFXPlayer();
                    Content current = lifecycleManager.current();
                    player.openMedia(current);
                } else
                    player.play();
            }
            public void pause() {
                player.pause();
            }
        };
        dispatcher.playlistManager = playlistManager;
    }

    @Override
    public void requestFullScreen() {
        throw new IllegalStateException("FIXME: Not implemented");
    }

    @Override
    public void destroy(Runnable callback) {
        throw new IllegalStateException("FIXME: Not implemented");
    }

    @Override
    public PlaylistControl playlistManager() {
        return dispatcher;
    }

    @Override
    public PlaybackControl playbackController() {
        return dispatcher;
    }

}
