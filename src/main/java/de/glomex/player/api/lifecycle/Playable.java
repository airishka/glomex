package de.glomex.player.api.lifecycle;

import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface Playable {

    /**
     * Media ID
     */
    @NotNull MediaID id();

}
