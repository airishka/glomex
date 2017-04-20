package de.glomex.player.api.lifecycle;

import de.glomex.player.api.media.Content;
import de.glomex.player.api.media.MediaID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface ContentResolver {

    /**
     * Must either return content or throw an exception.
     */
    @NotNull Content resolve(@NotNull MediaID mediaID);

}
