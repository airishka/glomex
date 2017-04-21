package de.glomex.player.api.playback;

import de.glomex.player.api.ListenerTag;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface PlaybackListener extends ListenerTag {

    void onReady();

    void onPlay();

    void onPause();

    void onSeek(long position);

    void onError(@NotNull String message); // todo: throwable?

    void onFinished();
}
