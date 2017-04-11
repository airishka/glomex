package de.glomex.player.model.media;

import de.glomex.player.api.playlist.MediaID;

import java.util.UUID;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class MediaUUID implements MediaID {

    UUID uuid = UUID.randomUUID();

    @Override
    public String toString() {
        return uuid.toString();
    }

}
