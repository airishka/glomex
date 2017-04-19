package de.glomex.player.api.lifecycle;

import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

/**
 * Responsible  for returning media info given media id
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface MediaData extends Playable {

    /**
     * URL of the media
     */
    URL url();

    /**
     * True if media is the live stream
     */
    boolean isStream();

    /**
     * Duration in milliseconds or null for stream
     */
    @Nullable Long duration();

}
