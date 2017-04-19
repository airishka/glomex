package de.glomex.player.api.lifecycle;

import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface AdData extends Playable {

    /** URL to get ad metadata */
    URL url();

    /** Ad Placement */
    AdPosition position();

}
