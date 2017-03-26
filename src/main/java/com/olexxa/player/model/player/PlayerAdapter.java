package com.olexxa.player.model.player;

import com.olexxa.player.api.PlaybackControl;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class PlayerAdapter implements PlaybackControl, MediaControl {

    protected boolean autoplay;

    protected boolean canPushTicks;

}
