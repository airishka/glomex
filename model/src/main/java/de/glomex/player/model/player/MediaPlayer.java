package de.glomex.player.model.player;

import de.glomex.player.api.playback.PlaybackControl;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface MediaPlayer<UI_OUT> extends PlaybackControl {

    UI_OUT component();

    void shutdown();

}
