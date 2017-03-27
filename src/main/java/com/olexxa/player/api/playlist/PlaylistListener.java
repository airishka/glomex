package com.olexxa.player.api.playlist;

/**
 * Created by <b>me@olexxa.com</b>
 */
public interface PlaylistListener {

    void onChanged();

    void onNext(Content content);

    void onFinished();

}
