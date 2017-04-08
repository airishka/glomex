package de.glomex.player.api;

import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playlist.PlaylistControl;
import de.glomex.player.api.events.SubscribeControl;

/**
 * TODO: add events
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlayerAPI {

    void requestFullScreen();

    SubscribeControl subscribeManager();

    PlaylistControl playlistManager();

    PlaybackControl playbackController();

    void destroy(Runnable callback);

}
