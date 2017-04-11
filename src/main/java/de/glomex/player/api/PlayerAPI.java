package de.glomex.player.api;

import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playlist.PlaylistControl;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlayerAPI {

    EtcControl etcController();

    SubscribeControl subscribeManager();

    PlaylistControl playlistManager();

    PlaybackControl playbackController();

}
