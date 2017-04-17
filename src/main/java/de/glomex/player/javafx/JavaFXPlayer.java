package de.glomex.player.javafx;

import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.model.api.Logging;
import de.glomex.player.model.player.PlayerAdapter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Features: support markers.
 *
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXPlayer extends PlayerAdapter {

    private static final Logger log = Logging.getLogger(JavaFXPlayer.class);

    private final Stage stage;
    private final MediaView mediaView;

    MediaPlayer player;
    private boolean playing;

    public JavaFXPlayer(PlaybackListener playbackListener, boolean autoplay, boolean fullscreen) {
        eventListener = playbackListener;
        this.autoplay = autoplay;
        this.fullscreen = fullscreen;

        // mock: mocked UI, remove
        this.stage = JavaFXPlayerAPIFactory.stage;
        this.mediaView = JavaFXPlayerAPIFactory.mediaView;
    }

    @Override
    public void openMedia(@NotNull MediaData mediaData) {
        // New player object must be created for each new media
        JavaFXUtils.ensureFxThread(() -> {
            Media media = new Media(mediaData.url().toExternalForm());
            player = new MediaPlayer(media);
            player.setAutoPlay(autoplay);
            player.setOnReady(() -> {
                stage.sizeToScene();
                stage.centerOnScreen();
                if (fullscreen)
                    stage.setFullScreen(true);
                if (autoplay)
                    play();
            });
            player.setOnPlaying(() -> playing = true);
            player.setOnPaused(() -> playing = false);
            player.setOnStalled(() -> log.finest("buffering")); // mock: for debug
            // mock: fixme!!!! must call upper component, not the PL manager
            player.setOnEndOfMedia(JavaFXPlayerAPIFactory.playlistManager::next);
            mediaView.setMediaPlayer(player);
        });
    }

    @Override
    public void play() {
        if (player != null)
            JavaFXUtils.ensureFxThread( () -> {
                eventListener.onPlay();
                player.play();
            });
    }

    @Override
    public void pause() {
        if (player != null)
            JavaFXUtils.ensureFxThread( () -> {
                eventListener.onPause();
                player.pause();
            });
    }

    @Override
    public void seek(long position) {
        if (player != null)
            JavaFXUtils.ensureFxThread( () -> {
                eventListener.onSeek(position);
                player.seek(new Duration(position));
            });
    }

    @Override
    public long getPosition() {
        return player != null? Math.round(player.getCurrentTime().toMillis()) : 0;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void shutdown() {
        if (player != null)
            JavaFXUtils.ensureFxThread( () -> {
                player.dispose();
                player = null;
            });
    }

}
