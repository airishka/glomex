package com.olexxa.player.model.playlist;

import com.olexxa.player.api.PlaylistControl;
import com.olexxa.player.api.Content;
import com.olexxa.player.model.api.ContentImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class PlaylistManager implements PlaylistControl {

    private List<Content> playlist = new ArrayList<>();
    private int current;

    @Override
    public void addContent(URL... urls) {
        Stream.of(urls)
            .map(url -> (Content) new ContentImpl(UUID.randomUUID(), url))
            .forEach(playlist::add);
    }

    @Override
    public void addContent(Content... contents) {
        Stream.of(contents)
            .forEach(playlist::add);
    }

    @Override
    public void removeContent(int index) {
        playlist.remove(index);
    }

    @Override
    public void clear() {
        playlist.clear();
    }

    @Override
    public void skipTo(int index) {
        current = index;
    }

    @Override
    public void next() {
        throw new IllegalStateException("FIXME: implement");
    }

    @Override
    public void prev() {
        throw new IllegalStateException("FIXME: implement");
    }

    public Content current() {
        return playlist.get(current);
    }

}
