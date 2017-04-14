package de.glomex.player.api.playback;

import de.glomex.player.api.etc.ControlTag;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlaybackControl extends ControlTag {

    void play();

    void pause();

    void seek (long position);

    long getPosition();

    boolean isPlaying();

}
