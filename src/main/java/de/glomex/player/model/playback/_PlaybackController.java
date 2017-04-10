package de.glomex.player.model.playback;

import de.glomex.player.api.playlist.Content;
import de.glomex.player.model.player.PlayerAdapter;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class _PlaybackController { //implements PlaybackControl {

    PlayerAdapter player;

    boolean autoplay;

    Content current;
    boolean isPlaying;
    long position;


    public void stop() {

    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play(Content content) {

    }
}
