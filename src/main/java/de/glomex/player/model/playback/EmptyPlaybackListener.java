package de.glomex.player.model.playback;

import de.glomex.player.api.playback.PlaybackListener;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class EmptyPlaybackListener implements PlaybackListener {

    @Override
    public void onPlay() {}

    @Override
    public void onPause() {}

    @Override
    public void onSeek(double position) {}

    @Override
    public void onFinished() {}

}
