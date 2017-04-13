package de.glomex.player.model.player;

import de.glomex.player.api.playback.PlaybackControl;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.model.events.EventHandler;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class PlayerAdapter implements PlaybackControl, MediaControl {

    protected boolean autoplay;

    protected boolean canPushTicks;

    public PlaybackListener eventListener;

    public abstract void shutdown();
}
