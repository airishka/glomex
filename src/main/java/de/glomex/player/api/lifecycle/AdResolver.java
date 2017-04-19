package de.glomex.player.api.lifecycle;

import de.glomex.player.api.media.Advertise;
import de.glomex.player.api.media.MediaID;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Responsible for returning ad list given content id
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface AdResolver {

    @NotNull List<Advertise> resolve(@NotNull MediaID mediaID);

}
