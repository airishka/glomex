package de.glomex.player.api.lifecycle;

import de.glomex.player.api.lifecycle.MediaData;
import de.glomex.player.api.playlist.MediaID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface MediaResolver {

    @Nullable
    MediaData resolve(@NotNull MediaID mediaID);

}
