package de.glomex.player.api.playlist;

import de.glomex.player.api.ListenerTag;
import de.glomex.player.api.media.MediaID;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("UnusedDeclaration")
public interface PlaylistListener extends ListenerTag {

    void onChanged();

    void onNext(@NotNull MediaID mediaID);

    void onPlaylistFinished();

}
