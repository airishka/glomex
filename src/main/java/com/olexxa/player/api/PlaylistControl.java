package com.olexxa.player.api;

import java.net.URL;

/**
 * TODO: think: if playlist is immutable, then could be split to building and playing parts
 *
 * Created by <b>me@olexxa.com</b>
 */
@SuppressWarnings("unused")
public interface PlaylistControl {

    void addContent(URL... urls);

    void addContent(Content... contents);

    void removeContent(int index);

    void clear();

    void skipTo(int index);

    void next();

    void prev();

}
