package com.olexxa.player.api.playlist;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

/**
 * TODO: think: if playlist is immutable, then could be split to building and playing parts
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlaylistControl {

    void addContent(@Nullable URL... urls);

    void addContent(@Nullable Content... contents);

    void removeContent(@NotNull Content content);

    /**
     * Remove all items from playlist
     */
    void clear();

    /**
     * Shuffle the playlist.
     * Reset statuses (all items will be treated as non-played)
     * Doesn't change current item.
     * Doesn't clear history
     */
    void shuffle();

    /**
     * If true, next item will be selected randomly
     */
    void setRandom(boolean state);

    /**
     * If true, won't check if the next item has already been played
     */
    void setRepeatable(boolean state);

    /**
     * Switch to the content passed
     */
    boolean skipTo(Content current);

    /**
     * Chooses next (random non played) item, accordingly to options.
     */
    boolean next();

    /**
     * Choose previously played content, if history contains it.
     * Otherwise chooses previous play list item
     */
    boolean prev();

}
