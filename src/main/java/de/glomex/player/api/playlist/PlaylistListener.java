package de.glomex.player.api.playlist;

import de.glomex.player.api.events.ListenerTag;

/**
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlaylistListener extends ListenerTag {

    void onChanged();

    void onNext(MediaID mediaID);

    void onFinished();

}
