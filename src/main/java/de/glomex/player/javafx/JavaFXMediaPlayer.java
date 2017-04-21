package de.glomex.player.javafx;

import de.glomex.player.model.api.Logging;
import de.glomex.player.model.player.MediaPlayerAdapter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXMediaPlayer extends MediaPlayerAdapter<MediaPlayer> {

    private static final Logger log = Logging.getLogger(JavaFXMediaPlayer.class);

    protected @NotNull MediaPlayer fxPlayer;

    public JavaFXMediaPlayer(@NotNull de.glomex.player.api.media.Media media) {
        Media fxMedia = new Media(media.url().toExternalForm());
        fxPlayer = new MediaPlayer(fxMedia);
        fxPlayer.setOnReady(this::onReady);
        fxPlayer.setOnPlaying(this::onPlay);
        fxPlayer.setOnPaused(this::onPause);
        fxPlayer.setOnStalled(this::onStalled);
        fxPlayer.setOnEndOfMedia(this::onFinished);
    }

    @Override
    public @Nullable MediaPlayer component() {
        return fxPlayer;
    }

    @Override
    public void play() {
        JavaFXUtils.ensureFxThread(fxPlayer::play);
    }

    @Override
    public void pause() {
        JavaFXUtils.ensureFxThread(fxPlayer::pause);
    }

    @Override
    public void seek(long position) {
        JavaFXUtils.ensureFxThread( () -> {
            fxPlayer.seek(new Duration(position));
            playbackListener.onSeek(position);
        });
    }

    @Override
    public long getPosition() {
        return Math.round(fxPlayer.getCurrentTime().toMillis());
    }

    @Override
    public void shutdown() {
        JavaFXUtils.ensureFxThread(fxPlayer::dispose);
    }

}
