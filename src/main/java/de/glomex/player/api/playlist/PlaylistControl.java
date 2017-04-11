package de.glomex.player.api.playlist;

import de.glomex.player.api.etc.ControlTag;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * improve: if playlist is immutable, then could be split to building and playing parts
 *
 * improve: content can be placed many times
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlaylistControl extends ControlTag {

    /**
     * Add items in the end of playlist.
     * Can be called during playing
     */
    void addContent(@NotNull MediaID... mediaIDs);

    /**
     * Remove item from playlist.
     * Moves to next item if this item is currently played
     */
    void removeContent(@NotNull MediaID mediaID);

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
     * It there is several media with the same id added, first found will be played.
     *
     * @throws java.lang.IllegalArgumentException when content passed is not in the list
     */
    boolean skipTo(@NotNull MediaID current) throws IllegalArgumentException;

    /**
     * Play item  with index passed
     */
    boolean skipTo(int  index);

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
