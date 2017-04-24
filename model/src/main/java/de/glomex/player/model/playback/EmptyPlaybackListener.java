package de.glomex.player.model.playback;

import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.model.InternalTag;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public abstract class EmptyPlaybackListener implements PlaybackListener, InternalTag {

    @Override
    public void onReady() {}

    @Override
    public void onPlay() {}

    @Override
    public void onPause() {}

    @Override
    public void onSeek(long position) {}

    @Override
    public void onError(@NotNull String message) {}

    @Override
    public void onFinished() {}

}
