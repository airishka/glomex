package de.glomex.player.model.playback;

import de.glomex.player.api.playback.PlaybackControl;
import org.jetbrains.annotations.Nullable;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlaybackController implements PlaybackControl {

    private @Nullable Boolean shouldPlay;

    public void shouldPlay(Boolean state) {
        this.shouldPlay = state;
    }

    public Boolean shouldPlay() {
        return shouldPlay;
    }

    public void shutdown() {
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
    }

    @Override
    public long getPosition() {
        return 0;
    }

}
