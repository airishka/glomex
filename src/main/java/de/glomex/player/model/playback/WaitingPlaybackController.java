package de.glomex.player.model.playback;

import org.jetbrains.annotations.Nullable;

/**
 * This class stores state for button and position.
 * It used as temporary state keeper until rest is loaded
 *
 * Created by <b>me@olexxa.com</b>
 */
public class WaitingPlaybackController extends PlaybackControllerAdapter {

    private @Nullable Boolean shouldPlay;

    private @Nullable Long position;

    public @Nullable Boolean shouldPlay() {
        return shouldPlay;
    }

    public void shouldPlay(@Nullable Boolean shouldPlay) {
        this.shouldPlay = shouldPlay;
    }

    @Override
    public void play() {
        shouldPlay = Boolean.TRUE;
    }

    @Override
    public void pause() {
        shouldPlay = Boolean.FALSE;
    }

    @Override
    public void seek(long position) {
        this.position = position;
    }

    @Override
    public long getPosition() {
        return position == null? 0 : position;
    }

    @Override
    public boolean isPlaying() {
        return shouldPlay == null? false : shouldPlay;
    }

}
