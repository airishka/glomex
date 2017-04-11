package de.glomex.player.api.lifecycle;

import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Responsible for returning ad list given media id
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface AdResolver {

    @NotNull List<AdData> resolve(@NotNull MediaID mediaID);

}
