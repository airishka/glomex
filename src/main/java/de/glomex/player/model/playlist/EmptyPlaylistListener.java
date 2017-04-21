package de.glomex.player.model.playlist;

import de.glomex.player.api.media.MediaID;
import de.glomex.player.api.playlist.PlaylistListener;
import org.jetbrains.annotations.NotNull;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class EmptyPlaylistListener implements PlaylistListener {

    @Override
    public void onChanged() {}

    @Override
    public void onNext(@NotNull MediaID mediaID) {}

    @Override
    public void onPlaylistFinished() {}

}
