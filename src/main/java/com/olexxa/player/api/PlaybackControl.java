package com.olexxa.player.api;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlaybackControl {

    void play();

    void pause();

    void seek (double position);

    double getPosition();

}
