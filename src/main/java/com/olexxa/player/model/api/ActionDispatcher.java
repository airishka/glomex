package com.olexxa.player.model.api;

import com.olexxa.player.api.Content;
import com.olexxa.player.api.PlaybackControl;
import com.olexxa.player.api.PlaylistControl;

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
    public void removeContent(int index) {
        playlistManager.removeContent(index);
    }

    @Override
    public void clear() {
        playlistManager.clear();
    }

    @Override
    public void skipTo(int index) {
        playlistManager.skipTo(index);
    }

    @Override
    public void next() {
        playlistManager.next();
    }

    @Override
    public void prev() {
        playlistManager.prev();
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
