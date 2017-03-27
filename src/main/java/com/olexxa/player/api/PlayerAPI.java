package com.olexxa.player.api;

import com.olexxa.player.api.playback.PlaybackControl;
import com.olexxa.player.api.playlist.PlaylistControl;

/**
 * TODO: add events
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlayerAPI {

    void requestFullScreen();

    PlaylistControl playlistManager();

    PlaybackControl playbackController();

    void destroy(Runnable callback);

}
