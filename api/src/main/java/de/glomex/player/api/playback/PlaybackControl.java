package de.glomex.player.api.playback;

import de.glomex.player.api.ControlTag;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface PlaybackControl extends ControlTag {

    void play();

    void pause();

    void seek (long position);

    long getPosition();

    boolean isPlaying();

}
