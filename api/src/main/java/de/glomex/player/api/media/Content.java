package de.glomex.player.api.media;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

/**
 * Responsible  for returning content info given content id
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface Content extends Media {

    /**
     * URL of the content
     */
    @NotNull URL url();

    /**
     * True if content is the live stream
     */
    boolean isStream();

    /**
     * Duration in milliseconds or null for stream
     */
    @Nullable Long duration();

}
