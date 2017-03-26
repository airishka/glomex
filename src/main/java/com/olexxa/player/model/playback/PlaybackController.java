package com.olexxa.player.model.playback;

import com.olexxa.player.api.Content;
import com.olexxa.player.api.PlaybackControl;
import com.olexxa.player.model.player.PlayerAdapter;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlaybackController { //implements PlaybackControl {

    PlayerAdapter player;

    boolean autoplay;

    Content current;
    boolean isPlaying;
    long position;

}
