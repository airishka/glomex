package de.glomex.player.api.playback;

import de.glomex.player.api.events.Listener;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface PlaybackListener extends Listener {

    void onPlay();

    void onPause();

    void onSeek(double position);
}
