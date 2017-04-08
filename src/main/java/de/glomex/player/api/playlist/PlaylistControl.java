package de.glomex.player.api.playlist;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

/**
 * improve: if playlist is immutable, then could be split to building and playing parts
 *
 * improve: content can be placed many times
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlaylistControl {

    /**
     * Add items in the end of playlist.
     * Can be called during playing
     */
    void addContent(@Nullable URL... urls);

    /**
     * Add items in the end of playlist.
     * Can be called during playing
     */
    void addContent(@Nullable Content... contents);

    /**
     * Remove item from playlist.
     * Moves to next item if this item is currently played
     */
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
     * @throws java.lang.IllegalArgumentException when content passed is not in the list
     */
    boolean skipTo(@NotNull Content current) throws IllegalArgumentException;

    /**
     * Chooses next (random non played) item, accordingly to options.
     */
    boolean next();

    /**
     * Choose previously played content, if any.
     * Otherwise chooses previous playlist item
     */
    boolean prev();

}
