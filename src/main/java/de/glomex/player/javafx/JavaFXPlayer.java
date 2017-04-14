package de.glomex.player.javafx;

import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playback.PlaybackListener;
import de.glomex.player.model.player.PlayerAdapter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXPlayer extends PlayerAdapter {

    private final Stage stage;
    private final MediaView mediaView;

    MediaPlayer player;
    private boolean playing;

    public JavaFXPlayer(PlaybackListener playbackListener, boolean autoplay, boolean fullscreen) {
        eventListener = playbackListener;
        this.autoplay = autoplay;
        this.fullscreen = fullscreen;

        // mock: mocked UI, remove
        this.stage = JavaFXPlayerFactory.stage;
        this.mediaView = JavaFXPlayerFactory.mediaView;
    }

    @Override
    public void openMedia(@NotNull MediaData mediaData) {
        // New player object must be created for each new media
        Media media = new Media(mediaData.url().toExternalForm());
        player = new MediaPlayer(media);
        player.setAutoPlay(autoplay);
        if (fullscreen)
            stage.setFullScreen(true);
        player.setOnReady(() -> {
            if (!fullscreen) {
                stage.sizeToScene();
                stage.centerOnScreen();
            }
            if (autoplay)
                player.play();
        });
        player.setOnPlaying(() -> playing = true);
        player.setOnPaused(() -> playing = false);
        player.setOnEndOfMedia(JavaFXPlayerFactory.playlistManager::next);
        mediaView.setMediaPlayer(player);
    }

    @Override
    public void play() {
        if (player != null) {
            eventListener.onPlay();
            player.play();
        }
    }

    @Override
    public void pause() {
        if (player != null) {
            eventListener.onPause();
            player.pause();
        }
    }

    @Override
    public void seek(long position) {
        if (player != null) {
            eventListener.onSeek(position);
            player.seek(new Duration(position));
        }
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
        player.dispose();
        player = null;
    }

}
