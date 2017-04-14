package de.glomex.player.javafx;

import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.model.player.PlayerAdapter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXPlayer extends PlayerAdapter {

    private final Stage stage;
    private final MediaView mediaView;

    MediaPlayer player;

    public JavaFXPlayer() {
        // mock: mocked UI, remove
        this.stage = JavaFXPlayerFactory.stage;
        this.mediaView = JavaFXPlayerFactory.mediaView;
        autoplay = true;
        stage.setFullScreen(true);
    }

    @Override
    public void openMedia(MediaData mediaData) {
        // New player object must be created for each new media
        Media media = new Media(mediaData.url().toExternalForm());
        player = new MediaPlayer(media);
        player.setAutoPlay(autoplay);
        player.setOnReady(() -> {
            stage.sizeToScene();
            stage.centerOnScreen();
            if (autoplay)
                player.play();
        });
//        player.setOnPlaying(() -> playing = true);
//        player.setOnPaused(() -> playing = false);
//        player.setOnEndOfMedia(stage::shutdown);
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
    public void shutdown() {
        player.dispose();
        player = null;
    }

}
