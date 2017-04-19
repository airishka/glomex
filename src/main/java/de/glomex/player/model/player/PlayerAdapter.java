package de.glomex.player.model.player;

import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.model.playback.PlaybackControllerAdapter;

/**
 * TODO:
 * Created by <b>me@olexxa.com</b>
 */
public abstract class PlayerAdapter extends PlaybackControllerAdapter implements PlaybackControl, MediaControl {

    protected boolean autoplay;

    protected boolean fullscreen;

    public PlaybackListener playbackListener;

}
