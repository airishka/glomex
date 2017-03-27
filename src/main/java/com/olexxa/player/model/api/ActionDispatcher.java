package com.olexxa.player.model.api;

import com.olexxa.player.api.playlist.Content;
import com.olexxa.player.api.playback.PlaybackControl;
import com.olexxa.player.api.playlist.PlaylistControl;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class ActionDispatcher implements PlaylistControl, PlaybackControl {

    PlaylistControl playlistManager;

    PlaybackControl playbackController;

    @Override
    public void addContent(URL... urls) {
        playlistManager.addContent(urls);
    }

    @Override
    public void addContent(Content... contents) {
        playlistManager.addContent(contents);
    }

    @Override
    public void removeContent(@NotNull Content content) {
        playlistManager.removeContent(content);
    }

    @Override
    public void clear() {
        playlistManager.clear();
    }

    @Override
    public void shuffle() {
        playlistManager.shuffle();
    }

    @Override
    public void setRandom(boolean state) {
        playlistManager.setRandom(state);
    }

    @Override
    public void setRepeatable(boolean state) {
        playlistManager.setRepeatable(state);
    }

    @Override
    public boolean skipTo(Content content) {
        return playlistManager.skipTo(content);
    }

    @Override
    public boolean next() {
        return playlistManager.next();
    }

    @Override
    public boolean prev() {
        return playlistManager.prev();
    }

    @Override
    public void play() {
        playbackController.play();
    }

    @Override
    public void pause() {
        playbackController.pause();
    }

    @Override
    public void seek(double position) {
        playbackController.seek(position);
    }

    @Override
    public double getPosition() {
        return playbackController.getPosition();
    }
}
