package com.olexxa.player.javafx;

import com.olexxa.player.api.playlist.Content;
import com.olexxa.player.model.player.PlayerAdapter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class JavaFXPlayer extends PlayerAdapter {

    private final Stage stage;
    private final MediaView mediaView;

//    boolean playing;
    MediaPlayer player;

    public JavaFXPlayer() {
        // FIXME
        this.stage = JavaFXPlayerAPIFactory.stage;
        this.mediaView = JavaFXPlayerAPIFactory.mediaView;
        autoplay = true;
    }

    @Override
    public void openMedia(Content content) {
        // New player object must be created for each new media
        Media media = new Media(content.url().toExternalForm());
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
        player.setOnEndOfMedia(stage::close);
        mediaView.setMediaPlayer(player);
    }

    @Override
    public void play() {
        if (player != null)
            player.play();
    }

    @Override
    public void pause() {
        if (player != null)
            player.pause();
    }

    @Override
    public void seek(double position) {
        if (player != null)
            player.seek(new Duration(position));
    }

    @Override
    public double getPosition() {
        return player != null? player.getCurrentTime().toMillis() : 0;
    }
}
