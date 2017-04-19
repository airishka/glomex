package de.glomex.player.api;

import de.glomex.player.api.etc.EtcControl;
import de.glomex.player.api.events.SubscribeControl;
import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playlist.PlaylistControl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface PlayerAPI {

    @NotNull EtcControl etcController();

    @NotNull SubscribeControl subscribeManager();

    @NotNull PlaylistControl playlistManager();

    @NotNull PlaybackControl playbackController();

}
