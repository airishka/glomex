package de.glomex.player.model.player;

import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.model.api.GlomexPlayerFactory;
import de.glomex.player.model.api.Logging;
import de.glomex.player.model.playback.PlaybackControllerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * The playing flag is set on callback from real player response,
 * not after play/pause commands are called.
 *
 * Created by <b>me@olexxa.com</b>
 */
public abstract class MediaPlayerAdapter<UI_OUT> extends PlaybackControllerAdapter
    implements MediaPlayer<UI_OUT> {

    private static final Logger log = Logging.getLogger(MediaPlayerAdapter.class);

    protected final PlaybackListener playbackListener;

    private boolean playing;

    public MediaPlayerAdapter() {
        playbackListener = GlomexPlayerFactory.instance(PlaybackListener.class);
    }

    protected void onReady() {
        playbackListener.onReady();
    }

    protected void onFinished() {
        playbackListener.onFinished();
    }

    protected void onStalled() {
        log.finest("MediaPlayer: onStalled");
    }

    protected void onPlay() {
        playing = true;
        playbackListener.onPlay();
    }

    protected void onPause() {
        playing = false;
        playbackListener.onPause();
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

}
