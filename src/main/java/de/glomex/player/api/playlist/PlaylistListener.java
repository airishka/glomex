package de.glomex.player.api.playlist;

import de.glomex.player.api.events.Listener;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface PlaylistListener extends Listener{

    void onChanged();

    void onNext(Content content);

    void onFinished();

}
