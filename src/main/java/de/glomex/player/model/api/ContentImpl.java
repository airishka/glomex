package de.glomex.player.model.api;

import de.glomex.player.api.playlist.Content;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by <b>me@olexxa.com</b>
 */
public class ContentImpl implements Content {

    final UUID uuid;
    final URL url;

    public ContentImpl(String url) throws MalformedURLException {
        this(UUID.randomUUID(), new URL(url));
    }

    public ContentImpl(URL url) {
        this(UUID.randomUUID(), url);
    }

    public ContentImpl(String uuid, String url) throws MalformedURLException {
        this(UUID.fromString(uuid), new URL(url));
    }

    public ContentImpl(UUID uuid, URL url) {
        this.uuid = uuid;
        this.url = url;
    }

    public UUID uuid() {
        return uuid;
    }

    public URL url() {
        return url;
    }

}
