package de.glomex.player.api.playback;

import de.glomex.player.api.ListenerTag;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface PlaybackListener extends ListenerTag {

    void onPlay();

    void onPause();

    void onSeek(double position);

    void onFinished();
}
