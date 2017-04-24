package de.glomex.player.model.playback;

import de.glomex.player.api.playback.PlaybackControl;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class PlaybackControllerAdapter implements PlaybackControl {

    public void shutdown() {}

    @Override
    public void play() {}

    @Override
    public void pause() {}

    @Override
    public void seek(long position) {}

    @Override
    public long getPosition() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

}
